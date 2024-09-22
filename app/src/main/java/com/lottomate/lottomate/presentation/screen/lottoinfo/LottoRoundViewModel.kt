package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LottoRoundViewModel @Inject constructor(
    private val lottoInfoRepository: LottoInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<LottoBottomWheelUiState>(LottoBottomWheelUiState.Loading)
    val uiState: StateFlow<LottoBottomWheelUiState>
        get() = _uiState.asStateFlow()
    var lottoRoundRange = mutableStateOf(emptyList<String>())
        private set

    init {
        loadLatestLottoInfo(LottoType.L645)
    }

    fun getLatestLottoInfo(tabIndex: Int) {
        val lottoType = LottoType.findLottoType(tabIndex)

        if (lottoType == LottoType.L645 || lottoType == LottoType.L720) {
            loadLatestLottoInfo(lottoType)
            setLottoRoundRange(lottoType)
        }
    }

    private fun loadLatestLottoInfo(lottoType: LottoType) {
        val round = lottoInfoRepository.allLatestLottoRound.getValue(lottoType.num)

        viewModelScope.launch {
            lottoInfoRepository.fetchLottoInfo(lottoType.num, round)
                .collectLatest { info ->
                    _uiState.update {
                        LottoBottomWheelUiState.Success(round = info.lottoRound!!, date = info.lottoDate!!)
                    }
                }

            setLottoRoundRange(lottoType)
        }
    }

    private fun setLottoRoundRange(lottoType: LottoType) {
        val latestLottoRound = lottoInfoRepository.allLatestLottoRound.getValue(lottoType.num)

        lottoRoundRange.value = (LOTTO_FIRST_ROUND..(latestLottoRound.plus(1))).map { round ->
            if (round > latestLottoRound) ""
            else round.toString()
        }.toList().reversed()
    }

    companion object {
        private const val LOTTO_FIRST_ROUND = 1
    }
}

sealed interface LottoBottomWheelUiState {
    data object Loading : LottoBottomWheelUiState
    data class Success(val round: Int, val date: String) : LottoBottomWheelUiState
}