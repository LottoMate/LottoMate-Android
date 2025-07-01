package com.lottomate.lottomate.presentation.screen.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.domain.model.LoginType
import com.lottomate.lottomate.presentation.component.LoginIconButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTooltip
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.component.ToolTipDirection
import com.lottomate.lottomate.presentation.model.LoginTypeUiModel
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.presentation.ui.poppinsStyle
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@Composable
fun LoginRoute(
    vm: LoginViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLoginSuccess: () -> Unit,
    onCloseClicked: () -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    val context = LocalContext.current
    val latestLoginType by vm.latestLoginType

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            if (effect) { moveToLoginSuccess() }
        }
    }

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest { error -> onShowErrorSnackBar(error) }
    }

    LoginScreen(
        padding = padding,
        latestLoginType = latestLoginType,
        moveToLoginSuccess = moveToLoginSuccess,
        onClickClose = onCloseClicked,
        onClickLogin = { type ->
            when (type) {
                LoginTypeUiModel.EMAIL -> vm.loginWithEmail()
                else -> {
                    // TODO : SNS 로그인 연결 or 제거
                }
            }
        },
    )
}

@Composable
private fun LoginScreen(
    padding: PaddingValues,
    latestLoginType: LoginTypeUiModel?,
    moveToLoginSuccess: () -> Unit,
    onClickClose: () -> Unit,
    onClickLogin: (LoginTypeUiModel) -> Unit,
) {
    val density = LocalDensity.current

    var loginIconOffset by remember { mutableStateOf(Offset.Zero) }
    var loginIconSize by remember { mutableStateOf(IntSize.Zero) }
    var latestLoginToolTipSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoMateText(
                text = stringResource(id = R.string.login_title),
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.title2
                    .copy(color = LottoMateBlack),
            )

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_login_1),
                contentDescription = null,
                modifier = Modifier.padding(top = 44.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LottoMateText(
                    text = stringResource(id = R.string.login_text_bottom),
                    textAlign = TextAlign.Center,
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray100),
                )

                Spacer(modifier = Modifier.height(20.dp))

                LoginIconButtons(
                    latestLoginType = latestLoginType,
                    onPositioned = { coordinates ->
                        val position = coordinates.positionInRoot()
                        loginIconOffset = position
                        loginIconSize = coordinates.size
                    },
                    onClickLogin = onClickLogin,
                )
            }
        }

        latestLoginType?.let {
            LottoMateTooltip(
                text = "최근 로그인했어요",
                direction = ToolTipDirection.TOP,
                modifier = Modifier
                    .onGloballyPositioned { latestLoginToolTipSize = it.size }
                    .offset {
                        val margin = with (density) { 12.dp.toPx() }

                        IntOffset(
                            x = ((loginIconOffset.x + loginIconSize.width/2f) - latestLoginToolTipSize.width/2f).roundToInt(),
                            y = (loginIconOffset.y + loginIconSize.height + margin).roundToInt()
                        )
                    }
            )
        }

        LottoMateTopAppBar(
            titleRes = R.string.top_app_bar_empty_title,
            hasNavigation = false,
            isTitleCenter = false,
            actionButtons = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = stringResource(id = R.string.desc_setting_icon),
                    tint = LottoMateGray100,
                    modifier = Modifier.noInteractionClickable { onClickClose() }
                )
            }
        )
    }
}

@Composable
private fun LoginIconButtons(
    latestLoginType: LoginTypeUiModel?,
    onPositioned: (LayoutCoordinates) -> Unit,
    onClickLogin: (LoginTypeUiModel) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
    ) {
        LoginTypeUiModel.entries.forEach { loginTypeUiModel ->
            LoginIconButton(
                iconRes = loginTypeUiModel.iconRes,
                descRes = loginTypeUiModel.descRes,
                type = loginTypeUiModel,
                latest = latestLoginType,
                onPositioned = onPositioned,
                onClick = { onClickLogin(loginTypeUiModel) },
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LottoMateTheme {
        LoginScreen(
            padding = PaddingValues(0.dp),
            latestLoginType = LoginTypeUiModel.EMAIL,
            onClickLogin = {},
            moveToLoginSuccess = {},
            onClickClose = {},
        )
    }
}