package com.lottomate.lottomate.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()
        LocationManager.updateLocation(this@MainActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setHideSoftKey()

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            LottoMateTheme {
                MainScreen(
                    navigator = navigator,
                )
            }
        }
    }

    /**
     * 하단 소프트키 보이지 않도록 설정합니다.
     */
    private fun setHideSoftKey() {
        window.decorView.apply {
            systemUiVisibility = systemUiVisibility xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            systemUiVisibility = systemUiVisibility xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    /**
     * 위치 권한을 체크합니다.
     */
    private fun checkPermissions() {
        PermissionManager.requestLocation(this@MainActivity)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            val hasPermissions = grantResults.all { true }

            if (!hasPermissions) {
                Toast.makeText(this@MainActivity, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "위치 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                LocationManager.updateLocation(this@MainActivity)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}