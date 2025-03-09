package com.lottomate.lottomate.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {
    private lateinit var requestPermissions: List<String>

    const val LOCATION_REQUEST_CODE = 111

    fun requestLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST_CODE
            )
        }
    }

    /**
     * 권한을 가지고 있는지 확인
     */
    fun hasPermission(
        permissionTypes: List<PermissionType>,
        context: Context
    ): Boolean {
        val permissions = permissionTypes.flatMap { it.permissions }
        requestPermissions = permissions

        return permissions.all { permission -> ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED }
    }

    /**
     * 권한 요청
     */
    fun requestPermissions(
        permissionLauncher: ActivityResultLauncher<Array<String>>
    ) {
        permissionLauncher.launch(requestPermissions.toTypedArray())
    }

    /**
     * 권한 요청하는 다이얼로그(런쳐) 호출
     */
    @Composable
    fun rememberPermissionLauncher(
        onLocationPermissionsResult: (Boolean) -> Unit,
    ): ActivityResultLauncher<Array<String>> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val resultPermissions = requestPermissions.all { permissions[it] == true }

            onLocationPermissionsResult(resultPermissions)
        }
    }
}

sealed interface PermissionType {
    val permissions: List<String>

    data object CameraPermission : PermissionType {
        override val permissions: List<String>
            get() = listOf(Manifest.permission.CAMERA)
    }

    /**
     * API 33 이상인 경우, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION 권한을 요청
     */
    data object LocationPermissionAPI33 : PermissionType {
        override val permissions: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
    }

    /**
     * API 33 미만인 경우, ACCESS_FINE_LOCATION 권한을 요청
     */
    data object LocationPermission : PermissionType {
        override val permissions: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}