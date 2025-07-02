package com.lottomate.lottomate.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun LoginSuccessRoute(
    vm: LoginCompleteViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToHome: () -> Unit,
    moveToInterviewDetail: (Int, String) -> Unit,
    onErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    LoginSuccessScreen(
        padding = padding,
        moveToHome = moveToHome,
        moveToInterviewDetail = {
            if (vm.interviews.isNotEmpty()) {
                val latestInterview = vm.interviews.first()
                moveToInterviewDetail(latestInterview.no, latestInterview.place)
            }
        },
    )
}

@Composable
private fun LoginSuccessScreen(
    padding: PaddingValues,
    moveToHome: () -> Unit,
    moveToInterviewDetail: () -> Unit,
) {
    val bottomPadding = if (padding.calculateBottomPadding() < 36.dp) {
        36.dp.minus(padding.calculateBottomPadding()).plus(padding.calculateBottomPadding())
    } else padding.calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding.calculateTopPadding())
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottoMateText(
                text = stringResource(id = R.string.login_title_success),
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.title1
                    .copy(color = LottoMateBlack),
            )

            LottoMateText(
                text = stringResource(id = R.string.login_title_sub_success),
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.body1
                    .copy(color = LottoMateGray100),
                modifier = Modifier.padding(top = 4.dp),
            )

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_login_2),
                contentDescription = stringResource(id = R.string.desc_login_success_image),
                modifier = Modifier
                    .padding(top = 47.dp)
                    .size(236.dp),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BannerCard(
                modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
                type = BannerType.INTERVIEW,
                onClickBanner = { moveToInterviewDetail() },
            )
            
            LottoMateSolidButton(
                text = stringResource(id = R.string.login_btn_start), 
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                onClick = moveToHome,
                modifier = Modifier
                    .fillMaxWidth()
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
            moveToInterviewDetail = {},
        )
    }
}