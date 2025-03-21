package com.lottomate.lottomate.presentation.screen.winnerguide.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.winnerguide.model.WinnerGuideType
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import com.lottomate.lottomate.utils.noInteractionClickable
import com.naver.maps.geometry.LatLng

@Composable
fun TopPrizeClaimLocation(
    modifier: Modifier = Modifier,
    type: WinnerGuideType,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        LottoMateText(
            text = "등수별 당첨금 수령 장소 & 준비물",
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
        )

        Column(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(top = Dimens.DefaultPadding20)
                .dropShadow(
                    shape = RoundedCornerShape(Dimens.RadiusLarge),
                    color = LottoMateBlack.copy(alpha = 0.16f),
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blur = 8.dp
                )
                .background(LottoMateWhite, RoundedCornerShape(Dimens.RadiusLarge))

                .padding(horizontal = 28.dp, vertical = 24.dp),
        ) {
            when (type) {
                WinnerGuideType.LOTTO645 -> {
                    TopPrizeClaimLocationItem(
                        rank = "1등",
                        claimLocation = "NH농협 본점",
                        requiredItems = listOf("당첨복권", "신분증"),
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "2등, 3등",
                        hasCaption = true,
                        claimLocation = "NH농협 전국지점",
                        requiredItems = listOf("당첨복권", "신분증"),
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "4등, 5등",
                        claimLocation = "전국 복권 판매점",
                        requiredItems = listOf("당첨복권"),
                    )
                }
                WinnerGuideType.LOTTO720 -> {
                    TopPrizeClaimLocationItem(
                        rank = "1등, 2등",
                        claimLocation = "동행복권 본사",
                        requiredItems = listOf("당첨복권", "신분증", "통장사본"),
                        hasCaption = true,
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "3등, 4등",
                        claimLocation = "NH농협 전국지점",
                        requiredItems = listOf("당첨복권", "신분증"),
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "5등, 6등, 7등",
                        claimLocation = "전국 복권 판매점",
                        requiredItems = listOf("당첨복권"),
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "보너스",
                        claimLocation = "동행복권 본사",
                        requiredItems = listOf("당첨복권", "신분증", "통장사본"),
                        hasCaption = true,
                    )
                }
                WinnerGuideType.SPEETTO -> {
                    TopPrizeClaimLocationItem(
                        rank = "1억 이상",
                        claimLocation = "동행복권 본사",
                        requiredItems = listOf("당첨복권", "신분증"),
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "200만원 초과 1억원 이하",
                        claimLocation = "NH농협 전국지점",
                        requiredItems = listOf("당첨복권", "신분증"),
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "5만원 초과 200만원 이하",
                        claimLocation = "NH농협 전국지점",
                        requiredItems = listOf("당첨복권"),
                    )

                    WinnerGuideHorizontalDivider()

                    TopPrizeClaimLocationItem(
                        rank = "5만원 이하",
                        claimLocation = "전국 복권 판매점",
                        requiredItems = listOf("당첨복권"),
                    )
                }
            }
        }
    }
}

@Composable
fun WinnerGuideHorizontalDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        color = LottoMateGray20,
    )
}

@Composable
private fun TopPrizeClaimLocationItem(
    modifier: Modifier = Modifier,
    rank: String,
    claimLocation: String,
    claimLatLng: LatLng? = null,
    requiredItems: List<String>,
    hasCaption: Boolean = false,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = when (requiredItems.size) {
                1 -> R.drawable.img_winner_guide03
                2 -> R.drawable.img_winner_guide01
                3 -> R.drawable.img_winner_guide02
                else -> R.drawable.img_winner_guide02
            }),
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            LottoMateText(
                text = rank,
                style = LottoMateTheme.typography.headline2,
            )

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "장소",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                )

                LottoMateText(
                    text = claimLocation,
                    style = LottoMateTheme.typography.label2,
                    modifier = Modifier.padding(start = 4.dp),
                )

                // 장소 위/경도 값이 있을 때에만 아이콘 노출
                claimLatLng?.let {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_place),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(14.dp)
                            .noInteractionClickable {
                                // TODO : 지도 아이콘 클릭 시, 지도 표시
                            },
                        tint = LottoMateGray100,
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "준비물",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                )

                LottoMateText(
                    text = requiredItems.joinToString(),
                    style = LottoMateTheme.typography.label2,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }

            if (hasCaption) {
                LottoMateText(
                    text = "*연금식 당첨금으로 매달 지급",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}