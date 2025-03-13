package com.lottomate.lottomate.presentation.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.delay

@Composable
fun IntroRoute(
    moveToHome: () -> Unit,
    moveToOnboarding: () -> Unit,
) {
    val onboardingState by LottoMateDataStore.onBoardingFlow.collectAsState(initial = null)
    var showIntro by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // 추후 변경 예정 (확정 X)
        delay(3_000L)
        showIntro = false
    }

    LaunchedEffect(showIntro, onboardingState) {
        if (!showIntro) {
            onboardingState?.let { isOnboardingCompleted ->
                when (isOnboardingCompleted) {
                    true -> moveToHome()
                    false -> moveToOnboarding()
                }
            }
        }
    }

    if (showIntro) IntroScreen()
}

@Composable
private fun IntroScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoMateText(
                text = "행운을 불러오는",
                style = LottoMateTheme.typography.title3
                    .copy(color = LottoMateGray120),
            )

            LottoMateText(
                text = "LottoMate",
                style = LottoMateTheme.typography.title1
                    .copy(color = LottoMateGray120),
                modifier = Modifier.padding(top = 2.dp)
            )

            Image(bitmap = ImageBitmap.imageResource(id = R.drawable.img_login_2), contentDescription = null,
                modifier = Modifier
                    .padding(top = 36.dp)
                    .size(180.dp))

        }
    }
}