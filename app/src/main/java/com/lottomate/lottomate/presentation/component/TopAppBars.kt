package com.lottomate.lottomate.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.pixelsToDp
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

private val topAppBarHorizontalPadding = 12.dp

@Composable
fun LottoMateTopAppBar(
    @StringRes titleRes: Int,
    hasNavigation: Boolean,
    onBackPressed: () -> Unit = {},
    actionButtons: @Composable () -> Unit = {},
) {
    val statusHeight = LocalContext.current.resources.getDimensionPixelSize(
        LocalContext.current.resources.getIdentifier("status_bar_height", "dimen", "android")
    ).run {
        pixelsToDp(pixels = this)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(LottoMateWhite.copy(alpha = 0.8f)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusHeight)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.TopAppBarHeight),
        ) {
            if (hasNavigation) {
                LottoMateIconButton(
                    iconRes = R.drawable.icon_arrow_left,
                    contentDescription = "Navigation Back Button",
                    onClick = onBackPressed,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = topAppBarHorizontalPadding)
                )
            }

            LottoMateText(
                text = stringResource(id = titleRes),
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.headline1,
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = topAppBarHorizontalPadding),
            ) {
                actionButtons()
            }
        }
    }
}

@Composable
fun LottoMateIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = contentDescription,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
private fun LottoMateTopBarPreview() {
    LottoMateTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            LottoMateTopAppBar(
                titleRes = R.string.top_app_bar_title_lotto_info,
                hasNavigation = false,
            )

            LottoMateTopAppBar(
                titleRes = R.string.top_app_bar_title_lotto_info,
                hasNavigation = true
            )

            LottoMateTopAppBar(
                titleRes = R.string.top_app_bar_title_lotto_info,
                hasNavigation = false,
                actionButtons = {
                    LottoMateIconButton(
                        iconRes = R.drawable.icon_home,
                        contentDescription = "actionButton",
                        onClick = {}
                    )
                }
            )

            LottoMateTopAppBar(
                titleRes = R.string.top_app_bar_title_lotto_info,
                hasNavigation = true,
                actionButtons = {
                    LottoMateIconButton(
                        iconRes = R.drawable.icon_home,
                        contentDescription = "actionButton",
                        onClick = {}
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    LottoMateIconButton(
                        iconRes = R.drawable.icon_mypage,
                        contentDescription = "actionButton",
                        onClick = {}
                    )
                }
            )
        }
    }
}