package com.lottomate.lottomate.presentation.screen.pocket.my.model

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.screen.pocket.model.LottoCondition
import java.util.Locale
import kotlin.math.roundToInt

data class MyNumberUiModel(
    val firstPurchaseDate: String = "2024.01.01",
    val totalCount: Int = 0,
    val winCount: Int = 0,
    val loseCount: Int = 0,
    val myNumberDetails: List<MyNumberDetailUiModel> = emptyList(),
) {
    fun getWinRate(): String {
        val rate = ((winCount.toDouble() / winCount.plus(loseCount)) * 100).roundToInt()
        return String.format(locale = Locale.getDefault(), "%2d", rate).plus("%")
    }
}

data class MyNumberDetailUiModel(
    val type: LottoType,
    val round: Int,
    val date: String = "2024.01.01",
    val numberRows: List<MyNumberRowUiModel>,
)

data class MyNumberRowUiModel(
    val numbers: List<Int>,
    val isWin: Boolean,
    val condition: LottoCondition = LottoCondition.NOT_CHECKED,
)
