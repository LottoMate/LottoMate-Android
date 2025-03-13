package com.lottomate.lottomate.presentation.screen.scan

import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun LottoScanRoute(
    vm: LottoScanViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLottoScanResult: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val barcodeScannerLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        Log.d("LottoScanScreen", "LottoScanScreen: ${result}")
        if (result.contents == null) {
            onBackPressed()
            return@rememberLauncherForActivityResult
        }
        moveToLottoScanResult(result.contents)
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