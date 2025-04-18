package com.lottomate.lottomate.presentation.screen.lottoinfo.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun LottoWinNumberCard(
    modifier: Modifier = Modifier,
    lottoType: LottoType,
    winNumbers: List<Int>,
    bonusNumbers: List<Int>,
) {
    LottoMateCard(
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = LottoMateGray20
        ),
        shadowColor = LottoMateBlack.copy(alpha = 0.1f),
    ) {
        when (lottoType) {
            LottoType.L645 -> {
                Lotto645WinNumber(
                    winNumbers = winNumbers,
                    bonusNumber = bonusNumbers.first()
                )
            }
            LottoType.L720 -> {
                Lotto720WinNumber(
                    winNumbers = winNumbers,
                    bonusNumbers = bonusNumbers,
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun Lotto645WinNumber(
    modifier: Modifier = Modifier,
    winNumbers: List<Int>,
    bonusNumber: Int,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LottoMateText(
                text = "당첨 번호",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray70)
            )
            LottoMateText(
                text = "보너스",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray70)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            winNumbers.forEach { number ->
                LottoBall645(number = number)
            }

            Icon(
                painter = painterResource(id = R.drawable.icon_mini_plus),
                contentDescription = "Just Separator",
            )

            LottoBall645(number = bonusNumber)
        }
    }
}

@Composable
private fun Lotto720WinNumber(
    modifier: Modifier = Modifier,
    winNumbers: List<Int>,
    bonusNumbers: List<Int>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        LottoMateText(
            text = "1등",
            style = LottoMateTheme.typography.caption
                .copy(LottoMateGray70),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottoBall720(
                    index = 0,
                    number = winNumbers.first(),
                    isBonusNumber = false,
                )

                Spacer(modifier = Modifier.width(8.dp))

                LottoMateText(
                    text = "조",
                    style = LottoMateTheme.typography.label2,
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            (1..winNumbers.lastIndex).forEach { index ->
                LottoBall720(
                    index = index,
                    number = winNumbers[index],
                    isBonusNumber = false
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = LottoMateGray20)

        Spacer(modifier = Modifier.height(16.dp))

        LottoMateText(
            text = "보너스",
            style = LottoMateTheme.typography.caption
                .copy(LottoMateGray70),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottoMateText(
                    text = "각",
                    style = LottoMateTheme.typography.body1,
                )

                Spacer(modifier = Modifier.width(24.dp))

                LottoMateText(
                    text = "조",
                    style = LottoMateTheme.typography.label2,
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            bonusNumbers.forEachIndexed { index, number ->
                LottoBall720(
                    index = index,
                    number = number,
                    isBonusNumber = true
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Lotto645WinNumberPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LottoWinNumberCard(
                lottoType = LottoType.L645,
                winNumbers = listOf(4, 5, 11, 21, 37, 40),
                bonusNumbers = listOf(43)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun Lotto720WinNumberPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LottoWinNumberCard(
                lottoType = LottoType.L720,
                winNumbers = listOf(5, 8, 1, 7, 5, 0, 9),
                bonusNumbers = listOf(1, 6, 1, 7, 1, 7),
            )
        }
    }
}