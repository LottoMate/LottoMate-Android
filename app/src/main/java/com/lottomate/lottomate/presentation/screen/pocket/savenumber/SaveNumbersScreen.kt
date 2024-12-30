package com.lottomate.lottomate.presentation.screen.pocket.savenumber

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun SaveNumbersRoute(
    padding: PaddingValues,
) {
    SaveNumbersScreen(
        modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
    )
}

@Composable
private fun SaveNumbersScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
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
            )
        }

        LottoMateTopAppBar(
            titleRes = R.string.save_numbers_title,
            hasNavigation = false,
            actionButtons = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "",
                    modifier = Modifier.noInteractionClickable {  }
                )
            }
        )
    }
}

@Composable
private fun Lotto645Section(
    modifier: Modifier = Modifier,
) {
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
                painter = painterResource(id = R.drawable.icon_arrow_left),
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
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = "",
                tint = LottoMateGray100,
            )
        }

        Lotto645TextField(
            number = numbers,
            onChangeValue = { numbers = it }
        )
    }
}

@Composable
private fun Lotto645TextField(
    modifier: Modifier = Modifier,
    number: String = "",
    onChangeValue: (String) -> Unit = {},
    onClickRemove: () -> Unit = {},
) {
    BasicTextField(
        value = number,
        textStyle = LottoMateTheme.typography.body1,
        onValueChange = {
            val onlyNumberLength = it.replace("-", "")
            if (onlyNumberLength.length % 2 == 0) {
                onChangeValue(it.plus("-"))
            } else onChangeValue(it)
        },
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
                    if (number.isEmpty()) {
                        LottoMateText(
                            text = "숫자 6자리를 입력하세요",
                            style = LottoMateTheme.typography.body1
                                .copy(color = LottoMateGray60),
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    } else {
                        innerTextField()

                        Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    }

                    Divider(
                        color = LottoMateGray60,
                    )
                }

                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.icon_xbox),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 27.dp)
                        .noInteractionClickable { onChangeValue("") },
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SaveNumbersScreenPreview() {
    LottoMateTheme {
        SaveNumbersRoute(
            padding = PaddingValues(0.dp)
        )
    }
}