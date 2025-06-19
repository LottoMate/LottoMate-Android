package com.lottomate.lottomate.domain.model

sealed interface WinResultInfo {
    fun getPrize(rank: Int): String
}
data class Lotto645ResultInfo(
    val firstPrize: String,
    val secondPrize: String,
    val thirdPrize: String,
    val fourthPrize: String = "50000",
    val fifthPrize: String = "5000",
) : WinResultInfo {
    override fun getPrize(rank: Int): String {
        return when (rank) {
            1 -> firstPrize
            2 -> secondPrize
            3 -> thirdPrize
            4 -> fourthPrize
            else -> fifthPrize
        }
    }
}

data class Lotto720ResultInfo(
    val firstPrize: String,
    val secondPrize: String,
    val thirdPrize: String,
    val fourthPrize: String,
    val fifthPrize: String,
    val sixthPrize: String,
    val seventhPrize: String,
) : WinResultInfo {
    override fun getPrize(rank: Int): String {
        return when (rank) {
            1 -> firstPrize
            2 -> secondPrize
            3 -> thirdPrize
            4 -> fourthPrize
            5 -> fifthPrize
            6 -> sixthPrize
            else -> seventhPrize
        }
    }
}