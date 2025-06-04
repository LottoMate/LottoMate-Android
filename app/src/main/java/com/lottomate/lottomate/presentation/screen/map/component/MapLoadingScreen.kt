package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.GifImageResourceType
import com.lottomate.lottomate.presentation.component.LottoMateGifImage
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow

private val LoadingBackgroundSize = 160.dp

/**
 * 지도 화면 첫 진입 시 보여지는 로딩 화면 입니다.
 */
@Preview
@Composable
fun MapLoadingScreen() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false,
        )
    ) {
        val dialogWindowProvider = LocalView.current.parent as? DialogWindowProvider
        dialogWindowProvider?.window?.setDimAmount(0.4f)

        Box(
            modifier = Modifier.fillMaxSize(),
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
                        .background(LottoMateWhite),
                    contentAlignment = Alignment.Center,
                ) {
                    LottoMateGifImage(
                        modifier = Modifier
                            .height(104.dp),
                        gifImageResourceType = GifImageResourceType.MAP_LOADING
                    )
                }

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