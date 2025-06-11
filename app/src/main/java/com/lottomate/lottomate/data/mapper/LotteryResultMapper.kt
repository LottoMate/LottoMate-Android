package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.domain.model.LotteryResult
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.presentation.screen.scanResult.model.ScanResultUiModel

fun LotteryResult.toUiModel(myLottoInfo: MyLottoInfo) = ScanResultUiModel(
    myLotto = mapOf(this.lottoType to myLottoInfo),
    isWinner = this.isWinner,
    winningRanksByType = this.winningRanks,
    winningInfoByType = this.winningInfo,
    isClaimPeriodExpired = this.isClaimPeriodExpired,
)