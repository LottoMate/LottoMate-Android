package com.lottomate.lottomate.presentation.screen.pocket

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomNumbersStorageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private var _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow: SharedFlow<String> get() = _snackBarFlow.asSharedFlow()

    /**
     * 랜덤 뽑기에 대한 숫자 복사 기능
     *
     * Android 12(API 32) 이하에서는 사용자에게 복사되었음을 표시해주는 것을 권장
     *
     * [공식문서 참고](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste?hl=ko#PastePlainText)
     */
    fun copyLottoNumbers(numbers: List<Int>) {
        val copyData = numbers.joinToString("-")
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(CLIPBOARD_LABEL, copyData)

        clipboardManager.setPrimaryClip(clip)

        viewModelScope.launch {
            _snackBarFlow.emit(SNACKBAR_MESSAGE)
        }
    }

    /**
     * 저장한 번호를 삭제하는 함수
     *
     * @param key 해당 model을 가리키는 key 값
     */
    fun deleteLottoNumbers(key: Int) {
        // TODO : Remote Database 연결 -> 삭제 진행

        viewModelScope.launch {
            _snackBarFlow.emit(SNACKBAR_MESSAGE_DELETE)
        }
    }

    companion object {
        private const val SNACKBAR_MESSAGE = "로또 번호를 복사했어요!"
        private const val SNACKBAR_MESSAGE_DELETE = "로또 번호를 삭제했어요."
        private const val CLIPBOARD_LABEL = "RandomNumbers"
    }
}