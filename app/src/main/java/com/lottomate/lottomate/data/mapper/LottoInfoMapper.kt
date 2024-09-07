package com.lottomate.lottomate.data.mapper

import android.icu.text.DecimalFormat
import com.lottomate.lottomate.data.model.Lotto645InfoEntity
import com.lottomate.lottomate.data.model.Lotto720InfoEntity
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info

object LottoInfoMapper {
    private const val LOTTO_645_TOTAL_RANK_COUNT = 5
    private const val LOTTO_720_TOTAL_RANK_COUNT = 8

    fun toLotto645Info(lottoInfoEntity: Lotto645InfoEntity): Lotto645Info {
        val lottoWinnerCntList = MutableList(LOTTO_645_TOTAL_RANK_COUNT) { 0 }
        val lottoPrizes = MutableList(LOTTO_645_TOTAL_RANK_COUNT) { 0L }
        with(lottoInfoEntity) {
            lottoWinnerCntList[0] = p1WinnrCnt
            lottoPrizes[0] = p1Jackpot
            lottoWinnerCntList[1] = p2WinnrCnt
            lottoPrizes[1] = p2Jackpot
            lottoWinnerCntList[2] = p3WinnrCnt
            lottoPrizes[2] = p3Jackpot
            lottoWinnerCntList[3] = p4WinnrCnt
            lottoPrizes[3] = p4Jackpot
            lottoWinnerCntList[4] = p5WinnrCnt
            lottoPrizes[4] = p5Jackpot
        }

        val prizePerPerson = List(LOTTO_645_TOTAL_RANK_COUNT) {
            val prize = lottoPrizes[it].div(lottoWinnerCntList[it])
            formatNumberWithCommas(prize)
        }

        return Lotto645Info(
            lottoRound = lottoInfoEntity.drwNum,
            lottoDate = lottoInfoEntity.drwDate,
            lottoNum = lottoInfoEntity.lottoNum,
            lottoBonusNum = lottoInfoEntity.lottoBonusNum,
            lottoWinnerNum = lottoWinnerCntList.map { formatNumberWithCommas(it) },
            lottoPrize = lottoPrizes.map { formatNumberWithCommas(it) },
            lottoPrizePerPerson = prizePerPerson,
            totalSalesPrice = formatNumberWithCommas(lottoInfoEntity.totalSalesPrice),
        )
    }

    fun toLotto720Info(lottoInfoEntity: Lotto720InfoEntity): Lotto720Info {
        val lottoWinnerCntList = MutableList(LOTTO_720_TOTAL_RANK_COUNT) { 0 }
        with(lottoInfoEntity) {
            lottoWinnerCntList[0] = p1WinnrCnt
            lottoWinnerCntList[1] = p2WinnrCnt
            lottoWinnerCntList[2] = p3WinnrCnt
            lottoWinnerCntList[3] = p4WinnrCnt
            lottoWinnerCntList[4] = p5WinnrCnt
            lottoWinnerCntList[5] = p6WinnrCnt
            lottoWinnerCntList[6] = p7WinnrCnt
            lottoWinnerCntList[7] = p8WinnrCnt
        }
        return Lotto720Info(
            lottoRound = lottoInfoEntity.drwNum,
            lottoDate = lottoInfoEntity.drwDate,
            lottoNum = lottoInfoEntity.lottoNum,
            lottoBonusNum = lottoInfoEntity.lottoBonusNum,
            lottoWinnerNum = lottoWinnerCntList.map { formatNumberWithCommas(it) },
        )
    }

    private fun formatNumberWithCommas(num: Long): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(num)
    }

    private fun formatNumberWithCommas(num: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(num)
    }
}