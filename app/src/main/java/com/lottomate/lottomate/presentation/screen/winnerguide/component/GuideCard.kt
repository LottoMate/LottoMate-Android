package com.lottomate.lottomate.presentation.screen.winnerguide.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue5
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateRed5
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import com.lottomate.lottomate.utils.noInteractionClickable

/**
 * 당첨자 가이드 화면에 사용되는 공통 Card Component
 */
@Composable
fun GuideNoticeCard(
    modifier: Modifier = Modifier,
    index: Int,
    text: String,
    captions: List<String> = emptyList(),
    color: Color,
    subContents: List<String> = emptyList(),
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .size(width = 260.dp, height = 186.dp)
            .dropShadow(
                shape = RoundedCornerShape(Dimens.RadiusMedium),
                blur = 8.dp,
                offsetX = 0.dp,
                offsetY = 0.dp,
                color = LottoMateBlack.copy(alpha = 0.25f),
            ),
        colors = CardDefaults.cardColors(
            containerColor = LottoMateWhite,
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20, vertical = 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        if (color == LottoMateRed50) LottoMateRed5 else LottoMateBlue5,
                        CircleShape
                    )
            ) {
                LottoMateText(
                    text = index.toString(),
                    style = LottoMateTheme.typography.headline1
                        .copy(color = color),
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )

            when {
                captions.isNotEmpty() -> {
                    captions.forEachIndexed { index, caption ->
                        LottoMateText(
                            text = caption,
                            style = LottoMateTheme.typography.caption
                                .copy(color = LottoMateGray100),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = if (index == 0) 6.dp else 4.dp),
                        )
                    }
                }

                subContents.isNotEmpty() -> {
                    subContents.forEachIndexed { index, subContent ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = if (index == 0) 6.dp else 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            LottoMateText(
                                text = subContent,
                                style = LottoMateTheme.typography.caption
                                    .copy(color = LottoMateGray120),
                                modifier = Modifier                                ,
                            )

                            if (subContent.contains("고객센터")) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_call),
                                    contentDescription = null,
                                    tint = LottoMateGray100,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .size(14.dp)
                                        .noInteractionClickable {
                                            val dialIntent = Intent(
                                                Intent.ACTION_CALL,
                                                Uri.parse("tel:1566-5520")
                                            )
                                            context.startActivity(dialIntent)
                                        },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GuideNoticeCardPreview() {
    LottoMateTheme {
        Column {
            GuideNoticeCard(
                modifier = Modifier.padding(16.dp),
                color = LottoMateRed50,
                index = 1,
                text = "19세 미만은 복권을\n살 수 없어요",
                captions = listOf(
                    "*검사비는 소지자가 부담합니다.",
                    "*검사비는 소지자가 부담합니다.",
                )
            )

            GuideNoticeCard(
                modifier = Modifier.padding(16.dp),
                color = LottoMateBlue50,
                index = 2,
                text = "19세 미만은 복권을\n살 수 없어요",
                subContents = listOf(
                    "월 - 금 오전 10시 - 오후 3시",
                    "고객센터 : 1566 - 5520",
                )
            )
        }

    }
}