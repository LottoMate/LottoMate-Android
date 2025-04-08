package com.lottomate.lottomate.presentation.screen.pocket.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun sendSnackBarMsg(message: String) {
        viewModelScope.launch {
            _snackBarFlow.emit(message)
        }
    }

    companion object {
        private const val REGISTRABLE_ROUND_LIMIT = 48
    }
}