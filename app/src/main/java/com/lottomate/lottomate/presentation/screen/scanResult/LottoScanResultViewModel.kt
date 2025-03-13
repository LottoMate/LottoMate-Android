package com.lottomate.lottomate.presentation.screen.scanResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.domain.repository.LottoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LottoScanResultViewModel @Inject constructor(
    private val lottoRepository: LottoRepository,
) : ViewModel() {
    private var _lottoWinResultInfo = MutableStateFlow<LottoScanResultUiState>(LottoScanResultUiState.Loading)
    val lottoWinResultInfo: StateFlow<LottoScanResultUiState> get() = _lottoWinResultInfo.asStateFlow()

    fun getLottoResultByRound(data: String) {
        viewModelScope.launch {
//            val round = data.split("q")[0]
            val winNumbers = data.split("q").drop(1).mapNotNull {
                it.chunked(2).mapNotNull { chunk -> chunk.toIntOrNull() }
            }
//            val winNumbers = listOf(
//                listOf(2, 11, 31, 33, 37, 45),
//                listOf(2, 11, 31, 33, 37, 44),
//                listOf(2, 11, 31, 33, 37, 45),
//                listOf(2, 11, 31, 33, 37, 45),
//                listOf(2, 11, 31, 33, 37, 45),
//            )
            val winResult = lottoRepository.fetchLottoWinResultByRound("1199")

            when {
                winResult.isEmpty() -> {
                    // 아직 당첨 발표 전
                    _lottoWinResultInfo.update {
                        delay(3_000)
                        LottoScanResultUiState.NotYet
                    }
                }
                else -> {
                    val bonusNum = winResult.last()
                    val result = winNumbers.map { list -> list.count { it in winResult } }
                    val bonusResult = winNumbers.map { list -> bonusNum in list }

                    val emptyWinResult = mutableListOf<Int>()
                    winNumbers.forEachIndexed { index, _ ->
                        val winRank = when (result[index]) {
                            6 -> 1
                            5 -> {
                                if (bonusResult[index]) 2 else 3
                            }
                            4 -> 4
                            3 -> 5
                            else -> -1
                        }

                        emptyWinResult.add(winRank)
                    }


                    _lottoWinResultInfo.update {
                        delay(3_000)
                        LottoScanResultUiState.Success(emptyWinResult)
                    }
                }
            }
        }
    }
}


sealed interface LottoScanResultUiState {
    data object Loading : LottoScanResultUiState
    data object NotYet : LottoScanResultUiState
    data class Success(val data: List<Int>) : LottoScanResultUiState
}