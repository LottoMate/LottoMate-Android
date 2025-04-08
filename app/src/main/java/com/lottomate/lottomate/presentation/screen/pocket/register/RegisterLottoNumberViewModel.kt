package com.lottomate.lottomate.presentation.screen.pocket.register

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterLottoNumber
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterNavigationType
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
class RegisterLottoNumberViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val lottoInfoRepository: LottoInfoRepository,
) : ViewModel() {
    private val latestLottoRoundInfo: StateFlow<Map<Int, LatestRoundInfo>>
        get() = lottoInfoRepository.latestLottoRoundInfo

    private val _currentLotto645Round = MutableStateFlow(LatestRoundInfo.EMPTY)
    private val _currentLotto720Round = MutableStateFlow(LatestRoundInfo.EMPTY)
    val currentLotto645Round: StateFlow<LatestRoundInfo>
        get() = _currentLotto645Round.asStateFlow()
    val currentLotto720Round: StateFlow<LatestRoundInfo>
        get() = _currentLotto720Round.asStateFlow()

    private val _registerNavigationFlow = MutableStateFlow<RegisterNavigationType>(RegisterNavigationType.None)
    val registerNavigationFlow: StateFlow<RegisterNavigationType>
        get() = _registerNavigationFlow.asStateFlow()

    var hasLotto645PreRound = mutableStateOf(true)
        private set
    var hasLotto645NextRound = mutableStateOf(false)
        private set
    var hasLotto720PreRound = mutableStateOf(true)
        private set
    var hasLotto720NextRound = mutableStateOf(false)
        private set

    private var _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow: SharedFlow<String> get() = _snackBarFlow.asSharedFlow()

    init {
        loadLatestLottoRoundInfo()
    }

    fun saveLottoNumbers(inputLotto645List: List<RegisterLottoNumber>, inputLotto720List: List<RegisterLottoNumber>) {
        // 로또 번호 저장
        val msg = context.getString(R.string.register_lotto_number_text_complete)
        sendSnackBarMsg(msg)

        // 화면 전환 확인
        checkNavigation(inputLotto645List, inputLotto720List)
    }

    /**
     * 번호 등록 후, 이동되어질 화면을 결정합니다.
     *
     * - 최신 회차일 경우: 보관소(내 번호 Tab)로 이동
     * - 이전 회차일 경우: 결과 화면으로 이동
     */
    private fun checkNavigation(
        inputLotto645List: List<RegisterLottoNumber>,
        inputLotto720List: List<RegisterLottoNumber>
    ) {
        val has645 = inputLotto645List.isNotEmpty()
        val has720 = inputLotto720List.isNotEmpty()

        // 각각의 로또가 최신 회차인지 여부 판단
        val is645Latest = isLatestRound(LottoType.L645)
        val is720Latest = isLatestRound(LottoType.L720)

        when {
            has645 && has720 -> {
                // 로또645와 연금복권720 모두 입력된 경우
                when {
                    // 두 회차 모두 최신 → 보관소로 이동
                    is645Latest && is720Latest -> navigateBack()

                    // 둘 중 하나라도 이전 회차일 경우 → 결과 화면으로 이동
                    else -> {
                        // 하나 또는 둘 다 이전 회차 → 결과 화면으로 이동
                        val resultList = mutableListOf<RegisterLottoNumber>()

                        if (!is645Latest) resultList += inputLotto645List
                        if (!is720Latest) resultList += inputLotto720List

                        navigateToLottoResult(resultList)
                    }
                }
            }

            has645 -> {
                if (is645Latest) navigateBack() // 최신 → 보관소
                else navigateToLottoResult(inputLotto645List) // 이전 → 결과 화면
            }

            has720 -> {
                if (is720Latest) navigateBack() // 최신 → 보관소
                else navigateToLottoResult(inputLotto720List) // 이전 → 결과 화면
            }
        }
    }

    /**
     * 보관소(내 번호 탭)로 이동
     */
    private fun navigateBack() {
        _registerNavigationFlow.update { RegisterNavigationType.Back }
    }

    /**
     * 결과 화면으로 이동
     * - TODO: 실제 결과 데이터 전달 필요
     */
    private fun navigateToLottoResult(inputLottoNumbers: List<RegisterLottoNumber>) {
        _registerNavigationFlow.update {
            RegisterNavigationType.LottoResult(
                emptyList() // TODO: 실제 결과 데이터 리스트로 교체 필요
            )
        }
    }

    fun resetNavigation() {
        _registerNavigationFlow.value = RegisterNavigationType.None
    }


    private fun isLatestRound(lottoType: LottoType): Boolean {
        return when (lottoType) {
            LottoType.L645 -> {
                currentLotto645Round.value.round == latestLottoRoundInfo.value.getValue(lottoType.num).round
            }
            LottoType.L720 -> {
                currentLotto720Round.value.round == latestLottoRoundInfo.value.getValue(lottoType.num).round
            }
            else -> {
                val msg = context.getString(R.string.register_lotto_number_text_error)
                sendSnackBarMsg(msg)
                false
            }
        }
    }


    fun updateLottoRoundByType(lottoType: LottoType, roundInfo: LatestRoundInfo) {
        when (lottoType) {
            LottoType.L645 -> _currentLotto645Round.update { roundInfo }
            LottoType.L720 -> _currentLotto720Round.update { roundInfo }
            else -> {}
        }

        checkPreOrNextLottoRound(lottoType, roundInfo.round)
    }

    /**
     * 현재 회차로부터 이전/다음 회차가 존재하는지 확인합니다.
     *
     * @param lotteryType
     * @param lottoRndNum 현재 선택된 로또 회차
     */
    private fun checkPreOrNextLottoRound(lotteryType: LottoType, lottoRndNum: Int) {
        val latestLottoRound = latestLottoRoundInfo.value.getValue(lotteryType.num)

        when (lotteryType) {
            LottoType.L645 -> {
                hasLotto645NextRound.value = lottoRndNum != latestLottoRound.round
                hasLotto645PreRound.value = lottoRndNum != latestLottoRound.round - REGISTRABLE_ROUND_LIMIT
            }
            LottoType.L720 -> {
                hasLotto720NextRound.value = lottoRndNum != latestLottoRound.round
                hasLotto720PreRound.value = lottoRndNum != latestLottoRound.round - REGISTRABLE_ROUND_LIMIT
            }
            else -> {}
        }
    }

    /**
     * 로또645/연금복권720의 최신 회차 정보를 가져옵니다.
     */
    private fun loadLatestLottoRoundInfo() {
        viewModelScope.launch {
            latestLottoRoundInfo
                .collectLatest {
                    val lotto645Round = it.getValue(LottoType.L645.num)
                    val lotto720Round = it.getValue(LottoType.L720.num)

                    _currentLotto645Round.update { lotto645Round }
                    _currentLotto720Round.update { lotto720Round }
                }
        }
    }

    private fun sendSnackBarMsg(message: String) {
        viewModelScope.launch {
            _snackBarFlow.emit(message)
        }
    }

    companion object {
        private const val REGISTRABLE_ROUND_LIMIT = 48
    }
}