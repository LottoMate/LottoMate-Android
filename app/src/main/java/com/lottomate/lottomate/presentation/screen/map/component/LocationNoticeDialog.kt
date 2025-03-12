package com.lottomate.lottomate.presentation.screen.map.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.lottomate.lottomate.presentation.component.LottoMateDialog

@Composable
internal fun LocationNoticeDialog(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    LottoMateDialog(
        title = """
                내 위치를 보려면
                앱 설정 > 위치 정보 사용을 허용해야
                확인할 수 있어요
            """.trimIndent(),
        cancelText = "사용 안 함",
        confirmText = "설정으로 이동",
        onConfirm = {
            onDismiss()

            val openSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }

            context.startActivity(openSettings)
        },
        onDismiss = onDismiss
    )
}

/**
 * 지도 진입 시, 위치 권한이 없을 때 보여지는 다이얼로그
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun checkLocationPermission(
    onChangeState: (Boolean) -> Unit,
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // 권한이 있으면 다이얼로그를 띄우지 않음
    LaunchedEffect(locationPermissionState.permissions) {
        onChangeState(locationPermissionState.permissions.all { it.status.isGranted })
    }
}

/**
 * Activity의 라이프사이클을 관찰하며, 라이프사이클에 맞추어 권한 여부를 확인합니다.
 *
 * `ON_START` : 권한이 허용되었다면 GPS를 업데이트합니다.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun LocationPermissionObserver(
    onStartLocationPermissionGranted: () -> Unit,
    onStartLocationPermissionDenied: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle

    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    val observer = rememberUpdatedState { event: Lifecycle.Event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                if (locationPermissionState.permissions.all { it.status.isGranted }) {
                    Log.d("MapScreen", "위치 권한 허용됨. GPS 가져오기")
                    onStartLocationPermissionGranted()
                } else {
                    onStartLocationPermissionDenied()
                }
            }
            Lifecycle.Event.ON_STOP -> {
                Log.d("MapScreen", "라이프사이클 : Stopped")
            }
            else -> Unit
        }
    }

    DisposableEffect(lifecycle) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            observer.value(event)
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}