package com.lottomate.lottomate.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val INTRO_DISPLAY_DELAY = 500L
    private var isShowingSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition { isShowingSplash }
        lifecycleScope.launch{
            delay(INTRO_DISPLAY_DELAY)
            isShowingSplash = false
        }

        enableEdgeToEdge()
        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            LottoMateTheme {
                MainScreen(
                    navigator = navigator,
                )
            }
        }

        setHideSoftKey()
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
}