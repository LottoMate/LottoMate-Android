package com.lottomate.lottomate.presentation.screen.lottoinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateBlue30
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray140
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateGreen50
import com.lottomate.lottomate.presentation.ui.LottoMatePeach50
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateTransparent
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.presentation.ui.LottoMateYellow50

@Composable
fun LottoBall645(
    modifier: Modifier = Modifier,
    number: Int,
) {
    val color = when (number) {
        in 1..10 -> LottoMateYellow50
        in 11..20 -> LottoMateBlue50
        in 21..30 -> LottoMateRed50
        in 31..40 -> LottoMateGray90
        in 41..45 -> LottoMateGreen50
        else -> LottoMateTransparent
    }

    LottoBaseBall(
        modifier = modifier,
        number = number,
        color = color
    )
}

@Composable
fun LottoBall720(
    modifier: Modifier = Modifier,
    index: Int,
    number: Int,
    isBonusNumber: Boolean,
) {
    val color = when (if (isBonusNumber) index+1 else index) {
        0 -> LottoMateGray140
        1 -> LottoMateRed50
        2 -> LottoMatePeach50
        3 -> LottoMateYellow50
        4 -> LottoMateBlue50
        5 -> LottoMateBlue30
        else -> LottoMateGray100
    }

    LottoBaseBall(
        modifier = modifier,
        number = number,
        color = color
    )
}

@Composable
private fun LottoBaseBall(
    modifier: Modifier = Modifier,
    number: Int,
    color: Color,
) {
    Box(
        modifier = modifier
            .background(
                color = color,
                shape = CircleShape,
            )
            .size(30.dp),
        contentAlignment = Alignment.Center,
    ) {
        LottoMateText(
            text = number.toString(),
            style = LottoMateTheme.typography.label2.copy(
                color = LottoMateWhite
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoBallPreview() {
    LottoMateTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            LottoBall645(number = 1)
            LottoBall645(number = 11)
            LottoBall645(number = 21)
            LottoBall645(number = 31)
            LottoBall645(number = 41)
        }
    }
}