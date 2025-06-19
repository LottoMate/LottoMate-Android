package com.lottomate.lottomate.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.main.MainBottomTab
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue10
import com.lottomate.lottomate.presentation.ui.LottoMateBlue5
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateGreen5
import com.lottomate.lottomate.presentation.ui.LottoMatePeach10
import com.lottomate.lottomate.presentation.ui.LottoMatePeach5
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.presentation.ui.LottoMateYellow10
import com.lottomate.lottomate.presentation.ui.LottoMateYellow5
import com.lottomate.lottomate.utils.dropShadow
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlin.random.Random

enum class BannerType(
    val title: String = "",
    val subTitle: String = "",
    val backgroundColor: Color,
    @DrawableRes val img: Int,
    val route: LottoMateRoute,
) {
    // 지도
    MAP(
        title = "행운의 1등 로또\n어디서 샀을까?",
        subTitle = "당첨 판매점 보기",
        backgroundColor = LottoMateYellow5,
        img = R.drawable.img_banner_map,
        route = MainBottomTab.MAP.route,
    ),
    INTERVIEW(
        title = "로또 1등 당첨\n기 받아가요",
        subTitle = "당첨자 후기 보기",
        backgroundColor = LottoMatePeach5,
        img = R.drawable.img_banner02,
        route = MainBottomTab.MAP.route,
    ),
    // 당첨자 가이드
    WINNER_GUIDE(
        title = "내가 당첨이 됐다면\n어떻게 해야할까",
        subTitle = "당첨자 가이드 확인하기",
        backgroundColor = LottoMateBlue5,
        img = R.drawable.img_home_slo_cony,
        route = LottoMateRoute.LottoWinnerGuide,
    ),
    DRAW_LOTTO(
        title = "두구두구두구\n로또 당첨 발표",
        subTitle = "당첨 로또 정보 확인하기",
        backgroundColor = LottoMateGreen5,
        img = R.drawable.img_banner_map,
        route = MainBottomTab.MAP.route,
    ),
    CHECK_MY_LOTTO_RESULT(
        title = "내 로또 당첨 결과\n빠르게 보고 싶다면?",
        subTitle = "QR로 당첨 확인하기",
        backgroundColor = LottoMateBlue10,
        img = R.drawable.img_banner05,
        route = LottoMateRoute.LottoScan,
    ),
    RANDOM_LOTTO_NUMBER(
        title = "나만의 로또 번호\n뽑아봐요!",
        subTitle = "로또 번호 뽑으러 가기",
        backgroundColor = LottoMateYellow10,
        img = R.drawable.img_banner06,
        route = BottomTabRoute.Pocket,
    ),
    REQUEST_WINNER_LOTTO_STORE(
        title = "우리 동네 로또 명당\n알고 싶어요",
        subTitle = "지도 오픈 요청하기",
        backgroundColor = LottoMatePeach10,
        img = R.drawable.img_banner07,
        route = MainBottomTab.MAP.route,
    ),
    VOTE_IDEA(
        title = "당첨이 되면\n뭐가 제일 하고 싶어?",
        subTitle = "메이트 투표 아이디어 접수",
        backgroundColor = LottoMateGray20,
        img = R.drawable.img_banner08,
        route = MainBottomTab.MAP.route,
    );

    companion object {
        fun find(index: Int): BannerType {
            return entries.find { it.ordinal == index } ?: MAP
        }
        fun getRandomGeneralBannerType(): BannerType {
            val randomOrdinal = (0..entries.size).random()
            return find(randomOrdinal)
        }

        fun getRandomBannerTypeBeforeResult(): BannerType {
            return listOf(WINNER_GUIDE, INTERVIEW).random()
        }

        fun getBannerResultExpired() = REQUEST_WINNER_LOTTO_STORE

        fun getResultScreenBannerType(isWinner: Boolean): BannerType {
            return if (isWinner) WINNER_GUIDE else MAP
        }
    }
}

@Composable
fun LottoMateCard(
    modifier: Modifier = Modifier,
    shadowColor: Color = LottoMateBlack.copy(alpha = 0.16f),
    shape: Shape = RoundedCornerShape(Dimens.RadiusLarge),
    border: BorderStroke? = null,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = LottoMateWhite,
    ),
    elevation: CardElevation = CardDefaults.cardElevation(),
    onClick: () -> Unit = {},
    content: @Composable (() -> Unit),
) {
    Card(
        modifier = modifier
            .dropShadow(
                shape = RoundedCornerShape(Dimens.RadiusLarge),
                color = shadowColor,
                blur = 8.dp,
                offsetX = 0.dp,
                offsetY = 0.dp
            )
            .noInteractionClickable { onClick() },
        colors = colors,
        shape = shape,
        border = border,
        elevation = elevation,
    ) {
        content()
    }
}

@Composable
fun BannerCard(
    modifier: Modifier = Modifier,
    type: BannerType = BannerType.WINNER_GUIDE,
    onClickBanner: (BannerType) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = type.backgroundColor,
                shape = RoundedCornerShape(Dimens.RadiusLarge)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClickBanner(type) }
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            LottoMateText(
                text = type.title,
                style = LottoMateTheme.typography.headline2,
            )

            Spacer(modifier = Modifier.height(4.dp))

            LottoMateText(
                text = type.subTitle,
                style = LottoMateTheme.typography.caption
                    .copy(LottoMateGray90),
            )
        }

        Image(
            bitmap = ImageBitmap.imageResource(id = type.img),
            contentDescription = "Lotto Info Bottom Banner Image",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 24.dp)
                .size(width = 122.dp, height = 76.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BannerCardPreview() {
    LottoMateTheme {
        BannerCard(
            type = BannerType.MAP,
            onClickBanner = {}
        )
    }
}