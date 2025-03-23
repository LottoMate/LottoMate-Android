package com.lottomate.lottomate.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.lottomate.lottomate.data.error.LottoMateErrorType

object LocationManager {
    private var latitude by mutableDoubleStateOf(0.0)
    private var longitude by mutableDoubleStateOf(0.0)

    fun getCurrentLocation() = Pair(latitude, longitude)

    fun hasGpsLocation(): Boolean {
        return latitude != 0.0 && longitude != 0.0
    }

    fun updateLocation(context: Context) {
        try {
            val locationService = LocationServices.getFusedLocationProviderClient(context)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationService.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    try {
                        Log.d("LocationManager", "사용자 GPS Update 성공 : ${location.latitude} / ${location.longitude}")

                        latitude = location.latitude
                        longitude = location.longitude
                    } catch (exception: Exception) {
                        Log.d("LocationManager(onSuccess)", "사용자 GPS Update 필요")
                        latitude = 0.0
                        longitude = 0.0
                    }
                }
                .addOnFailureListener {
                    Log.d("LocationManager", "사용자 GPS Update 실패 : ${it.stackTraceToString()}")
                }
        } catch (exception: Exception) {
            Log.d("LocationManager", "사용자 GPS Update 실패 : ${exception.stackTraceToString()}")
        }
    }

    /**
     * 위치 권한이 부여되었는지 확인하는 함수
     *
     * @param context 애플리케이션의 Context
     * @return 위치 권한이 부여되었으면 true, 그렇지 않으면 false
     */
    fun hasLocationPermission(context: Context): Boolean {
        return when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> true
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> true
            else -> false
        }
    }
}