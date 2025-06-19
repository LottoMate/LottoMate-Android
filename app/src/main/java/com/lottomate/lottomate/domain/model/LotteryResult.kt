package com.lottomate.lottomate.domain.model

import com.lottomate.lottomate.data.model.LottoType

/**
 * QR 스캔 or 직접 번호 입력 후, 당첨 결과를 가진 entity
 *
 * @param lottoType 복권 타입
 * @param isWinner 당첨 여부
 * @param winningRanks 당첨 등수
 * @param winningInfo 복권 당첨 정보
 * @param isClaimPeriodExpired 지급기한 유효 여부
 */
data class LotteryResult(
    val lottoType: LottoType,
    val isWinner: Boolean,
    val winningRanks: List<LottoRank>,
    val winningNumbers: List<List<Int>>,
    val winningInfo: WinResultInfo,
    val isClaimPeriodExpired: Boolean,
)
