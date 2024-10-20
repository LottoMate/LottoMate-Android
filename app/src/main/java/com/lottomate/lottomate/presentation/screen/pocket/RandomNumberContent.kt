package com.lottomate.lottomate.presentation.screen.pocket

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.utils.noInteractionClickable

/**
 * 사용자가 당일에 뽑은 랜덤 번호가 저장되어 표시되는 화면
 */
@Composable
fun RandomNumberContent(
    modifier: Modifier = Modifier,
    drewRandomNumbers: List<List<Int>>,
    onClickDrawRandomNumbers: () -> Unit,
    onClickStorageOfRandomNumbers: () -> Unit,
    onClickCopyRandomNumbers: (List<Int>) -> Unit,
    onClickSaveRandomNumbers: (List<Int>) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                LottoMateText(
                    text = "행운의 랜덤 뽑기",
                    style = LottoMateTheme.typography.title3
                        .copy(color = LottoMateGray100),
                )

                LottoMateText(
                    text = "행운의 랜덤 뽑기",
                    style = LottoMateTheme.typography.title2
                        .copy(color = LottoMateBlack),
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.icon_file),
                contentDescription = "",
                modifier = Modifier.noInteractionClickable { onClickStorageOfRandomNumbers() }
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.pocket_venus_boli_bori),
                contentDescription = null
            )

            LottoMateSolidButton(
                text = "랜덤 뽑기",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                onClick = onClickDrawRandomNumbers,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
                    .align(alignment = Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        BottomDrawNumbers(
            modifier = Modifier.fillMaxWidth(),
            numbers = drewRandomNumbers,
            onClickCopyRandomNumbers = onClickCopyRandomNumbers,
            onClickSaveRandomNumbers = onClickSaveRandomNumbers,
        )
    }
}

@Composable
private fun BottomDrawNumbers(
    modifier: Modifier = Modifier,
    numbers: List<List<Int>>,
    onClickCopyRandomNumbers: (List<Int>) -> Unit,
    onClickSaveRandomNumbers: (List<Int>) -> Unit,
) {
    var ballsToShow by remember { mutableIntStateOf(5) }
    var isInitialShow by remember {
        mutableStateOf(true)
    }
    val visibleNumbers = numbers.take(ballsToShow)

    Column(
        modifier = modifier,
    ) {
        LottoMateText(
            text = "오늘 뽑은 번호",
            style = LottoMateTheme.typography.headline1
                .copy(color = LottoMateBlack),
        )

        LottoMateText(
            text = "오늘이 지나면 뽑은 번호가 사라집니다.",
            style = LottoMateTheme.typography.caption1
                .copy(color = LottoMateGray80),
        )

        Spacer(modifier = Modifier.height(16.dp))

        visibleNumbers.chunked(if (isInitialShow) 5 else 10).forEachIndexed { index, chunk ->
            chunk.forEachIndexed { chunkIndex, it ->
                DrawNumberRow(
                    numbers = it,
                    onClickCopyRandomNumbers = { onClickCopyRandomNumbers(it) },
                    onClickSaveRandomNumbers = { onClickSaveRandomNumbers(it) },
                )

                if (chunkIndex != chunk.lastIndex) Spacer(modifier = Modifier.height(8.dp))
            }

            if (index != visibleNumbers.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }

        if (numbers.size > 5) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noInteractionClickable {
                        if (ballsToShow < numbers.size) ballsToShow += 10
                        else ballsToShow = 5
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = if (ballsToShow < numbers.size) "더보기" else "접기",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray100,)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = if (ballsToShow < numbers.size) painterResource(id = R.drawable.icon_arrow_down)
                    else painterResource(id = R.drawable.icon_arrow_up),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun DrawNumberRow(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    onClickCopyRandomNumbers: () -> Unit,
    onClickSaveRandomNumbers: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            numbers.forEach { num ->
                LottoBall645(
                    number = num,
                    size = 28.dp,
                )
            }
        }

        Spacer(modifier = Modifier.width(19.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_copy),
            contentDescription = null,
            modifier = Modifier.noInteractionClickable { onClickCopyRandomNumbers() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_save),
            contentDescription = null,
            modifier = Modifier.noInteractionClickable { onClickSaveRandomNumbers() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RandomNumberContentPreview() {
    LottoMateTheme {
        RandomNumberContent(
            drewRandomNumbers = emptyList(),
            onClickDrawRandomNumbers = {},
            onClickStorageOfRandomNumbers = {},
            onClickCopyRandomNumbers = {},
            onClickSaveRandomNumbers = {},
        )
    }
}