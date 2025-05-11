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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun IntroRoute(
    moveToHome: () -> Unit,
    moveToOnboarding: () -> Unit,
) {
    val onboardingState by LottoMateDataStore.onBoardingFlow
        .filterNotNull()
        .collectAsState(initial = false)

    var currentSplashIndex by remember { mutableIntStateOf(0) }

    val splashRes = arrayOf(
        R.drawable.img_splash01,
        R.drawable.img_splash02,
        R.drawable.img_splash03,
        R.drawable.img_splash04,
        R.drawable.img_splash01,
        R.drawable.img_splash02,
        R.drawable.img_splash03,
        R.drawable.img_splash04,
    )

    LaunchedEffect(Unit) {
        splashRes.forEachIndexed { index, _ ->
            delay(350L)
            currentSplashIndex = index
        }

        if (onboardingState) moveToHome() else moveToOnboarding()
    }

    IntroScreen(currentSplashIndex, splashRes)
}

@Composable
private fun IntroScreen(
    currentSplashIndex: Int,
    splashRes: Array<Int>,
) {
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
                text = stringResource(id = R.string.intro_title),
                style = LottoMateTheme.typography.headline1
                    .copy(color = LottoMateGray120),
            )

            Image(
                painter = painterResource(id = splashRes[currentSplashIndex]),
                contentDescription = "splash image",
                modifier = Modifier.padding(top = 13.dp),
            )

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_onboarding05),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 34.dp)
                    .size(234.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IntroScreenPreview() {
    LottoMateTheme {
        IntroRoute(
            moveToHome = {},
            moveToOnboarding = {},
        )
    }
}