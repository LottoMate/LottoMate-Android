package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.LotteryResult
import com.lottomate.lottomate.domain.model.LotteryResultInfo
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryResultRowUiModel
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.presentation.screen.scanResult.model.ScanResultUiModel

fun LotteryResult.toUiModel(type: LottoType, myLottoInfo: MyLottoInfo) = ScanResultUiModel(
    myLotto = myLottoInfo,
    isWinner = this.isWinner,
    resultRows = this.resultInfos
        .sortedBy { it.rank.rank }
        .map { it.toUiModel(type) },
)

fun LotteryResultInfo.toUiModel(type: LottoType) = LotteryResultRowUiModel(
    type = type,
    round = this.round,
    isWinner = this.isWinner,
    winningRank = this.rank,
    myWinningNumbers = this.winningNumbers,
    winningInfoByType = this.winningInfo,
    isClaimPeriodExpired = this.isClaimPeriodExpired,
)

fun LotteryResultRowUiModel.toDomain() = LotteryResultInfo(
    lottoType = this.type,
    round = this.round,
    isWinner = this.isWinner,
    rank = this.winningRank,
    winningNumbers = this.myWinningNumbers,
    winningInfo = this.winningInfoByType,
    isClaimPeriodExpired = this.isClaimPeriodExpired,
)