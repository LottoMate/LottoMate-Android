package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.runtime.mutableStateOf
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoMockDatas
import com.lottomate.lottomate.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LottoRoundViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val lottoInfoRepository: LottoInfoRepository,
) : BaseViewModel(errorHandler) {
    val latestLottoInfo: StateFlow<Map<Int, LatestRoundInfo>>
        get() = lottoInfoRepository.latestLottoRoundInfo

    var lottoRoundRange = mutableStateOf(emptyList<LatestRoundInfo>())
        private set

    fun setLottoRoundRange(lottoType: LottoType, isRegisterScreen: Boolean) {
        getLatestRound(lottoType)?.let { info ->
            val latestRound = info.round
            val latestDate = info.drawDate.replace("-", ".")


            when (lottoType) {
                LottoType.L645, LottoType.L720 -> {
                    if (isRegisterScreen) {
                        val roundRange = (latestRound-49)..(latestRound+1)
                        val reversedRounds = roundRange.toList().reversed()

                        lottoRoundRange.value = reversedRounds.mapIndexed { index, round ->
                            if (round > latestRound || round < (latestRound-48)) LatestRoundInfo(0, "")
                            else LatestRoundInfo(round, DateUtils.calLottoRoundDate(latestDate, index-1))
                        }.toList()
                    } else {
                        val roundRange = 0..(latestRound+1)
                        val reversedRounds = roundRange.toList().reversed()

                        lottoRoundRange.value = reversedRounds.mapIndexed { index, round ->
                            if (round > latestRound || round < LOTTO_FIRST_ROUND) LatestRoundInfo(0, "")
                            else LatestRoundInfo(round, DateUtils.calLottoRoundDate(latestDate, index-1))
                        }.toList().reversed()
                    }

                }
                else -> {
                    lottoRoundRange.value = (LOTTO_FIRST_ROUND.minus(1)..(SpeettoMockDatas.lastPage).plus(1)).map { page ->
//                    if (page == 0 || page == SpeettoMockDatas.lastPage.plus(1)) ""
//                    else page.toString()
                        LatestRoundInfo(page, "")
                    }.toList()
                }
            }
        }
    }

    /**
     * 최신 당첨 회차 및 당첨일을 가져옵니다.
     */
    private fun getLatestRound(lotteryType: LottoType): LatestRoundInfo? {
        return latestLottoInfo.value[lotteryType.num]
    }

    companion object {
        private const val LOTTO_FIRST_ROUND = 1
    }
}