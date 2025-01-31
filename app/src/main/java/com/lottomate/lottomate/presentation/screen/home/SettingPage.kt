package com.lottomate.lottomate.presentation.screen.home

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.login.LoginViewModel
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
internal fun SettingPage(
    vm: LoginViewModel = hiltViewModel(),
    padding: PaddingValues,
    onBackPressed: () -> Unit,
) {
    val context = LocalContext.current
    val resultLoginWithGoogle = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

        if (task.isSuccessful) {
            Log.d("Google Login", task.result.toString())

            task.result.idToken?.let {  idToken ->
                task.result.serverAuthCode?.let { accessToken ->
                    vm.loginWithGoogle(idToken, accessToken)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding())
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier.padding(top = Dimens.BaseTopPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
            ) {
                LottoMateText(
                    text = "소셜 로그인 하기",
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray100),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_kakao),
                        contentDescription = null
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_naver),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 20.dp),
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_google),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .noInteractionClickable {
                                val gso = vm.loginWithGoogleClient(context)
                                resultLoginWithGoogle.launch(gso.signInIntent)
                            }
                        )
                }

                Divider(
                    color = LottoMateGray20,
                    thickness = 10.dp,
                    modifier = Modifier.padding(top = 28.dp),
                )

                Column(
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp)
                            .padding(start = 20.dp, end = 13.dp),
                    ) {
                        LottoMateText(
                            text = "내 계정 관리",
                            style = LottoMateTheme.typography.body1,
                            modifier = Modifier.weight(1f),
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_right),
                            contentDescription = null,
                            tint = LottoMateGray100
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp)
                            .padding(start = 20.dp, end = 13.dp),
                    ) {
                        LottoMateText(
                            text = "공지사항",
                            style = LottoMateTheme.typography.body1,
                            modifier = Modifier.weight(1f),
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_right),
                            contentDescription = null,
                            tint = LottoMateGray100
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp)
                            .padding(start = 20.dp, end = 13.dp),
                    ) {
                        LottoMateText(
                            text = "약관 및 정책",
                            style = LottoMateTheme.typography.body1,
                            modifier = Modifier.weight(1f),
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_right),
                            contentDescription = null,
                            tint = LottoMateGray100
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = Dimens.DefaultPadding20),
                    ) {
                        LottoMateText(
                            text = "버전 정보",
                            style = LottoMateTheme.typography.body1,
                        )

                        LottoMateText(
                            text = "1.08",
                            style = LottoMateTheme.typography.body1
                                .copy(color = LottoMateRed50),
                            modifier = Modifier.padding(start = Dimens.DefaultPadding20),
                        )
                    }
                }

                Box(
                    modifier = Modifier.weight(1f),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.DefaultPadding20)
                        .padding(bottom = 32.dp)
                        .background(color = LottoMateGray20, RoundedCornerShape(16.dp)),
                ) {
                    Row(
                        modifier = Modifier.padding(Dimens.DefaultPadding20),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.img_alert_notice),
                            contentDescription = null,
                        )

                        LottoMateText(
                            text = "로또메이트에 문의사항이 있다면\n여기로 보내주세요",
                            style = LottoMateTheme.typography.body1,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }
        }
        LottoMateTopAppBar(
            titleRes = R.string.setting_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingPagePreview() {
    LottoMateTheme {
        SettingPage(
            padding = PaddingValues(0.dp),
            onBackPressed = {},
        )
    }
}