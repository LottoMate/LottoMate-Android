package com.lottomate.lottomate.presentation.screen.scanResult.model

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.LottoRank
import com.lottomate.lottomate.domain.model.WinResultInfo

data class ScanResultUiModel(
    val myLotto: Map<LottoType, MyLottoInfo> = emptyMap(),
    // 당첨 여부
    val isWinner: Boolean = false,
    // 당첨 내역 (로또 타입, 당첨 등수)
    val winningRanksByType: List<LottoRank> = emptyList(),
    // 당첨 결과 정보
    val winningInfoByType: WinResultInfo,
    // 지급 기한 유효 여부
    val isClaimPeriodExpired: Boolean = false,
)

sealed interface MyLottoInfo {
    val round: Int
}
data class MyLotto645Info(
    override val round: Int,
    val numbers: List<List<Int>>,
) : MyLottoInfo

data class MyLotto720Info(
    override val round: Int,
    val numbers: List<Int>,
    val firstNumber: Int,
) : MyLottoInfo