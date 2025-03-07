package com.lottomate.lottomate.presentation.screen.map.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.lottomate.lottomate.presentation.component.LottoMateDialog

/**
 * 위치 권한이 없을 때 보여지는 다이얼로그
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun checkLocationPermission() {
    var hasLocationPermission by remember { mutableStateOf(false) }
    var showLocationNoticeDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionState.permissions.any { it.status.isGranted }) {
        hasLocationPermission = true
    } else {
        showLocationNoticeDialog = true
    }

    if (showLocationNoticeDialog) {
        LottoMateDialog(
            title = """
                내 위치를 보려면
                앱 설정 > 위치 정보 사용을 허용해야
                확인할 수 있어요
            """.trimIndent(),
            cancelText = "사용 안 함",
            confirmText = "설정으로 이동",
            onConfirm = {
                showLocationNoticeDialog = false

                val openSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }

                context.startActivity(openSettings)
            },
            onDismiss = { showLocationNoticeDialog = false }
        )
    }
}