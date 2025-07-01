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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.lottomate.lottomate.domain.model.LoginType
import com.lottomate.lottomate.presentation.component.LoginIconButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTooltip
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.component.ToolTipDirection
import com.lottomate.lottomate.presentation.model.LoginTypeUiModel
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlin.math.roundToInt

@Composable
fun SettingRoute(
    vm: SettingViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToMyPage: () -> Unit,
    onBackPressed: () -> Unit,
) {
    val latestLoginType by vm.latestLoginType

    SettingScreen(
        latestLoginType = latestLoginType,
        onClickMyPage = moveToMyPage,
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun SettingScreen(
    modifier: Modifier = Modifier,
    latestLoginType: LoginTypeUiModel?,
    onClickMyPage: () -> Unit = {},
    onBackPressed: () -> Unit,
) {
    val density = LocalDensity.current

    var loginIconOffset by remember { mutableStateOf(Offset.Zero) }
    var loginIconSize by remember { mutableStateOf(IntSize.Zero) }
    var latestLoginToolTipSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
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
                TopLoginSection(
                    latestLoginType = latestLoginType,
                    onGloballyPositioned = { coordinates ->
                        val position = coordinates.positionInRoot()

                        loginIconOffset = position
                        loginIconSize = coordinates.size
                    }
                )

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
            titleRes = R.string.setting_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )
    }
}

@Composable
private fun TopLoginSection(
    latestLoginType: LoginTypeUiModel?,
    onGloballyPositioned: (LayoutCoordinates) -> Unit,
) {
    Column {
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
                    onClick = { }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
private fun SettingPagePreview() {
    LottoMateTheme {
        SettingScreen(
            latestLoginType = LoginTypeUiModel.EMAIL,
            onBackPressed = {},
        )
    }
}