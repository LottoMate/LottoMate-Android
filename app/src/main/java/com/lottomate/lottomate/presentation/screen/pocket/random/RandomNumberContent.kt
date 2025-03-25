package com.lottomate.lottomate.presentation.screen.pocket.random

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateAnnotatedText
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
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
        modifier = modifier.background(LottoMateWhite),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                LottoMateText(
                    text = stringResource(id = R.string.pocket_text_sub_random_number),
                    style = LottoMateTheme.typography.title3
                        .copy(color = LottoMateGray100),
                )

                when {
                    drewRandomNumbers.isEmpty() -> {
                        LottoMateText(
                            text = stringResource(id = R.string.pocket_text_random_number),
                            style = LottoMateTheme.typography.title2,
                        )
                    }
                    else -> {
                        val drawCount = if (drewRandomNumbers.size > 99) "99+" else drewRandomNumbers.size.toString()

                        val message = pluralStringResource(id = R.plurals.pocket_text_random_number_not_empty, 1, drawCount)

                        val startIndex = message.indexOf(drawCount)
                        val endIndex = startIndex.plus(drawCount.length).plus(1)
                        val annotatedRateMessage = AnnotatedString.Builder(message).apply {
                            addStyle(SpanStyle(fontWeight = FontWeight.Bold), startIndex, endIndex)
                        }.toAnnotatedString()

                        LottoMateAnnotatedText(
                            annotatedString = annotatedRateMessage,
                            style = LottoMateTheme.typography.title2,
                        )
                    }
                }

                LottoMateText(
                    text = stringResource(id = R.string.pocket_text_random_number_caption),
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray100),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.icon_file),
                contentDescription = stringResource(id = R.string.desc_pocket_storage_icon),
                tint = LottoMateGray100,
                modifier = Modifier
                    .size(24.dp)
                    .noInteractionClickable { onClickStorageOfRandomNumbers() }
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 36.dp)
                .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_pocket_random_number),
                contentDescription = stringResource(id = R.string.desc_pocket_draw_random_lotto_image),
            )

            LottoMateSolidButton(
                text = stringResource(id = R.string.pocket_btn_random_number),
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                onClick = onClickDrawRandomNumbers,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .padding(horizontal = Dimens.DefaultPadding20)
                    .align(alignment = Alignment.BottomCenter)
            )
        }

        BottomDrawNumbers(
            modifier = Modifier.padding(top = 20.dp),
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
    var isInitialShow by remember { mutableStateOf(true) }
    val visibleNumbers = numbers.take(ballsToShow)

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            LottoMateText(
                text = stringResource(id = R.string.pocket_title_today_draw_lotto),
                style = LottoMateTheme.typography.headline1
                    .copy(color = LottoMateBlack),
            )

            LottoMateText(
                text = stringResource(id = R.string.pocket_title_sub_today_draw_lotto),
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray80),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when(numbers.isEmpty()) {
            true -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LottoMateGray10),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 44.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pocket_venus),
                            contentDescription = stringResource(id = R.string.desc_pocket_empty_image),
                            modifier = Modifier.size(100.dp),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LottoMateText(
                            text = stringResource(id = R.string.pocket_text_empty_draw_lotto),
                            style = LottoMateTheme.typography.body2
                                .copy(color = LottoMateGray100),
                        )
                    }
                }
            }
            false -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
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

                    // 더보기 버튼
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
                                text = if (ballsToShow < numbers.size) stringResource(id = R.string.common_extend)
                                else stringResource(id = R.string.common_collapse),
                                style = LottoMateTheme.typography.caption
                                    .copy(color = LottoMateGray100),
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                painter = if (ballsToShow < numbers.size) painterResource(id = R.drawable.icon_arrow_down)
                                else painterResource(id = R.drawable.icon_arrow_up),
                                contentDescription = stringResource(id = R.string.desc_common_extend_or_collapse_icon),
                                tint = LottoMateGray100,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
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

        Icon(
            painter = painterResource(id = R.drawable.icon_copy),
            contentDescription = stringResource(id = R.string.desc_common_copy_icon),
            tint = LottoMateGray100,
            modifier = Modifier
                .size(24.dp)
                .noInteractionClickable { onClickCopyRandomNumbers() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_save),
            contentDescription = stringResource(id = R.string.desc_common_save_icon),
            tint = LottoMateGray100,
            modifier = Modifier
                .size(24.dp)
                .noInteractionClickable { onClickSaveRandomNumbers() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RandomNumberContentPreview() {
    LottoMateTheme {
        Column {
            RandomNumberContent(
            drewRandomNumbers = emptyList(),
                onClickDrawRandomNumbers = {},
                onClickStorageOfRandomNumbers = {},
                onClickCopyRandomNumbers = {},
                onClickSaveRandomNumbers = {},
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun RandomNumberNotEmptyContentPreview() {
    LottoMateTheme {
        Column {
            RandomNumberContent(
                drewRandomNumbers = List(100) {  listOf(1, 1, 1, 1, 1, 1)},
                onClickDrawRandomNumbers = {},
                onClickStorageOfRandomNumbers = {},
                onClickCopyRandomNumbers = {},
                onClickSaveRandomNumbers = {},
            )
        }
    }
}