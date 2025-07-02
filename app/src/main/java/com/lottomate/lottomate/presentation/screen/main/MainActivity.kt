package com.lottomate.lottomate.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lottomate.lottomate.presentation.component.LottoMateBackHandler
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

                LottoMateBackHandler()
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
}