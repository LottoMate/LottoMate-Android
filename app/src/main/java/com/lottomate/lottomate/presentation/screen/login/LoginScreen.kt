package com.lottomate.lottomate.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun LoginRoute(
    vm: LoginViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLoginSuccess: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val latestLoginType by vm.latestLoginType

    LoginScreen(
        padding = padding,
        latestLoginType = latestLoginType,
        moveToLoginSuccess = moveToLoginSuccess,
        onClickKakaoLogin = {},
        onClickNaverLogin = {},
        onClickGoogleLogin = {},
    )
}

@Composable
private fun LoginScreen(
    padding: PaddingValues,
    latestLoginType: LatestLoginType,
    moveToLoginSuccess: () -> Unit,
    onClickKakaoLogin: () -> Unit,
    onClickNaverLogin: () -> Unit,
    onClickGoogleLogin: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
        contentAlignment = Alignment.BottomCenter,
    ) {
        LottoMateText(
            text = "로그인 완료",
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopEnd)
                .clickable { moveToLoginSuccess() }
        )
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 104.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoMateText(
                text = stringResource(id = R.string.login_title),
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.title2,
            )

            Spacer(modifier = Modifier.height(116.dp))

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_login_1),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(78.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LottoMateText(
                    text = stringResource(id = R.string.login_text_bottom),
                    textAlign = TextAlign.Center,
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray100),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_kakao),
                        contentDescription = stringResource(id = R.string.desc_login_kakao_icon),
                        modifier = Modifier.noInteractionClickable { onClickKakaoLogin },
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_naver),
                        contentDescription = stringResource(id = R.string.desc_login_naver_icon),
                        modifier = Modifier.noInteractionClickable { onClickNaverLogin },
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_google),
                        contentDescription = stringResource(id = R.string.desc_login_google_icon),
                        modifier = Modifier.noInteractionClickable { onClickGoogleLogin },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LottoMateTheme {
        LoginScreen(
            padding = PaddingValues(0.dp),
            latestLoginType = LatestLoginType.GOOGLE,
            onClickKakaoLogin = {},
            onClickNaverLogin = {},
            onClickGoogleLogin = {},
            moveToLoginSuccess = {},
        )
    }
}