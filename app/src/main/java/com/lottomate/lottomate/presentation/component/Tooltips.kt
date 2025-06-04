package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGradient1
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

enum class ToolTipDirection(val group: Group) {
    TOP(Group.TOP),
    TOP_LEFT(Group.TOP),
    TOP_RIGHT(Group.TOP),
    LEFT(Group.LEFT),
    RIGHT(Group.RIGHT),
    BOTTOM(Group.BOTTOM),
    BOTTOM_LEFT(Group.BOTTOM),
    BOTTOM_RIGHT(Group.BOTTOM);

    enum class Group {
        TOP,
        LEFT,
        RIGHT,
        BOTTOM
    }
}

@Composable
fun LottoMateTooltip(
    modifier: Modifier = Modifier,
    text: String,
    direction: ToolTipDirection,
) {
    when (direction.group) {
        ToolTipDirection.Group.TOP -> LottoMateTopTooltip(
            modifier = modifier,
            text = text,
            direction = direction,
        )
        ToolTipDirection.Group.LEFT -> {
            LottoMateLeftToolTip(
                modifier = modifier,
                text = text,
                direction = direction,
            )
        }
        ToolTipDirection.Group.RIGHT -> {
            LottoMateRightToolTip(
                modifier = modifier,
                text = text,
                direction = direction,
            )
        }
        ToolTipDirection.Group.BOTTOM -> {
            LottoMateBottomTooltip(
                modifier = modifier,
                text = text,
                direction = direction,
            )
        }
    }

}

@Composable
private fun LottoMateTopTooltip(
    modifier: Modifier = Modifier,
    text: String,
    direction: ToolTipDirection,
) {
    val horizontalAlignment = when (direction) {
        ToolTipDirection.TOP -> Alignment.CenterHorizontally
        ToolTipDirection.TOP_LEFT -> Alignment.Start
        ToolTipDirection.TOP_RIGHT -> Alignment.End
        else -> Alignment.CenterHorizontally
    }
    
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier.wrapContentSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_tooltip_arrow_top),
            contentDescription = "top tooltip arrow",
            modifier = Modifier
                .padding(
                    start = if (direction == ToolTipDirection.TOP_LEFT) 9.dp else 0.dp,
                    end = if (direction == ToolTipDirection.TOP_RIGHT) 9.dp else 0.dp,
                ),
        )

        
        Box(
            modifier = Modifier
                .background(LottoMateGradient1, RoundedCornerShape(Dimens.RadiusExtraSmall))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateWhite),
            )
        }
    }
}

@Composable
private fun LottoMateLeftToolTip(
    modifier: Modifier = Modifier,
    text: String,
    direction: ToolTipDirection,
) {
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_tooltip_arrow_left),
            contentDescription = "left tooltip arrow",
        )
        
        Box(
            modifier = Modifier
                .background(LottoMateGradient1, RoundedCornerShape(Dimens.RadiusExtraSmall))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateWhite),
            )
        }
    }
}

@Composable
private fun LottoMateBottomTooltip(
    modifier: Modifier = Modifier,
    text: String,
    direction: ToolTipDirection,
) {
    val horizontalAlignment = when (direction) {
        ToolTipDirection.BOTTOM -> Alignment.CenterHorizontally
        ToolTipDirection.BOTTOM_LEFT -> Alignment.Start
        ToolTipDirection.BOTTOM_RIGHT -> Alignment.End
        else -> Alignment.CenterHorizontally
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier.wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .background(LottoMateGradient1, RoundedCornerShape(Dimens.RadiusExtraSmall))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateWhite),
            )
        }

        Image(
            painter = painterResource(id = R.drawable.img_tooltip_arrow_bottom),
            contentDescription = "right tooltip arrow",
            modifier = Modifier
                .padding(
                    start = if (direction == ToolTipDirection.BOTTOM_LEFT) 9.dp else 0.dp,
                    end = if (direction == ToolTipDirection.BOTTOM_RIGHT) 9.dp else 0.dp,
                ),
        )
    }
}

@Composable
private fun LottoMateRightToolTip(
    modifier: Modifier = Modifier,
    text: String,
    direction: ToolTipDirection,
) {
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(LottoMateGradient1, RoundedCornerShape(Dimens.RadiusExtraSmall))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateWhite),
            )
        }

        Image(
            painter = painterResource(id = R.drawable.img_tooltip_arrow_right),
            contentDescription = "bottom tooltip arrow",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun LottoMateTooltipPreview() {
    LottoMateTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.TOP
                )

                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.TOP_LEFT
                )
                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.TOP_RIGHT
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.LEFT
                )
                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.RIGHT
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.BOTTOM
                )
                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.BOTTOM_LEFT
                )
                LottoMateTooltip(
                    text = "tooltip",
                    direction = ToolTipDirection.BOTTOM_RIGHT
                )
            }
        }
    }
}