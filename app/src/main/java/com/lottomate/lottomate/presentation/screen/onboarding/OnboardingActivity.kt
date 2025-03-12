package com.lottomate.lottomate.presentation.screen.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.presentation.screen.main.MainActivity
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            LottoMateTheme {
                OnboardingRoute(
                    moveToHome = { moveToHome() },
                )
            }
        }
    }

    /**
     * 홈 화면으로 이동
     */
    private fun moveToHome() {
        val intent = Intent(this@OnboardingActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            lifecycleScope.launch {
                LottoMateDataStore.changeOnBoardingState()
            }.invokeOnCompletion { moveToHome() }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}