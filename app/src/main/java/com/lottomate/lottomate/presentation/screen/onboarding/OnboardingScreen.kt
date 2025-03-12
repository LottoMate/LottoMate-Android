package com.lottomate.lottomate.presentation.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.onboarding.model.OnboardingType
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun OnboardingRoute(
    moveToHome: () -> Unit,
) {
    val onboardingState by LottoMateDataStore.onBoardingFlow.collectAsState(initial = null)
    var showPermission by remember { mutableStateOf(false) }

    // TODO : 스플래시 화면 만들면 스플래시 화면에서 onboardingState 확인 후 넘어가도록 변경
    onboardingState?.let { isOnboardingCompleted ->
        when (isOnboardingCompleted) {
            true -> moveToHome()
            false -> {
                OnboardingScreen(
                    modifier = Modifier.padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                    ),
                    showPermission = { showPermission = true }
                )
            }
        }
    }

    if (showPermission) PermissionScreen()
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    showPermission: () -> Unit,
) {
    val onboardingTypes = OnboardingType.entries
    var currentOnboardingIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite)
            .padding(horizontal = Dimens.DefaultPadding20),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                        .plus(20.dp)
                )
                .fillMaxWidth()
        ) {
            onboardingTypes.forEachIndexed { index, _ ->
                Box(modifier = modifier
                    .weight(1f)
                    .padding(start = if (index != 0) 4.dp else 0.dp)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        color = if (index <= currentOnboardingIndex) LottoMateRed50 else LottoMateGray20,
                        shape = RoundedCornerShape(Dimens.RadiusExtraSmall)
                    )
                )
            }
        }

        onboardingTypes.find { it.ordinal == currentOnboardingIndex }?.let { type ->
            OnboardingContent(
                modifier = Modifier,
                title = type.title,
                img = type.img,
            )
        }

        LottoMateSolidButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp),
            text = if (onboardingTypes.lastIndex != currentOnboardingIndex) "다음" else "시작하기",
            buttonSize = LottoMateButtonProperty.Size.LARGE,
            onClick = {
                when (currentOnboardingIndex) {
                    in (0..< onboardingTypes.lastIndex) -> currentOnboardingIndex += 1
                    else -> showPermission()
                }
            }
        )
    }
}

@Composable
private fun OnboardingContent(
    modifier: Modifier = Modifier,
    title: String,
    img: Int,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottoMateText(
            text = title,
            style = LottoMateTheme.typography.title3,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Image(
            bitmap = ImageBitmap.imageResource(id = img),
            contentDescription = null,
            modifier = Modifier
                .size(234.dp),
        )
    }
}