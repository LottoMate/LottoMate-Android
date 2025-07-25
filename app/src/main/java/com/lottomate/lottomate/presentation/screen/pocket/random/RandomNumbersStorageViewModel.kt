package com.lottomate.lottomate.presentation.screen.pocket.random

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.domain.repository.local.RandomMyNumbersRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.pocket.random.contract.RandomMyNumbersEffect
import com.lottomate.lottomate.presentation.screen.pocket.random.contract.RandomMyNumbersEvent
import com.lottomate.lottomate.presentation.screen.pocket.random.contract.RandomMyNumbersUiState
import com.lottomate.lottomate.utils.ClipboardUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class RandomNumbersStorageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    errorHandler: LottoMateErrorHandler,
    private val randomMyNumberRepository: RandomMyNumbersRepository,
) : BaseViewModel(errorHandler) {
    private val _effect = Channel<RandomMyNumbersEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    val state = randomMyNumberRepository.getAllRandomMyNumbers()
        .map { myNumbersGroups ->
            if (myNumbersGroups.isEmpty()) RandomMyNumbersUiState.Empty
            else RandomMyNumbersUiState.Success(myNumbersGroups.toUiModel())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RandomMyNumbersUiState.Idle)

    fun handleEvent(event: RandomMyNumbersEvent) {
        when (event) {
            is RandomMyNumbersEvent.CopyRandomNumbers -> copyLottoNumbers(event.numbers)
            is RandomMyNumbersEvent.RemoveRandomNumbers -> deleteLottoNumbers(event.id)
        }
    }

    /**
     * 랜덤 뽑기에 대한 숫자 복사 기능
     *
     * Android 12(API 32) 이하에서는 사용자에게 복사되었음을 표시해주는 것을 권장
     *
     * [공식문서 참고](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste?hl=ko#PastePlainText)
     */
    private fun copyLottoNumbers(numbers: List<Int>) {
        val copyData = numbers.joinToString("-")

        ClipboardUtils.copyToClipboard(
            context = context,
            copyText = copyData,
            onSuccess = {
                viewModelScope.launch {
                    _effect.send(RandomMyNumbersEffect.ShowSnackBar(R.string.random_my_numbers_message_copy))
                }
            }
        )
    }

    /**
     * 저장한 번호를 삭제하는 함수
     *
     * @param key 해당 model을 가리키는 key 값
     */
    private fun deleteLottoNumbers(key: Int) {
        viewModelScope.launch {
            randomMyNumberRepository.deleteRandomMyNumbers(key)
                .onSuccess {
                    _effect.send(RandomMyNumbersEffect.ShowSnackBar(R.string.random_my_numbers_message_remove))
                }
        }
    }
}