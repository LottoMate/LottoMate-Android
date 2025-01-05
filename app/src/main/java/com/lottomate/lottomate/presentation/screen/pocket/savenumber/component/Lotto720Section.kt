package com.lottomate.lottomate.presentation.screen.pocket.savenumber.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.pocket.savenumber.LottoNumberTransformation
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
internal fun Lotto720Section(
    modifier: Modifier = Modifier,
) {
    var inputNumbers = remember {
        mutableStateListOf(emptyList<String>())
    }
    var numbers by remember {
        mutableStateOf("")
    }
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
                painter = painterResource(id = R.drawable.icon_lotto720_rank_first),
                contentDescription = ""
            )

            LottoMateText(
                text = "연금복권",
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
            Lotto720TextField(
                number = inputNumbers[i].joinToString(),
                onChangeValue = {
                    inputNumbers[i] = inputNumbers[i].map { it }
                },
                onClickRemove = { inputNumbers.removeAt(i) },
            )
        }

        if (inputNumbers.size < 5) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .noInteractionClickable { inputNumbers.add(emptyList()) },
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
private fun Lotto720TextField(
    modifier: Modifier = Modifier,
    number: String = "",
    onChangeValue: (String) -> Unit = {},
    onClickRemove: () -> Unit = {},
) {
    BasicTextField(
        modifier = modifier.padding(bottom = 24.dp),
        value = number,
        textStyle = LottoMateTheme.typography.body1,
        onValueChange = {
//            val onlyNumberLength = it.replace("-", "")
//            if (onlyNumberLength.length % 2 == 0) {
//                onChangeValue(it.plus("-"))
//            } else onChangeValue(it)
//                        onChangeValue(it)
//            input =
            val filteredString = it.filter { char -> char.isDigit() }.take(10)
            val digitsOnly = it.filter { char -> char.isDigit() }
            if (digitsOnly.length <= 10) {
                onChangeValue(digitsOnly)
            }
//                        onChangeValue(filteredString)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        visualTransformation = LottoNumberTransformation(),
        decorationBox = { innerTextField ->
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
                        modifier = Modifier
                        ,
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
                            LottoMateText(text = number)
                            Spacer(modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }

                    Divider(
                        color = LottoMateGray60,
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
        }
    )
}