package com.lottomate.lottomate.presentation.screen.home.model

/**
 * Home 사용되는 이번 주 당첨 결과 - 로또 645
 */
sealed interface HomeLottoInfo

data class HomeLotto645Info(
    val round: Int,
    val date: String,
    val winnerPrice: String,
    val winnerCount: String,
    val winnerNumbers: List<Int>,
    val winnerBonusNumber: Int,
) : HomeLottoInfo

/**
 * Home 사용되는 이번 주 당첨 결과 - 연금복권720
 */
data class HomeLotto720Info(
    val round: Int,
    val date: String,
    val winnerNumbers: List<Int>,
) : HomeLottoInfo
