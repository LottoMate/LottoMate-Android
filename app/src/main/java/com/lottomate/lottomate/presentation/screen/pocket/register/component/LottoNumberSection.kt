package com.lottomate.lottomate.presentation.screen.pocket.register.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterLottoNumber
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun LottoNumberSection(
    modifier: Modifier = Modifier,
    lotteryType: LottoType,
    round: Int,
    date: String,
    inputNumbers: List<RegisterLottoNumber>,
    hasPreRound: Boolean,
    hasNextRound: Boolean,
    onAddNewInputNumber: () -> Unit,
    onRemoveInputNumber: (Int) -> Unit,
    onChangeInputNumber: (Int, String) -> Unit,
    onChangeReset: (Int) -> Unit,
    onClickLottoRoundPicker: (LottoType) -> Unit,
    onClickNextRound: () -> Unit,
    onClickPreRound: () -> Unit,
) {
    val maxLottoNumberLength = when (lotteryType) {
        LottoType.L645 -> 12
        LottoType.L720 -> 6
        else -> 0
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (lotteryType) {
            LottoType.L645 -> {
                TopTitleSection(
                    icon = R.drawable.icon_lotto645_rank_first,
                    title = R.string.common_text_lottery_type_lotto645,
                )
            }
            LottoType.L720 -> {
                TopTitleSection(
                    icon = R.drawable.icon_lotto720_rank_first,
                    title = R.string.common_text_lottery_type_lotto720,
                )
            }
            else -> {}
        }

        MiddleLotteryRoundPicker(
            round = round,
            date = date,
            lotteryType = lotteryType,
            hasPreRound = hasPreRound,
            hasNextRound = hasNextRound,
            onClickNextRound = onClickNextRound,
            onClickPreRound = onClickPreRound,
            onClickLottoRoundPicker = { onClickLottoRoundPicker(lotteryType) },
        )

        Spacer(modifier = Modifier.height(32.dp))

        for (i in inputNumbers.indices) {
            RegisterLottoNumberTextField(
                number = inputNumbers[i].lottoNumbers,
                isError = inputNumbers[i].isError,
                maxNumberLength = maxLottoNumberLength,
                visualTransformation = when (lotteryType) {
                    LottoType.L645 -> Lotto645Transformation()
                    LottoType.L720 -> Lotto720Transformation()
                    else -> VisualTransformation.None
                },
                onChangeValue = {
                    if (i < inputNumbers.size) onChangeInputNumber(i, it)
                },
                onClickRemove = {
                    if (inputNumbers.size == 1) onChangeReset(i)
                    else onRemoveInputNumber(i)
                },
            )
        }

        if (inputNumbers.size < 5) {
            Row(
                modifier = Modifier
                    .clickable(
                        enabled = inputNumbers.first().lottoNumbers.length >= maxLottoNumberLength,
                        onClick = { onAddNewInputNumber() },
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_plus),
                    contentDescription = null,
                    tint = if (inputNumbers.first().lottoNumbers.length < maxLottoNumberLength) LottoMateGray40 else LottoMateGray100,
                    modifier = Modifier.size(14.dp)
                )

                LottoMateText(
                    text = stringResource(id = R.string.register_lotto_number_text_add),
                    style = LottoMateTheme.typography.caption
                        .copy(color = if (inputNumbers.first().lottoNumbers.length < maxLottoNumberLength) LottoMateGray40 else LottoMateGray100),
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

@Composable
fun TopTitleSection(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes title: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .padding(horizontal = Dimens.DefaultPadding20),
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = ""
        )

        LottoMateText(
            text = stringResource(id = title),
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun MiddleLotteryRoundPicker(
    modifier: Modifier = Modifier,
    round: Int,
    date: String,
    lotteryType: LottoType,
    hasNextRound: Boolean = false,
    hasPreRound: Boolean = true,
    onClickLottoRoundPicker: () -> Unit,
    onClickNextRound: () -> Unit,
    onClickPreRound: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_small_left),
                contentDescription = "",
                tint = if (hasPreRound) LottoMateGray100 else LottoMateGray40,
                modifier = Modifier.noInteractionClickable { if (hasPreRound) onClickPreRound() }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onClickLottoRoundPicker() }
            ) {
                LottoMateText(
                    text = round.toString().plus("회"),
                    style = LottoMateTheme.typography.headline1,
                )

                LottoMateText(
                    text = date.replace("-", "."),
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray70),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_small_right),
                contentDescription = "",
                tint = if (hasNextRound) LottoMateGray100 else LottoMateGray40,
                modifier = Modifier.noInteractionClickable { if (hasNextRound) onClickNextRound() }
            )
        }

        if (!hasPreRound) {
            LottoMateText(
                text = stringResource(id = if (lotteryType == LottoType.L645) R.string.register_lotto_number_text_round_notice_645 else R.string.register_lotto_number_text_round_notice_720),
                style = LottoMateTheme.typography.caption2
                    .copy(color = LottoMateGray70),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Lotto645SectionPreview() {
    LottoMateTheme {
        val inputNumbers = remember { mutableStateListOf(RegisterLottoNumber.EMPTY) }

        LottoNumberSection(
            lotteryType = LottoType.L645,
            round = 1156,
            date = "2025.11.06",
            inputNumbers = inputNumbers,
            hasPreRound = true,
            hasNextRound = false,
            onAddNewInputNumber = { inputNumbers.add(0, RegisterLottoNumber.EMPTY)},
            onRemoveInputNumber = { index -> inputNumbers.removeAt(index) },
            onChangeInputNumber = { index, number -> inputNumbers[index] = inputNumbers[index].copy(lottoNumbers = number) },
            onChangeReset = { inputNumbers[it] = inputNumbers[it].copy(lottoNumbers = "", isError = false)},
            onClickLottoRoundPicker = {},
            onClickNextRound = {},
            onClickPreRound = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Lotto720SectionPreview() {
    LottoMateTheme {
        val inputNumbers720 = remember { mutableStateListOf(RegisterLottoNumber.EMPTY) }


        LottoNumberSection(
            lotteryType = LottoType.L720,
            round = 256,
            date = "2025.03.27",
            inputNumbers = inputNumbers720,
            hasPreRound = true,
            hasNextRound = false,
            onAddNewInputNumber = { inputNumbers720.add(0, RegisterLottoNumber.EMPTY)},
            onRemoveInputNumber = { index -> inputNumbers720.removeAt(index) },
            onChangeInputNumber = { index, number -> inputNumbers720[index] = inputNumbers720[index].copy(lottoNumbers = number) },
            onChangeReset = { inputNumbers720[it] = inputNumbers720[it].copy(lottoNumbers = "", isError = false)},
            onClickLottoRoundPicker = {},
            onClickPreRound = {},
            onClickNextRound = {},
        )
    }
}

