package com.lottomate.lottomate.presentation.screen.pocket.random

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.LottoNumberRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomLottoNumber
import com.lottomate.lottomate.utils.ClipboardUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomNumbersStorageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    errorHandler: LottoMateErrorHandler,
    private val lottoNumberRepository: LottoNumberRepository
) : BaseViewModel(errorHandler) {
    private var _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow: SharedFlow<String> get() = _snackBarFlow.asSharedFlow()

    private var _lottoNumbers = MutableStateFlow<List<RandomLottoNumber>>(emptyList())
    val lottoNumbers: StateFlow<List<RandomLottoNumber>> get() = _lottoNumbers.asStateFlow()

    init {
        loadLottoNumbers()
    }

    private fun loadLottoNumbers() {
        viewModelScope.launch {
            lottoNumberRepository.getLottoNumbers()
                .map { list ->
                    list.map { item ->
                        item.copy(date = item.date.replace("-", "."))
                    }.sortedBy { it.date }
                }
                .catch { handleException(it) }
                .collectLatest { collectData ->
                    _lottoNumbers.update { collectData }
                }
        }
    }

    /**
     * 랜덤 뽑기에 대한 숫자 복사 기능
     *
     * Android 12(API 32) 이하에서는 사용자에게 복사되었음을 표시해주는 것을 권장
     *
     * [공식문서 참고](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste?hl=ko#PastePlainText)
     */
    fun copyLottoNumbers(numbers: List<Int>) {
        val copyData = numbers.joinToString("-")

        ClipboardUtils.copyToClipboard(
            context = context,
            copyText = copyData,
            onSuccess = {
                viewModelScope.launch {
                    _snackBarFlow.emit(SNACKBAR_MESSAGE)
                }
            }
        )
    }

    /**
     * 저장한 번호를 삭제하는 함수
     *
     * @param key 해당 model을 가리키는 key 값
     */
    fun deleteLottoNumbers(key: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                lottoNumberRepository.removeLottoNumber(key)
            }.onSuccess {
                _snackBarFlow.emit(SNACKBAR_MESSAGE_DELETE)
            }.onFailure { handleException(it) }
        }
    }

    companion object {
        private const val SNACKBAR_MESSAGE = "로또 번호를 복사했어요"
        private const val SNACKBAR_MESSAGE_DELETE = "로또 번호를 삭제했어요"
    }
}