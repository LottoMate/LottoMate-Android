package com.lottomate.lottomate.data.mapper

import android.icu.text.DecimalFormat
import com.lottomate.lottomate.data.model.Lotto645InfoEntity
import com.lottomate.lottomate.data.model.Lotto720InfoEntity
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info

object LottoInfoMapper {
    fun toLotto645Info(lottoInfoEntity: Lotto645InfoEntity): Lotto645Info {
        return Lotto645Info(
            lottoRndNum = lottoInfoEntity.lottoRndNum,
            drwtDate = lottoInfoEntity.drwtDate,
            prizeMoney = lottoInfoEntity.prizeMoney.map {
                formatNumberWithCommas(it)
            },
            drwtWinNum = lottoInfoEntity.drwtWinNum.map {
                formatNumberWithCommas(it)
            },
            drwtMoney = lottoInfoEntity.drwtMoney.map {
                formatNumberWithCommas(it)
            },
            drwtNum = lottoInfoEntity.drwtNum,
            drwtBonusNum = lottoInfoEntity.drwtBonusNum,
            drwtSaleMoney = lottoInfoEntity.drwtSaleMoney
        )
    }

    fun toLotto720Info(lottoInfoEntity: Lotto720InfoEntity): Lotto720Info {
        return Lotto720Info(
            lottoRndNum = lottoInfoEntity.lottoRndNum,
            drwtDate = lottoInfoEntity.drwtDate,
            drwtWinNum = lottoInfoEntity.drwtWinNum.map {
                formatNumberWithCommas(it)
            },
            drwtNum = lottoInfoEntity.drwtNum,
            drwtBonusNum = lottoInfoEntity.drwtBonusNum
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