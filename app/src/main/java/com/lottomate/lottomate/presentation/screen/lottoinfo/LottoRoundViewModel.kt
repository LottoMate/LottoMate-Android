package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LottoRoundViewModel @Inject constructor(
    private val lottoInfoRepository: LottoInfoRepository,
) : ViewModel() {
    private val _latestLottoRound = MutableStateFlow(INIT_LOTTO645_ROUND)
    val latestLottoRound: StateFlow<Int> = _latestLottoRound.asStateFlow()
    private val _latestLottoDate = MutableStateFlow(INIT_LOTTO645_DATE)
    val latestLottoDate: StateFlow<String> = _latestLottoDate.asStateFlow()
    var lottoRoundRange = mutableStateOf(emptyList<String>())
        private set

    fun getLatestLottoInfo(tabIndex: Int) {
        val lottoType = LottoType.findLottoType(tabIndex)

        loadLatestLottoInfo(lottoType)
        setLottoRoundRange()
    }

    private fun loadLatestLottoInfo(lottoType: LottoType) {
        with(lottoInfoRepository.latestLottoInfo.getValue(lottoType.num)) {
            _latestLottoRound.update { lottoRound }
            _latestLottoDate.update { lottoDate }
        }
    }

    private fun setLottoRoundRange() {
        lottoRoundRange.value = (LOTTO_FIRST_ROUND..(latestLottoRound.value.plus(1))).map { round ->
            if (round > latestLottoRound.value) ""
            else round.toString()
        }.toList().reversed()
    }

    companion object {
        private const val INIT_LOTTO645_ROUND = 1131
        private const val INIT_LOTTO645_DATE = "2024-08-03"
        private const val LOTTO_FIRST_ROUND = 1
    }
}