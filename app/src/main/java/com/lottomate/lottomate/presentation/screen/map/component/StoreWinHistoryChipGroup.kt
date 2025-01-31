package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMock
import com.lottomate.lottomate.presentation.screen.map.model.WinningDetail
import com.lottomate.lottomate.presentation.ui.LottoMateBlue5
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGreen5
import com.lottomate.lottomate.presentation.ui.LottoMatePeach5
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun StoreWinHistoryChipGroup(
    histories: List<WinningDetail>,
) {
    Column {
        histories.forEachIndexed { index, history ->
            StoreWinHistoryChip(history = history)

            if (index != histories.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun StoreWinHistoryChip(
    modifier: Modifier = Modifier,
    history: WinningDetail,
) {
    val backgroundColor = when (history.lottoType) {
        LottoTypeFilter.Lotto645.kr -> LottoMateGreen5
        LottoTypeFilter.Lotto720.kr -> LottoMateBlue5
        else -> LottoMatePeach5
    }

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(Dimens.RadiusSmall)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            LottoMateText(
                text = history.lottoType.plus(" ${history.rank}"),
                style = LottoMateTheme.typography.label2
                    .copy(fontWeight = FontWeight.Medium),
            )

            Spacer(modifier = Modifier.width(4.dp))

            LottoMateText(
                text = history.prize.plus(" 당첨"),
                style = LottoMateTheme.typography.label2,
            )

            Spacer(modifier = Modifier.width(4.dp))

            LottoMateText(
                text = history.round,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray100),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreWinHistoryChipGroupPreview() {
    LottoMateTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp))
        StoreWinHistoryChipGroup(
            histories = StoreInfoMock.winCountOfLottoType.flatMap { it.winningDetails },
        )
    }
}