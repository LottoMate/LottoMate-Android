package com.lottomate.lottomate.presentation.screen.pocket.my

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.domain.repository.MyNumberRepository
import com.lottomate.lottomate.domain.repository.UserRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyNumberViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val myNumberRepository: MyNumberRepository,
    private val userRepository: UserRepository,
) : BaseViewModel(errorHandler) {
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
}