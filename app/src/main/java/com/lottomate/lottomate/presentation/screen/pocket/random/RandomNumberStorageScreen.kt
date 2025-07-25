package com.lottomate.lottomate.presentation.screen.pocket.random

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.screen.pocket.random.contract.RandomMyNumbersEffect
import com.lottomate.lottomate.presentation.screen.pocket.random.contract.RandomMyNumbersEvent
import com.lottomate.lottomate.presentation.screen.pocket.random.contract.RandomMyNumbersUiState
import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomMyNumbersGroupUiModel
import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomMyNumbersUiModel
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun RandomNumbersStorageRoute(
    vm: RandomNumbersStorageViewModel = hiltViewModel(),
    padding: PaddingValues,
    onShowGlobalSnackBar: (String) -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
    onBackPressed: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is RandomMyNumbersEffect.ShowSnackBar -> {
                    onShowGlobalSnackBar(context.getString(effect.messageRes))
                }
            }
        }
    }

    RandomNumberStorageScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onClickCopyRandomNumbers = { vm.handleEvent(RandomMyNumbersEvent.CopyRandomNumbers(it)) },
        onClickDeleteRandomNumbers = { vm.handleEvent(RandomMyNumbersEvent.RemoveRandomNumbers(it)) },
    )
}

@Composable
private fun RandomNumberStorageScreen(
    modifier: Modifier = Modifier,
    uiState: RandomMyNumbersUiState,
    onBackPressed: () -> Unit,
    onClickCopyRandomNumbers: (List<Int>) -> Unit,
    onClickDeleteRandomNumbers: (Int) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item { Spacer(modifier = Modifier.height(Dimens.BaseTopPadding.plus(16.dp))) }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.DefaultPadding20),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_random_number_save),
                        style = LottoMateTheme.typography.title3
                            .copy(color = LottoMateBlack),
                    )

                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_sub_random_number_save),
                        style = LottoMateTheme.typography.body1
                            .copy(color = LottoMateGray80),
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(34.dp)) }

            lottoNumbers.groupBy { it.date }.forEach { (date, randomLottoNumber) ->
                SavedRandomNumbersSection(
                    modifier = Modifier.fillMaxWidth(),
                    savedDate = date,
                    savedRandomNumbers = randomLottoNumber,
                    onClickCopyRandomNumbers = onClickCopyRandomNumbers,
                    onClickDeleteRandomNumbers = onClickDeleteRandomNumbers,
                )
            when (uiState) {
                RandomMyNumbersUiState.Idle -> {}
                RandomMyNumbersUiState.Empty -> {
                }
                is RandomMyNumbersUiState.Success -> {
                    items(uiState.data) { (date, randomMyNumbers) ->
                        SavedRandomNumbersSection(
                            modifier = Modifier.fillMaxWidth(),
                            date = date,
                            randomMyNumbers = randomMyNumbers,
                            onClickCopyRandomNumbers = onClickCopyRandomNumbers,
                            onClickDeleteRandomNumbers = onClickDeleteRandomNumbers,
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
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
    }
}

@Composable
private fun SavedRandomNumbersSection(
    modifier: Modifier = Modifier,
    date: String,
    randomMyNumbers: List<RandomMyNumbersUiModel>,
    onClickCopyRandomNumbers: (List<Int>) -> Unit,
    onClickDeleteRandomNumbers: (Int) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
    ) {
        LottoMateText(
            text = date,
            style = LottoMateTheme.typography.label2
                .copy(LottoMateGray80),
        )

        Spacer(modifier = Modifier.height(12.dp))

        randomMyNumbers.forEachIndexed { index, myNumbers ->
            DrawNumberRow(
                modifier = Modifier.fillMaxWidth(),
                numbers = myNumbers.numbers,
                onClickCopyRandomNumbers = { onClickCopyRandomNumbers(myNumbers.numbers) },
                onClickDeleteRandomNumbers = { onClickDeleteRandomNumbers(myNumbers.id) },
            )

            if (index != randomMyNumbers.lastIndex) {
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (number in numbers) {
                LottoBall645(
                    number = number,
                    size = 28.dp,
                )
            }
        }

        Spacer(modifier = Modifier.width(19.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
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
            uiState = RandomMyNumbersUiState.Success(
                listOf(
                    RandomMyNumbersGroupUiModel(
                        date = "2024.01.01",
                        numbers = List(5) {
                            RandomMyNumbersUiModel(
                                id = it,
                                numbers = listOf(1, 2, 3, 4, 5, 6)
                            )
                        }
                    ),
                    RandomMyNumbersGroupUiModel(
                        date = "2024.10.05",
                        numbers = List(10) {
                            RandomMyNumbersUiModel(
                                id = it,
                                numbers = listOf(1, 2, 3, 4, 5, 6)
                            )
                        }
                    )
                )
            ),
            onBackPressed = {},
            onClickCopyRandomNumbers = {},
            onClickDeleteRandomNumbers = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RandomNumberEmptyStorageScreenPreview() {
    LottoMateTheme {
        RandomNumberStorageScreen(
            uiState = RandomMyNumbersUiState.Empty,
            onBackPressed = {},
            onClickCopyRandomNumbers = {},
            onClickDeleteRandomNumbers = {},
        )
    }
}