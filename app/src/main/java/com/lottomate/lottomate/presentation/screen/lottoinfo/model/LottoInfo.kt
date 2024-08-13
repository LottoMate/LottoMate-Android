package com.lottomate.lottomate.presentation.screen.lottoinfo.model

sealed interface LottoInfo {
    val lottoRndNum: Int
    val drwtDate: String
    val drwtWinNum: List<String>
    val drwtNum: List<Int>
    val drwtBonusNum: List<Int>
}

data class Lotto645Info(
    override val lottoRndNum: Int,
    override val drwtDate: String,
    override val drwtWinNum: List<String>,
    override val drwtNum: List<Int>,
    override val drwtBonusNum: List<Int>,
    val prizeMoney: List<String>,
    val drwtMoney: List<String>,
    val drwtSaleMoney: Long = 0L,
): LottoInfo

data class Lotto720Info(
    override val lottoRndNum: Int,
    override val drwtDate: String,
    override val drwtWinNum: List<String>,
    override val drwtNum: List<Int>,
    override val drwtBonusNum: List<Int>,
): LottoInfo