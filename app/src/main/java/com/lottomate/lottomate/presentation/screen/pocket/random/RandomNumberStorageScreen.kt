package com.lottomate.lottomate.presentation.screen.pocket.random

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomLottoNumber
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RandomNumbersStorageRoute(
    vm: RandomNumbersStorageViewModel = hiltViewModel(),
    padding: PaddingValues,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
    onBackPressed: () -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val lottoNumbers by vm.lottoNumbers.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        vm.snackBarFlow.collectLatest {
            snackBarHostState.showSnackbar(it)
        }

        vm.errorFlow.collectLatest { error -> onShowErrorSnackBar(error) }
    }

    RandomNumberStorageScreen(
        snackBarHostState = snackBarHostState,
        lottoNumbers = lottoNumbers,
        onBackPressed = onBackPressed,
        onClickCopyRandomNumbers = { vm.copyLottoNumbers(it) },
        onClickDeleteRandomNumbers = { vm.deleteLottoNumbers(it) },
    )
}

@Composable
private fun RandomNumberStorageScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    lottoNumbers: List<RandomLottoNumber>,
    onBackPressed: () -> Unit,
    onClickCopyRandomNumbers: (List<Int>) -> Unit,
    onClickDeleteRandomNumbers: (Int) -> Unit,
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
                    text = stringResource(id = R.string.pocket_title_random_number_save),
                    style = LottoMateTheme.typography.title3,
                )

                Spacer(modifier = Modifier.height(2.dp))

                LottoMateText(
                    text = stringResource(id = R.string.pocket_title_sub_random_number_save),
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            lottoNumbers.groupBy { it.date }.forEach { (date, randomLottoNumber) ->
                SavedRandomNumbersSection(
                    modifier = Modifier.fillMaxWidth(),
                    savedDate = date,
                    savedRandomNumbers = randomLottoNumber,
                    onClickCopyRandomNumbers = onClickCopyRandomNumbers,
                    onClickDeleteRandomNumbers = onClickDeleteRandomNumbers,
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        LottoMateTopAppBar(
            titleRes = R.string.top_app_bar_empty_title,
            hasNavigation = false,
            actionButtons = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "",
                    tint = LottoMateGray100,
                    modifier = Modifier.noInteractionClickable { onBackPressed() }
                )
            }
        )

        snackBarHostState.currentSnackbarData?.let {
            LottoMateSnackBarHost(
                modifier = Modifier.align(Alignment.TopCenter),
                snackBarHostState = snackBarHostState
            ) {
                LottoMateSnackBar(
                    modifier = Modifier
                        .padding(top = Dimens.BaseTopPadding.plus(12.dp)),
                    message = it.visuals.message
                )
            }
        }
    }
}

@Composable
private fun SavedRandomNumbersSection(
    modifier: Modifier = Modifier,
    savedDate: String,
    savedRandomNumbers: List<RandomLottoNumber>,
    onClickCopyRandomNumbers: (List<Int>) -> Unit,
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
                modifier = Modifier.fillMaxWidth(),
                numbers = randomNumbers.numbers,
                onClickCopyRandomNumbers = { onClickCopyRandomNumbers(randomNumbers.numbers) },
                onClickDeleteRandomNumbers = { onClickDeleteRandomNumbers(randomNumbers.id) },
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
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            numbers.forEach { num ->
                LottoBall645(
                    number = num,
                    size = 28.dp,
                )

                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.width(19.dp))

        Row {
            Icon(
                painter = painterResource(id = R.drawable.icon_copy),
                contentDescription = null,
                tint = LottoMateGray100,
                modifier = Modifier
                    .size(24.dp)
                    .noInteractionClickable { onClickCopyRandomNumbers() }
            )

            Icon(
                painter = painterResource(id = R.drawable.icon_trash),
                contentDescription = null,
                tint = LottoMateGray100,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp)
                    .noInteractionClickable { onClickDeleteRandomNumbers() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RandomNumberStorageScreenPreview() {
    LottoMateTheme {
        RandomNumberStorageScreen(
            snackBarHostState = SnackbarHostState(),
            lottoNumbers = listOf(
                RandomLottoNumber(1, listOf(3, 12, 19, 24, 33, 42), "2025-03-16"),
                RandomLottoNumber(2, listOf(7, 11, 15, 28, 35, 44), "2025-03-16"),
                RandomLottoNumber(3, listOf(1, 9, 17, 23, 38, 45), "2025-03-18"),
                RandomLottoNumber(4, listOf(2, 13, 22, 30, 39, 41), "2025-03-19"),
                RandomLottoNumber(5, listOf(5, 8, 18, 26, 32, 43), "2025-03-20"),
                RandomLottoNumber(6, listOf(6, 14, 20, 29, 36, 40), "2025-03-21"),
                RandomLottoNumber(7, listOf(4, 10, 16, 27, 31, 37), "2025-03-22"),
                RandomLottoNumber(8, listOf(1, 6, 11, 19, 28, 34), "2025-03-23"),
                RandomLottoNumber(9, listOf(3, 9, 14, 25, 33, 44), "2025-03-24"),
                RandomLottoNumber(10, listOf(2, 7, 13, 21, 35, 45), "2025-03-25")
            ).sortedByDescending { it.date },
            onBackPressed = {},
            onClickCopyRandomNumbers = {},
            onClickDeleteRandomNumbers = {},
        )
    }
}