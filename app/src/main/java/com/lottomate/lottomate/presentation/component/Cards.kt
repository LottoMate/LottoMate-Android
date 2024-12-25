package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.presentation.ui.LottoMateYellow5
import com.lottomate.lottomate.utils.dropShadow
import com.lottomate.lottomate.utils.noInteractionClickable

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
    onClickBanner: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(
                color = LottoMateYellow5,
                shape = RoundedCornerShape(Dimens.RadiusLarge)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClickBanner
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 20.dp, end = 18.dp),
        ) {
            Column {
                LottoMateText(
                    text = stringResource(id = R.string.banner_lotto_info_title),
                    style = LottoMateTheme.typography.headline2,
                )

                Spacer(modifier = Modifier.height(4.dp))

                LottoMateText(
                    text = stringResource(id = R.string.banner_lotto_info_sub_title),
                    style = LottoMateTheme.typography.caption1
                        .copy(LottoMateGray120),
                )
            }

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.bn_pochi_lotto_info),
                contentDescription = "Lotto Info Bottom Banner Image",
                modifier = Modifier.padding(bottom = 7.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BannerCardPreview() {
    LottoMateTheme {
        BannerCard {

        }
    }
}