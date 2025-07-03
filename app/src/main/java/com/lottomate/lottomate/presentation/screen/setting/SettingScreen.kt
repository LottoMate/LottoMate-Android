package com.lottomate.lottomate.presentation.screen.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lottomate.lottomate.R
import com.lottomate.lottomate.domain.model.UserProfile
import com.lottomate.lottomate.presentation.component.LoginIconButton
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTooltip
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.component.ToolTipDirection
import com.lottomate.lottomate.presentation.model.LoginTypeUiModel
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.setting.editnickname.EditNickNameBottomSheet
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SettingRoute(
    vm: SettingViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToMyPage: () -> Unit,
    onBackPressed: () -> Unit,
) {
    val userProfile by vm.userProfile.collectAsState()
    val latestLoginType by vm.latestLoginType
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    SettingScreen(
        modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
        userProfile = userProfile,
        latestLoginType = latestLoginType,
        snackBarHostState = snackBarHostState,
        onClickMyPage = moveToMyPage,
        onClickLogin = { loginType ->
            when (loginType) {
                LoginTypeUiModel.EMAIL -> { vm.loginWithEmail() }
                else -> {}
            }
        },
        onBackPressed = onBackPressed,
        onChangeNickNameCompleted = {
            coroutineScope.launch {
                snackBarHostState.showSnackbar("닉네임을 변경했어요")
            }
        }
    )
}

@Composable
private fun SettingScreen(
    modifier: Modifier = Modifier,
    userProfile: UserProfile?,
    latestLoginType: LoginTypeUiModel?,
    snackBarHostState: SnackbarHostState,
    onClickMyPage: () -> Unit = {},
    onClickLogin: (LoginTypeUiModel) -> Unit,
    onChangeNickNameCompleted: () -> Unit,
    onBackPressed: () -> Unit,
) {
    val density = LocalDensity.current

    var loginIconOffset by remember { mutableStateOf(Offset.Zero) }
    var loginIconSize by remember { mutableStateOf(IntSize.Zero) }
    var latestLoginToolTipSize by remember { mutableStateOf(IntSize.Zero) }

    var showNickNameEditingBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier.padding(top = Dimens.BaseTopPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                userProfile?.let { profile ->
                    TopUserProfileSection(
                        userProfile = profile,
                        onClickNickNameEdit = {
                            showNickNameEditingBottomSheet = true
                        }
                    )
                } ?: run {
                    TopLoginSection(
                        latestLoginType = latestLoginType,
                        onGloballyPositioned = { coordinates ->
                            val position = coordinates.positionInRoot()

                            loginIconOffset = position
                            loginIconSize = coordinates.size
                        },
                        onClickLogin = onClickLogin,
                    )
                }

                Divider(
                    color = LottoMateGray20,
                    thickness = 10.dp,
                )

                Column(
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { onClickMyPage() }
                            .padding(vertical = 18.dp)
                            .padding(start = 20.dp, end = 13.dp)
                            .fillMaxWidth(),
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

        userProfile ?: run {
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
        }

        LottoMateTopAppBar(
            titleRes = R.string.setting_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )

        userProfile?.let { profile ->
            if (showNickNameEditingBottomSheet) {
                EditNickNameBottomSheet(
                    nickName = profile.nickname,
                    onDismiss = { showNickNameEditingBottomSheet = false },
                    onComplete = {
                        onChangeNickNameCompleted()
                        showNickNameEditingBottomSheet = false
                    },
                )
            }
        }

        snackBarHostState.currentSnackbarData?.let {
            LottoMateSnackBarHost(
                modifier = Modifier.align(Alignment.TopCenter),
                snackBarHostState = snackBarHostState
            ) {
                LottoMateSnackBar(
                    modifier = Modifier
                        .padding(top = Dimens.BaseTopPadding.plus(12.dp)),
                    message = it.visuals.message
                )
            }
        }
    }
}

@Composable
private fun TopLoginSection(
    modifier: Modifier = Modifier,
    latestLoginType: LoginTypeUiModel?,
    onGloballyPositioned: (LayoutCoordinates) -> Unit,
    onClickLogin: (LoginTypeUiModel) -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 28.dp),
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
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
        ) {
            LoginTypeUiModel.entries.forEach { loginTypeUiModel ->
                LoginIconButton(
                    iconRes = loginTypeUiModel.iconRes,
                    descRes = loginTypeUiModel.descRes,
                    type = loginTypeUiModel,
                    latest = latestLoginType,
                    onPositioned = onGloballyPositioned,
                    onClick = { onClickLogin(loginTypeUiModel) },
                )
            }
        }
    }
}

@Composable
private fun TopUserProfileSection(
    userProfile: UserProfile,
    onClickNickNameEdit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.DefaultPadding20)
            .padding(top = 36.dp, bottom = 20.dp),
    ) {
        LottoMateText(
            text = "안녕하세요",
            style = LottoMateTheme.typography.headline1
                .copy(color = LottoMateBlack),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LottoMateText(
                text = "${userProfile.nickname}님",
                style = LottoMateTheme.typography.headline1
                    .copy(color = LottoMateBlack),
            )
            
            Icon(
                painter = painterResource(R.drawable.icon_pen),
                contentDescription = "NickName Edit",
                tint = LottoMateGray100,
                modifier = Modifier
                    .size(18.dp)
                    .noInteractionClickable { onClickNickNameEdit() },
            )
        }

    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
private fun SettingPagePreview() {
    LottoMateTheme {
        SettingScreen(
            userProfile = UserProfile(
                nickname = "NIckName"
            ),
            snackBarHostState = remember { SnackbarHostState() },
            latestLoginType = LoginTypeUiModel.EMAIL,
            onBackPressed = {},
            onClickLogin = {},
            onChangeNickNameCompleted = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
private fun SettingPageNotLoginPreview() {
    LottoMateTheme {
        SettingScreen(
            userProfile = null,
            latestLoginType = LoginTypeUiModel.EMAIL,
            snackBarHostState = remember { SnackbarHostState() },
            onBackPressed = {},
            onClickLogin = {},
            onChangeNickNameCompleted = {},
        )
    }
}