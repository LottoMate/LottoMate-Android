package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.LotteryResult
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.presentation.screen.scanResult.model.ScanResultUiModel

fun LotteryResult.toUiModel(type: LottoType, myLottoInfo: MyLottoInfo) = ScanResultUiModel(
    type = type,
    myLotto = myLottoInfo,
    myWinningNumbers = this.winningNumbers,
    isWinner = this.isWinner,
    winningRanksByType = this.winningRanks,
    winningInfoByType = this.winningInfo,
    isClaimPeriodExpired = this.isClaimPeriodExpired,
)