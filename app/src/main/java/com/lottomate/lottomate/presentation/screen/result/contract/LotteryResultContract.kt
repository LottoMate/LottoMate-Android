package com.lottomate.lottomate.presentation.screen.result.contract

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.screen.result.model.LotteryResultFrom
import com.lottomate.lottomate.presentation.screen.result.model.ScanResultUiModel

sealed interface LotteryResultUiState {
    data object Loading : LotteryResultUiState
    // 당첨되어 결과를 기다릴때 보여주는 로딩 화면
    data object CelebrationLoading : LotteryResultUiState
    data class NotYet(val type: LottoType, val days: Int) : LotteryResultUiState
    data object NotWinner : LotteryResultUiState
    data class Success(
        val from: LotteryResultFrom,
        val data: ScanResultUiModel,
    ) : LotteryResultUiState
    data object Expired : LotteryResultUiState
}

sealed interface LotteryResultEffect {
    data class ShowSaveSuccessSnackBar(val message: String) : LotteryResultEffect
    data object NavigatePocket : LotteryResultEffect
}