package com.lottomate.lottomate.presentation.screen.scanResult.contract

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryResultFrom
import com.lottomate.lottomate.presentation.screen.scanResult.model.ScanResultUiModel

sealed interface LottoScanResultUiState {
    data object Loading : LottoScanResultUiState
    // 당첨되어 결과를 기다릴때 보여주는 로딩 화면
    data object CelebrationLoading : LottoScanResultUiState
    data class NotYet(val type: LottoType, val days: Int) : LottoScanResultUiState
    data object NotWinner : LottoScanResultUiState
    data class Success(
        val from: LotteryResultFrom,
        val data: ScanResultUiModel,
    ) : LottoScanResultUiState
    data object Expired : LottoScanResultUiState
}

sealed interface LotteryResultEffect {
    data class ShowSaveSuccessSnackBar(val message: String) : LotteryResultEffect
    data object NavigatePocket : LotteryResultEffect
}