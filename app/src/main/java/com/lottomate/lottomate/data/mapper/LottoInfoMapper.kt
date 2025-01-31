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
        val prizePerPerson = MutableList(LOTTO_645_TOTAL_RANK_COUNT) { 0L }
        with(lottoInfoEntity) {
            lottoWinnerCntList[0] = p1WinnrCnt
            lottoPrizes[0] = p1Jackpot
            prizePerPerson[0] = p1PerJackpot
            lottoWinnerCntList[1] = p2WinnrCnt
            lottoPrizes[1] = p2Jackpot
            prizePerPerson[1] = p2PerJackpot
            lottoWinnerCntList[2] = p3WinnrCnt
            lottoPrizes[2] = p3Jackpot
            prizePerPerson[2] = p3PerJackpot
            lottoWinnerCntList[3] = p4WinnrCnt
            lottoPrizes[3] = p4Jackpot
            prizePerPerson[3] = p4PerJackpot
            lottoWinnerCntList[4] = p5WinnrCnt
            lottoPrizes[4] = p5Jackpot
            prizePerPerson[4] = p5PerJackpot
        }

        return Lotto645Info(
            lottoRound = lottoInfoEntity.drwNum,
            lottoDate = lottoInfoEntity.drwDate.replace("-", "."),
            lottoNum = lottoInfoEntity.lottoNum,
            lottoBonusNum = lottoInfoEntity.lottoBonusNum,
            lottoWinnerNum = lottoWinnerCntList.map { formatNumberWithCommas(it) },
            lottoPrize = lottoPrizes.map { formatNumberWithCommas(it) },
            lottoPrizePerPerson = prizePerPerson.map { formatNumberWithCommas(it) },
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
        }
        return Lotto720Info(
            lottoRound = lottoInfoEntity.drwNum,
            lottoDate = lottoInfoEntity.drwDate.replace("-", "."),
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