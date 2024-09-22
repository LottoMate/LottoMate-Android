package com.lottomate.lottomate.presentation.screen.map.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateTransparent
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import com.lottomate.lottomate.utils.noInteractionClickable

private val FilterButtonRadius = 30.dp

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int? = null,
    iconDescription: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .dropShadow(
                shape = RoundedCornerShape(FilterButtonRadius),
                offsetX = 0.dp,
                offsetY = 0.dp,
                blur = 8.dp,
            )
            .background(
                color = LottoMateWhite,
                shape = RoundedCornerShape(FilterButtonRadius)
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) LottoMateBlack else LottoMateTransparent,
                shape = RoundedCornerShape(FilterButtonRadius)
            )
            .clip(RoundedCornerShape(FilterButtonRadius))
            .noInteractionClickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = iconDescription
                )

                Spacer(modifier = Modifier.width(4.dp))
            }

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.label2,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreview() {
    LottoMateTheme {
        Column(modifier = Modifier.padding(20.dp)) {
            FilterButton(
                text = "찜",
                isSelected = true,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(20.dp))

            FilterButton(
                text = "복권 전체",
                iconRes = R.drawable.icon_filter_12,
                onClick = {}
            )
        }
    }
}