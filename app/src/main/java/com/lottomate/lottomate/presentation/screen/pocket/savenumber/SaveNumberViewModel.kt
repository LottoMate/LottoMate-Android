package com.lottomate.lottomate.presentation.screen.pocket.savenumber

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SaveNumberViewModel @Inject constructor(
    private val lottoInfoRepository: LottoInfoRepository,
) : ViewModel() {
    var hasPreLottoRound = mutableStateOf(true)
        private set
    var hasNextLottoRound = mutableStateOf(false)
        private set

    private var _lottoRoundUiState = MutableStateFlow(LottoRoundUiState.Loading)
    val lottoRoundUiState: StateFlow<LottoRoundUiState> get() = _lottoRoundUiState.asStateFlow()

    private fun judgePreOrNextLottoRound(lottoRndNum: Int) {
//        val currentLottoType = LottoType.findLottoType(currentTabMenu.intValue).num
        val latestLottoRound = lottoInfoRepository.allLatestLottoRound.getValue(lottoRndNum)

        hasNextLottoRound.value = lottoRndNum != latestLottoRound
        hasPreLottoRound.value = lottoRndNum != LOTTO_FIRST_ROUND
    }

    fun loadLotto645Round() {
//        viewModelScope.launch {
//            lottoInfoRepository.fetchLottoInfo(LottoType.L645.num, 1126)
//                .collectLatest { lottoInfo ->
//                    lottoInfo.lottoRound?.let { round ->
//                        judgePreOrNextLottoRound(round)
//                    }
//
//                    _lottoRoundUiState.update {
//                        LottoRoundUiState.Success(lottoInfo)
//                    }
//                }
//        }
    }

    companion object {
        private const val LOTTO_FIRST_ROUND = 1
    }
}

sealed interface LottoRoundUiState {
    data object Loading: LottoRoundUiState
    data class Success(val round: String, val date: String): LottoRoundUiState
    data class Failed(val message: String, val throwable: Throwable? = null): LottoRoundUiState
}