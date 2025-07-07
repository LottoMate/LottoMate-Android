package com.lottomate.lottomate.presentation.screen.pocket.my

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.domain.repository.MyNumberRepository
import com.lottomate.lottomate.domain.repository.UserRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberContract
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberDetailUiModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyNumberViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val myNumberRepository: MyNumberRepository,
    private val userRepository: UserRepository,
) : BaseViewModel(errorHandler) {
    private val _effect = Channel<MyNumberContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private val _isEdit = MutableStateFlow(false)
    val isEdit get() = _isEdit.asStateFlow()

    val myNumbers: StateFlow<MyNumberUiModel> = myNumberRepository.myNumbers
        .map { it.toUiModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MyNumberUiModel()
        )

    init {
        userRepository.userProfile.value?.let { profile ->
            viewModelScope.launch {
                myNumberRepository.getAllMyNumber()
                    .onFailure { handleException(it) }
            }
        }
    }

    fun handleEvent(event: MyNumberContract.Event) {
        when (event) {
            is MyNumberContract.Event.DeleteMyNumber -> deleteMyNumber(event.myNumberId)
            is MyNumberContract.Event.ClickCheckWin -> checkLotteryWin(event.myNumberDetail, event.numbers)
            MyNumberContract.Event.ChangeMode -> changeMode()
        }
    }

    private fun changeMode() {
        _isEdit.update { !it }
    }

    private fun deleteMyNumber(id: Int) {
        viewModelScope.launch {
            myNumberRepository.deleteMyNumber(id)
                .onSuccess {
                    _effect.send(MyNumberContract.Effect.ShowSnackBar("내 로또 내역을 삭제했어요"))
                }
                .onFailure { handleException(it) }
        }
    }

    private fun checkLotteryWin(detail: MyNumberDetailUiModel, numbers: List<Int>) {
        viewModelScope.launch {
            _effect.send(MyNumberContract.Effect.NavigateToLotteryResylt(detail.type, detail.round, numbers))
        }
    }
}