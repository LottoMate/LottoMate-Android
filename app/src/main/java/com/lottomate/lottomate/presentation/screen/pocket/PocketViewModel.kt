package com.lottomate.lottomate.presentation.screen.pocket

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.local.RandomLottoRepository
import com.lottomate.lottomate.domain.repository.LottoNumberRepository
import com.lottomate.lottomate.domain.repository.UserRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.utils.ClipboardUtils
import com.lottomate.lottomate.utils.DateUtils
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PocketViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    errorHandler: LottoMateErrorHandler,
    private val randomLottoRepository: RandomLottoRepository,
    private val lottoNumberRepository: LottoNumberRepository,
    private val userRepository: UserRepository,
) : BaseViewModel(errorHandler) {
    val userProfile = userRepository.userProfile
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
        val copyData = numbers.joinToString("-") { String.format("%02d", it) }

        ClipboardUtils.copyToClipboard(
            context = context,
            copyText = copyData,
            onSuccess = {
                viewModelScope.launch {
                    _snackBarFlow.emit("로또 번호를 복사했어요")
                }
            }
        )
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

    /**
     * 랜덤 번호 뽑기에서 뽑은 번호를 DB에 저장하는 함수
     */
    fun saveDrewRandomNumber(numbers: List<Int>) {
        viewModelScope.launch {
            runCatching {
                lottoNumberRepository.saveLottoNumber(numbers)
            }.onSuccess {
                _snackBarFlow.emit("로또 번호를 저장했어요")
            }.onFailure { handleException(it) }
        }
    }

    private fun fetchDrewRandomNumbers() {
        viewModelScope.launch {
            randomLottoRepository.fetchAllRandomLottoOnlyNumbers()
                .catch { handleException(it) }
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