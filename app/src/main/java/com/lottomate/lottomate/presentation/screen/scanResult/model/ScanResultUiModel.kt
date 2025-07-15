package com.lottomate.lottomate.presentation.screen.scanResult.model

import android.os.Parcelable
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.LottoRank
import com.lottomate.lottomate.domain.model.WinResultInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class ScanResultUiModel(
    val type: LottoType,
    val myLotto: MyLottoInfo,
    // 당첨된 번호
    val myWinningNumbers: List<List<Int>>,
    // 당첨 여부
    val isWinner: Boolean = false,
    // 당첨 내역 (로또 타입, 당첨 등수)
    val winningRanksByType: List<LottoRank> = emptyList(),
    // 당첨 결과 정보
    val winningInfoByType: WinResultInfo,
    // 지급 기한 유효 여부
    val isClaimPeriodExpired: Boolean = false,
)

@Serializable
@Parcelize
data class MyLottoInfo(
    val myLotto645Info: MyLotto645Info? = null,
    val myLotto720Info: MyLotto720Info? = null,
) : Parcelable

@Serializable
@Parcelize
data class MyLotto645Info(
    val round: Int,
    val numbers: List<List<Int>>,
) : Parcelable

@Serializable
@Parcelize
data class MyLotto720Info(
    val round: Int,
    val numbers: List<MyLotto720InfoNumbers>,
) : Parcelable

@Serializable
@Parcelize
data class MyLotto720InfoNumbers(
    val numbers: List<Int>,
    val firstNumber: Int = 0,
) : Parcelable