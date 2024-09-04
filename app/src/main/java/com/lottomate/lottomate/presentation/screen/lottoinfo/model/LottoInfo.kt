package com.lottomate.lottomate.presentation.screen.lottoinfo.model

sealed interface LottoInfo {
    val lottoRound: Int?
    val lottoDate: String?
}

sealed interface LottoInfoWithBalls : LottoInfo {
    val lottoNum: List<Int>
    val lottoBonusNum: List<Int>
    val lottoWinnerNum: List<String>
    override val lottoRound: Int
    override val lottoDate: String
}

data class Lotto645Info(
    val lottoPrize: List<String>,
    val totalSalesPrice: String,
    override val lottoRound: Int,
    override val lottoDate: String,
    override val lottoNum: List<Int>,
    override val lottoBonusNum: List<Int>,
    override val lottoWinnerNum: List<String>,
): LottoInfoWithBalls

data class Lotto720Info(
    override val lottoRound: Int,
    override val lottoDate: String,
    override val lottoNum: List<Int>,
    override val lottoBonusNum: List<Int>,
    override val lottoWinnerNum: List<String>
): LottoInfoWithBalls