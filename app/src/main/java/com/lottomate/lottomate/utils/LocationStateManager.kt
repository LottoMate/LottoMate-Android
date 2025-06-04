package com.lottomate.lottomate.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object LocationStateManager {
    private lateinit var context: Context

    private var _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> get() = _currentLocation.asStateFlow()

    private var _shouldShowLocationSettingDialog = MutableStateFlow(false)
    val shouldShowLocationSettingDialog: StateFlow<Boolean> get() = _shouldShowLocationSettingDialog.asStateFlow()

    fun updateLocation() {
        try {
            // 위치 데이터를 사용할 수 있는지 여부 확인
            if (!isLocationSettingEnabled()) {
                setShouldShowLocationSettingDialog(true)
                return
            }

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
                        _currentLocation.update { LatLng(location.latitude, location.longitude) }
                    } catch (exception: Exception) {
                        setDefaultLatLng()
                    }
                }
                .addOnFailureListener {
                    setDefaultLatLng()

                    Log.d("LocationStateManager", "사용자 GPS Update 실패 : ${it.stackTraceToString()}")
                }
        } catch (exception: Exception) {
            setDefaultLatLng()

            Log.d("LocationStateManager", "사용자 GPS Update 실패 : ${exception.stackTraceToString()}")
        }
    }

    /**
     * 위치 권한이 부여되었는지 확인하는 함수
     *
     * @param context 애플리케이션의 Context
     * @return 위치 권한이 부여되었으면 true, 그렇지 않으면 false
     */
    fun hasLocationPermission(): Boolean {
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

    fun setShouldShowLocationSettingDialog(state: Boolean) {
        _shouldShowLocationSettingDialog.update { state }
    }

    fun init(context: Context) {
        this.context = context
    }

    /**
     * 설정 > 위치 ON/OFF 를 확인하는 함수
     *
     * 위치 권한이 허용되어있지만, OS 설정에서 위치 기능을 꺼놓았다면 false, 켜져있다면 true
     */
    private fun isLocationSettingEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)

        return isGpsEnabled || isNetworkEnabled
    }

    private fun setDefaultLatLng() {
        _currentLocation.update { null }
    }
}