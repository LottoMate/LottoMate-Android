package com.lottomate.lottomate.presentation.screen.pocket.savenumber

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.rememberPickerState
import com.lottomate.lottomate.presentation.screen.pocket.savenumber.component.Lotto720Section
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateError
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun SaveNumbersRoute(
    padding: PaddingValues,
    onBackPressed: () -> Unit,
) {
//    val bottomPadding = WindowInsets.navigationBars.asPaddingValues()

    SaveNumbersScreen(
//        modifier = Modifier.padding(bottom = bottomPadding.calculateBottomPadding()),
        onClickClose = onBackPressed,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveNumbersScreen(
    modifier: Modifier = Modifier,
    onClickClose: () -> Unit,
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues()
    var showLottoRoundPicker by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val pickerState = rememberPickerState()

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LottoMateWhite,
        scaffoldState = scaffoldState,
        sheetContainerColor = LottoMateWhite,
        sheetPeekHeight = 0.dp,
        sheetContent = {
//            LottoRoundWheelPicker(
//                currentLottoRound = 1126,
//                currentTabIndex = 0,
//                scaffoldState = scaffoldState,
//                pickerState = pickerState,
//                onClickSelect = {},
//            )
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding.calculateBottomPadding())
                .background(LottoMateWhite),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(Dimens.BaseTopPadding))

                LottoMateText(
                    text = "내 복권 번호를 저장하세요",
                    style = LottoMateTheme.typography.title3
                        .copy(color = LottoMateBlack),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = Dimens.DefaultPadding20),
                )

                LottoMateText(
                    text = "로또는 0까지 같이 입력해주세요 ex) 01, 05...",
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray80),
                    modifier = Modifier
                        .padding(horizontal = Dimens.DefaultPadding20),
                )

                Lotto645Section(
                    modifier = Modifier.padding(top = 40.dp),
                    onClickLottoRoundPicker = { showLottoRoundPicker = true }
                )

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
                    .background(color = LottoMateGray20)
                    .height(10.dp)
                )

                Lotto720Section()
            }

            LottoMateTopAppBar(
                titleRes = R.string.save_numbers_title,
                hasNavigation = false,
                actionButtons = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        modifier = Modifier.noInteractionClickable { onClickClose() }
                    )
                }
            )

            LottoMateSolidButton(
                text = "저장",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = Dimens.DefaultPadding20)
                    .padding(bottom = 36.dp),
            )
        }
    }
}

@Composable
private fun Lotto645Section(
    modifier: Modifier = Modifier,
    onClickLottoRoundPicker: () -> Unit,
) {
    var inputNumbers = remember { mutableStateListOf<String>("") }

    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .padding(horizontal = Dimens.DefaultPadding20),
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_lotto645_rank_first),
                contentDescription = ""
            )

            LottoMateText(
                text = "로또",
                style = LottoMateTheme.typography.headline1,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_small_left),
                contentDescription = "",
                tint = LottoMateGray100,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.noInteractionClickable { onClickLottoRoundPicker() }
            ) {
                LottoMateText(
                    text = "1126회",
                    style = LottoMateTheme.typography.headline1,
                )

                LottoMateText(
                    text = "2024.06.29",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray70),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_small_right),
                contentDescription = "",
                tint = LottoMateGray100,
            )
        }

        for (i in 0..< inputNumbers.size) {
            Lotto645TextField(
                number = inputNumbers[i],
                onChangeValue = {
                    if (i < inputNumbers.size) {
                        inputNumbers[i] = it
                    }

                },
                onClickRemove = {
                    if (inputNumbers.size == 1) {
                        inputNumbers[i] = ""
                    } else {
                        inputNumbers.removeAt(i)
                    }
                },
            )
        }

        if (inputNumbers.size < 5) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .noInteractionClickable { inputNumbers.add("") },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_plus),
                    contentDescription = "",
                    tint = LottoMateGray40,
                    modifier = Modifier.size(14.dp)
                )

                LottoMateText(
                    text = "번호 추가하기",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray40),
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun Lotto645TextField(
    modifier: Modifier = Modifier,
    number: String = "",
    onChangeValue: (String) -> Unit = {},
    onClickRemove: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    var isError by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier.padding(bottom = 24.dp),
        value = number,
        textStyle = LottoMateTheme.typography.body1,
        onValueChange = { inputNum ->
            val splitNumbers = inputNum.split("-")

            when {
                splitNumbers.sumOf { it.length } > 12 || inputNum.length > 17 -> {
                    onChangeValue(inputNum.substring(0, 17))
                }
                splitNumbers.map { it.length }.any { it > 2 } -> {
                    isError = true
                    onChangeValue(inputNum)
                }
                splitNumbers.map { it.toInt() }.any { it > 45 } -> {
                    isError = true
                    onChangeValue(inputNum)
                }
                else -> {
                    isError = false
                    onChangeValue(inputNum)
                }
            }



        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
//        visualTransformation = LottoNumberTransformation(),
        decorationBox = { innerTextField ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.DefaultPadding20),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (number.isEmpty()) {
                                LottoMateText(
                                    text = "숫자 6자리를 입력하세요",
                                    style = LottoMateTheme.typography.body1
                                        .copy(color = LottoMateGray60),
                                    modifier = Modifier.padding(bottom = 8.dp),
                                )
                            } else {
                                LottoMateText(
                                    text = number,
                                    style = LottoMateTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                )
                                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                            }
                        }

                        Divider(
                            color = if (isError) LottoMateError else LottoMateGray60,
                        )
                    }

                    Image(
                        bitmap = ImageBitmap.imageResource(
                            id = if (number.isNullOrEmpty()) R.drawable.icon_xbox_gray20 else R.drawable.icon_xbox_gray60
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 27.dp)
                            .noInteractionClickable { onClickRemove() },
                    )
                }

                if (isError) {
                    LottoMateText(
                        text = "숫자를 다시 확인해주세요",
                        style = LottoMateTheme.typography.label2
                            .copy(color = LottoMateError),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .padding(horizontal = Dimens.DefaultPadding20),
                    )
                }
            }
        }
    )
}

class LottoNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text
        val builder = StringBuilder()

        input.forEachIndexed { index, char ->
            builder.append(char)
            if ((index + 1) % 2 == 0 && index < input.length - 1) {
                builder.append("-")
            }
        }
        return TransformedText(
            AnnotatedString(builder.toString()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return input.substring(0, offset).count { it.isDigit() || it == '-' }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return input.substring(0, offset).count { it.isDigit() }
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun SaveNumbersScreenPreview() {
    LottoMateTheme {
        SaveNumbersRoute(
            padding = PaddingValues(0.dp),
            onBackPressed = {},
        )
    }
}