package com.lottomate.lottomate.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object LocationStateManager {
    private lateinit var context: Context

    private var latitude by mutableStateOf<Double?>(null)
    private var longitude by mutableStateOf<Double?>(null)

    private var _shouldRequestEnableLocation = MutableStateFlow(false)
    val shouldRequestEnableLocation: StateFlow<Boolean> get() = _shouldRequestEnableLocation.asStateFlow()

    fun getCurrentLocation(): Pair<Double, Double>? {
        if (latitude == null || longitude == null) {
            updateLocation()
            return null
        } else {
            return Pair(latitude!!, longitude!!)
        }
    }

    fun hasGpsLocation(): Boolean = latitude != null && longitude != null

    fun updateLocation() {
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
                        when (isLocationSettingEnabled()) {
                            true -> {
                                _shouldRequestEnableLocation.update { true }
                                setDefaultLatLng()

                                Log.d("LocationStateManager(onSuccess)", "사용자 GPS Update 필요")
                            }
                            false -> {
                                latitude = location.latitude
                                longitude = location.longitude

                                Log.d("LocationStateManager", "사용자 GPS Update 성공 : ${location.latitude} / ${location.longitude}")
                            }
                        }
                    } catch (exception: Exception) {
                        Log.d("LocationStateManager(onSuccess-catch)", "사용자 GPS Update 필요")

                        setDefaultLatLng()
                    }
                }
                .addOnFailureListener {
                    Log.d("LocationStateManager", "사용자 GPS Update 실패 : ${it.stackTraceToString()}")
                }
        } catch (exception: Exception) {
            Log.d("LocationStateManager", "사용자 GPS Update 실패 : ${exception.stackTraceToString()}")
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

    fun setRequestEnableLocationState() {
        _shouldRequestEnableLocation.update { false }
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

        return !isGpsEnabled && !isNetworkEnabled
    }

    private fun setDefaultLatLng() {
        latitude = null
        longitude = null
    }
}