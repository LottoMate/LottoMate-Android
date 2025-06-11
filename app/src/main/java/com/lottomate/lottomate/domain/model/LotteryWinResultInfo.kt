package com.lottomate.lottomate.domain.model

sealed interface WinResultInfo
data class Lotto645ResultInfo(
    val firstPrize: String,
    val secondPrize: String,
    val thirdPrize: String,
    val fourthPrize: String = "50000",
    val fifthPrize: String = "5000",
) : WinResultInfo

data class Lotto720ResultInfo(
    val firstPrize: String,
    val secondPrize: String,
    val thirdPrize: String,
    val fourthPrize: String,
    val fifthPrize: String,
    val sixthPrize: String,
    val seventhPrize: String,
) : WinResultInfo