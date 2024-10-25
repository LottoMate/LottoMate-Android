package com.lottomate.lottomate.presentation.screen.pocket

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.local.repository.RandomLottoRepository
import com.lottomate.lottomate.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PocketViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val randomLottoRepository: RandomLottoRepository,
) : ViewModel() {
    var currentTabIndex = mutableIntStateOf(0)

    private var _snackBarFlow = MutableSharedFlow<String>()
    private var _drewRandomNumbers = MutableStateFlow<List<List<Int>>>(emptyList())

    val snackBarFlow: SharedFlow<String> get() = _snackBarFlow.asSharedFlow()
    val drewRandomNumbers: StateFlow<List<List<Int>>> get() = _drewRandomNumbers.asStateFlow()

    init {
        fetchDrewRandomNumbers()

        viewModelScope.launch {
            randomLottoRepository.dataChanged.collect { fetchDrewRandomNumbers() }
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
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(CLIPBOARD_LABEL, copyData)

        clipboardManager.setPrimaryClip(clip)

        viewModelScope.launch {
            _snackBarFlow.emit(SNACKBAR_MESSAGE)
        }
    }

    /**
     * 이전 날짜에 뽑은 로또 번호를 삭제하는 함수
     */
    fun resetDrewRandomLottoNumbers() {
        viewModelScope.launch {
            val savedRandomLottoList = randomLottoRepository.fetchAllRandomLotto()

            // 저장된 모든 로또가 지난 날짜인 경우
            if (savedRandomLottoList.all { DateUtils.isDateInPast(it.createAt) }) {
                randomLottoRepository.deleteAllRandomLotto()
            } else {
                savedRandomLottoList
                    .filter { DateUtils.isDateInPast(it.createAt) }
                    .forEach { randomLottoRepository.deleteOneOfRandomLotto(it.key) }
            }
        }
    }

    private fun fetchDrewRandomNumbers() {
        viewModelScope.launch {
            randomLottoRepository.fetchAllRandomLottoOnlyNumbers()
                .collectLatest { collect ->
                    _drewRandomNumbers.update { collect.toList() }
                }
        }
    }

    companion object {
        private const val SNACKBAR_MESSAGE = "로또 번호를 복사했어요!"
        private const val CLIPBOARD_LABEL = "RandomNumbers"
    }
}