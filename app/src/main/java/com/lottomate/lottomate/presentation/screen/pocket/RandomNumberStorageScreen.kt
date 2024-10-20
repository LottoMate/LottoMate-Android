package com.lottomate.lottomate.presentation.screen.pocket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun RandomNumbersStorageRoute(
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBackPressed: () -> Unit,
) {
    RandomNumberStorageScreen(
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun RandomNumberStorageScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(Dimens.BaseTopPadding.plus(16.dp)))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.DefaultPadding20),
            ) {
                LottoMateText(
                    text = "저장한 번호",
                    style = LottoMateTheme.typography.title3,
                )

                Spacer(modifier = Modifier.height(2.dp))

                LottoMateText(
                    text = "저장한 행운의 번호를 확인해주세요.",
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray80),
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            SavedRandomNumbersSection(
                modifier = Modifier.fillMaxWidth(),
                savedDate = "2024.10.19",
                savedRandomNumbers = listOf(
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                ),
                onClickCopyRandomNumbers = {},
                onClickDeleteRandomNumbers = {},
            )

            Spacer(modifier = Modifier.height(16.dp))

            SavedRandomNumbersSection(
                modifier = Modifier.fillMaxWidth(),
                savedDate = "2024.10.19",
                savedRandomNumbers = listOf(
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                ),
                onClickCopyRandomNumbers = {},
                onClickDeleteRandomNumbers = {},
            )

            Spacer(modifier = Modifier.height(16.dp))

            SavedRandomNumbersSection(
                modifier = Modifier.fillMaxWidth(),
                savedDate = "2024.10.19",
                savedRandomNumbers = listOf(
                    listOf(4, 5, 11, 21, 37, 40),
                    listOf(4, 5, 11, 21, 37, 40),
                ),
                onClickCopyRandomNumbers = {},
                onClickDeleteRandomNumbers = {},
            )

            Spacer(modifier = Modifier.height(Dimens.DefaultPadding20))
        }
        LottoMateTopAppBar(
            titleRes = R.string.top_app_bar_empty_title,
            hasNavigation = false,
            actionButtons = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "",
                    modifier = Modifier.noInteractionClickable { onBackPressed() }
                )
            }
        )
    }
}

@Composable
private fun SavedRandomNumbersSection(
    modifier: Modifier = Modifier,
    savedDate: String,
    savedRandomNumbers: List<List<Int>>,
    onClickCopyRandomNumbers: (Int) -> Unit,
    onClickDeleteRandomNumbers: (Int) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
    ) {
        LottoMateText(
            text = savedDate,
            style = LottoMateTheme.typography.label2
                .copy(LottoMateGray80),
        )

        Spacer(modifier = Modifier.height(12.dp))

        savedRandomNumbers.forEachIndexed { index, randomNumbers ->
            DrawNumberRow(
                numbers = randomNumbers,
                onClickCopyRandomNumbers = { onClickCopyRandomNumbers(index) },
                onClickDeleteRandomNumbers = { onClickDeleteRandomNumbers(index) },
            )

            if (index != savedRandomNumbers.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun DrawNumberRow(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    onClickCopyRandomNumbers: () -> Unit,
    onClickDeleteRandomNumbers: () -> Unit,
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
            painter = painterResource(id = R.drawable.icon_trash),
            contentDescription = null,
            modifier = Modifier.noInteractionClickable { onClickDeleteRandomNumbers() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RandomNumberStorageScreenPreview() {
    LottoMateTheme {
        RandomNumberStorageScreen(
            onBackPressed = {},
        )
    }
}