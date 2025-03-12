package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateDim
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import kotlinx.coroutines.delay

private val LoadingBackgroundSize = 160.dp

/**
 * 지도 화면 첫 진입 시 보여지는 로딩 화면 입니다.
 */
@Composable
fun MapLoadingScreen(
    modifier: Modifier = Modifier,
) {
    var showLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5_000)
        showLoading = false
    }

    if (showLoading) {
        Box(
            modifier = modifier.background(LottoMateDim),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(LoadingBackgroundSize)
                        .dropShadow(
                            shape = CircleShape,
                            blur = 8.dp,
                            offsetY = 0.dp,
                            offsetX = 0.dp,
                        )
                        .clip(CircleShape)
                        .background(LottoMateWhite)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LottoMateText(
                    text = stringResource(id = R.string.map_loading_text),
                    style = LottoMateTheme.typography.title3
                        .copy(
                            color = LottoMateWhite,
                            shadow = Shadow(
                                color = LottoMateBlack.copy(0.16f),
                                blurRadius = 0.8f
                            )
                        ),
                )

                LottoMateText(
                    text = stringResource(id = R.string.map_loading_sub_text),
                    style = LottoMateTheme.typography.headline2
                        .copy(
                            color = LottoMateWhite,
                            shadow = Shadow(
                                color = LottoMateBlack.copy(0.16f),
                                blurRadius = 0.8f
                            )
                        ),
                )
            }
        }
    }
}