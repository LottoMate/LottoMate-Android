package com.lottomate.lottomate.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {
    const val LOCATION_REQUEST_CODE = 111

    /**
     * 권한을 가지고 있는지 확인
     */
    fun hasPermissions(
        context: Context,
        permissions: List<String>,
    ): Boolean {
        var hasPermission = false

        permissions.forEach {  permission ->
            hasPermission = when (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED) {
                true -> true
                false -> false
            }
        }

        return hasPermission
    }

    /**
     * 권한 요청
     */
    fun requestPermissions(
        context: Activity,
        permissions: List<String>,
        requestCode: Int = 1,
    ) {
        ActivityCompat.requestPermissions(
            context,
            permissions.toTypedArray(),
            requestCode,
        )
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