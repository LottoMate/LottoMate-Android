package com.lottomate.lottomate.presentation.screen.lottoinfo.model

/**
 * 최신 로또 회차/당첨일 정보
 */
data class LatestRoundInfo(
    val round: Int,
    val drawDate: String,
) {
    companion object {
        val EMPTY = LatestRoundInfo(0, "")
    }
}