package com.lottomate.lottomate.presentation.screen.scanResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LottoScanResultViewModel @Inject constructor(
    private val lottoRepository: LottoRepository,
    private val lottoInfoRepository: LottoInfoRepository,
) : ViewModel() {
    private var _lottoWinResultInfo = MutableStateFlow<LottoScanResultUiState>(LottoScanResultUiState.Loading)
    val lottoWinResultInfo: StateFlow<LottoScanResultUiState> get() = _lottoWinResultInfo.asStateFlow()

    fun getLottoResultByRound(data: String) {
        viewModelScope.launch {
            val round = data.split("q")[0]

            // 구매한 로또 번호 목록
            val purchasedLottoNumbers = data.split("q").drop(1).mapNotNull {
                it.chunked(2).mapNotNull { chunk -> chunk.toIntOrNull() }
            }

            // 당첨 숫자 목록
            val winningNumbers = async { lottoRepository.fetchLottoWinResultByRound(round) }.await()
            lottoInfoRepository.fetchLottoInfo(LottoType.L645.num, round.toInt())
                .onStart {
                    _lottoWinResultInfo.update {
                        LottoScanResultUiState.Loading
                    }
                }
                .collectLatest { collectData ->
                    val winResultInfo = collectData as Lotto645Info

                    when {
                        winningNumbers.isEmpty() -> {
                            // 당첨 발표 전
                            _lottoWinResultInfo.update {
                                LottoScanResultUiState.NotYet
                            }
                        }
                        else -> {
                            // 보너스 숫자 (1개)
                            val bonusNum = winningNumbers.last()
                            // 당첨 숫자 (6개)
                            val winNumbers = winningNumbers.filter { it != bonusNum }
                            // 구매한 로또 번호 중 당첨 번호와 일치하는 번호의 개수
                            // 6개(1등) / 5개(2,3등) / 4개(4등) / 3개(5등) / 그 외(낙첨)
                            val matchingCounts = purchasedLottoNumbers.map { list -> list.filter { it != bonusNum }.count { it in winNumbers } }
                            val hasBonusNumber = purchasedLottoNumbers.map { list -> bonusNum in list }

                            val lottoResults = mutableListOf<LottoResultInfo?>()
                            purchasedLottoNumbers.forEachIndexed { index, _ ->
                                val lottoResult = when (matchingCounts[index]) {
                                    6 -> LottoResultInfo(1, winResultInfo.lottoPrizePerPerson[0])
                                    5 -> {
                                        if (hasBonusNumber[index]) LottoResultInfo(2, winResultInfo.lottoPrizePerPerson[1])
                                        else LottoResultInfo(3, winResultInfo.lottoPrizePerPerson[2])
                                    }
                                    4 -> LottoResultInfo(4, winResultInfo.lottoPrizePerPerson[3])
                                    3 -> LottoResultInfo(5, winResultInfo.lottoPrizePerPerson[4])
                                    else -> null
                                }

                                lottoResults.add(lottoResult)
                            }

                            val isWinning = lottoResults.any { it != null }

                            when (isWinning) {
                                true -> {
                                    _lottoWinResultInfo.update { LottoScanResultUiState.Loading }
                                    delay(1_500)
                                    _lottoWinResultInfo.update { LottoScanResultUiState.CelebrationLoading }
                                    delay(1_500)
                                }
                                false -> delay(2_000)
                            }

                            _lottoWinResultInfo.update {
                                LottoScanResultUiState.Success(lottoResults)
                            }
                        }
                    }
                }
        }
    }
}


sealed interface LottoScanResultUiState {
    data object Loading : LottoScanResultUiState
    // 당첨되어 결과를 기다릴때 보여주는 로딩 화면
    data object CelebrationLoading : LottoScanResultUiState
    data object NotYet : LottoScanResultUiState
    data class Success(val data: List<LottoResultInfo?>) : LottoScanResultUiState
}

data class LottoResultInfo(
    val rank: Int,
    val price: String,
)