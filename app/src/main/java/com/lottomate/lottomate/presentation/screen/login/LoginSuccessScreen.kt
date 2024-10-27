package com.lottomate.lottomate.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun LoginSuccessRoute(
    padding: PaddingValues,
    moveToHome: () -> Unit,
    moveToMap: () -> Unit,
    onErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    LoginSuccessScreen(
        padding = padding,
        moveToHome = moveToHome,
        moveToMap = moveToMap,
    )
}

@Composable
private fun LoginSuccessScreen(
    padding: PaddingValues,
    moveToHome: () -> Unit,
    moveToMap: () -> Unit,
) {
    val bottomPadding = if (padding.calculateBottomPadding() < 36.dp) {
        36.dp.minus(padding.calculateBottomPadding()).plus(padding.calculateBottomPadding())
    } else padding.calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoMateText(
                text = stringResource(id = R.string.login_title_success),
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.title2,
            )

            Spacer(modifier = Modifier.height(2.dp))

            LottoMateText(
                text = stringResource(id = R.string.login_title_sub_success),
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.body1
                    .copy(color = LottoMateGray100),
            )

            Spacer(modifier = Modifier.height(57.dp))

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_login_2),
                contentDescription = stringResource(id = R.string.desc_login_success_image),
            )

            Spacer(modifier = Modifier.height(97.dp))

            BannerCard(
                modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
                onClickBanner = moveToMap,
            )

            Spacer(modifier = Modifier.height(28.dp))
            
            LottoMateSolidButton(
                text = stringResource(id = R.string.login_btn_start), 
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                onClick = moveToHome,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = Dimens.DefaultPadding20)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginSuccessScreenPreview() {
    LottoMateTheme {
        LoginSuccessScreen(
            padding = PaddingValues(0.dp),
            moveToHome = {},
            moveToMap = {}
        )
    }
}