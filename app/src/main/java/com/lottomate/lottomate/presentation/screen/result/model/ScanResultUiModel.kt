package com.lottomate.lottomate.presentation.screen.result.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.LottoRank
import com.lottomate.lottomate.domain.model.WinResultInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Immutable
data class ScanResultUiModel(
    val myLotto: MyLottoInfo,
    val isWinner: Boolean,
    val resultRows: List<LotteryResultRowUiModel>,
)

@Immutable
data class LotteryResultRowUiModel(
    val type: LottoType,
    val round: Int,
    val isWinner: Boolean = false,
    val winningRank: LottoRank,
    val myWinningNumbers: List<Int>,
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