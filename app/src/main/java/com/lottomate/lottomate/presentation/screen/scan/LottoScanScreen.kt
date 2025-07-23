package com.lottomate.lottomate.presentation.screen.scan

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.lottomate.lottomate.presentation.screen.result.model.LotteryInputType
import com.lottomate.lottomate.presentation.screen.result.model.MyLottoInfo

@Composable
fun LottoScanRoute(
    vm: LottoScanViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLotteryResult: (LotteryInputType, MyLottoInfo) -> Unit,
    onBackPressed: () -> Unit,
) {
    val barcodeScannerLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if (result.contents == null) {
            onBackPressed()
            return@rememberLauncherForActivityResult
        }

        val (type, myLotto) = vm.parseMyLotto(result.contents.substringAfter("?v="))
        moveToLotteryResult(type, myLotto)
    }

    LottoScanScreen(
        barcodeScannerLauncher = barcodeScannerLauncher,
    )
}

@Composable
private fun LottoScanScreen(
    barcodeScannerLauncher: ManagedActivityResultLauncher<ScanOptions, ScanIntentResult>
) {
    LaunchedEffect(Unit) {
        val scanOption = ScanOptions().apply {
            setOrientationLocked(false)
            setCameraId(0)
            setCaptureActivity(LottoScanActivity::class.java)
        }

        barcodeScannerLauncher.launch(scanOption)
    }
}