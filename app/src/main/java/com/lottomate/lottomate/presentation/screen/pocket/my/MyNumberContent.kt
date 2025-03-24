package com.lottomate.lottomate.presentation.screen.pocket.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.component.LottoMateAnnotatedText
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateOutLineButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTextButton
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall720
import com.lottomate.lottomate.presentation.screen.pocket.model.LottoCondition
import com.lottomate.lottomate.presentation.screen.pocket.model.LottoDetail
import com.lottomate.lottomate.presentation.screen.pocket.model.mockLottoDetails
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateError
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMatePositive
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.DateUtils
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun MyNumberContent(
    modifier: Modifier = Modifier,
    onClickQRScan: () -> Unit,
    onClickSaveNumbers: () -> Unit,
    onClickLottoInfo: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
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
            LottoMateCard(
                modifier = Modifier.weight(1f),
                onClick = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.DefaultPadding20),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_left),
                        textAlign = TextAlign.Center,
                        style = LottoMateTheme.typography.headline2,
                    )

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_rounge_cony2),
                        contentDescription = stringResource(id = R.string.pocket_desc_my_number_top_left_image),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(70.dp),
                    )

                    LottoMateAssistiveButton(
                        text = stringResource(id = R.string.pocket_text_my_number_scan),
                        buttonSize = LottoMateButtonProperty.Size.SMALL,
                        onClick = onClickQRScan,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(15.dp))

            LottoMateCard(
                modifier = Modifier.weight(1f),
                onClick = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.DefaultPadding20),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_right),
                        textAlign = TextAlign.Center,
                        style = LottoMateTheme.typography.headline2,
                    )

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_rounge_cony),
                        contentDescription = stringResource(id = R.string.pocket_desc_my_number_top_right_image),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(70.dp),
                    )

                    LottoMateAssistiveButton(
                        text = stringResource(id = R.string.pocket_text_my_number_save_number),
                        buttonSize = LottoMateButtonProperty.Size.SMALL,
                        onClick = onClickSaveNumbers,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }

        MyLottoSituationSection(
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = Dimens.DefaultPadding20),
            onClickLottoInfo = onClickLottoInfo,
        )

        Spacer(modifier = Modifier.height(48.dp))

        MyLottoHistorySection(
            onClickEdit = {},
            onClickCheckWin = {},
            onClickBanner = onClickBanner,
        )
    }
}

@Composable
private fun MyLottoHistorySection(
    modifier: Modifier = Modifier,
    myLottoHistories: List<LottoDetail> = emptyList(),
    onClickEdit: () -> Unit,
    onClickCheckWin: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        LottoMateText(
            text = stringResource(id = R.string.pocket_title_my_number_history),
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20)
        )

        when {
            myLottoHistories.isEmpty() -> {
                MyLottoHistory(
                    myLottoHistories = mockLottoDetails,
                    onClickEdit = onClickEdit,
                    onClickCheckWin = onClickCheckWin
                )
            }
            else -> {
                MyLottoHistoryEmpty(
                    modifier = Modifier.padding(top = 12.dp),
                    onClickBanner = onClickBanner,
                )
            }
        }
    }
}

@Composable
private fun MyLottoHistoryEmpty(
    modifier: Modifier = Modifier,
    onClickBanner: (BannerType) -> Unit,
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = LottoMateGray10)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
                    .padding(top = 50.dp, bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pocket_venus),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                )

                LottoMateText(
                    text = stringResource(id = R.string.pocket_title_my_number_history_empty),
                    style = LottoMateTheme.typography.body2
                        .copy(color = LottoMateGray100),
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }

        BannerCard(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(top = 32.dp, bottom = 28.dp),
            type = BannerType.MAP,
            onClickBanner = { onClickBanner(BannerType.MAP) },
        )
    }
}

@Composable
private fun MyLottoHistory(
    modifier: Modifier = Modifier,
    myLottoHistories: List<LottoDetail>,
    onClickEdit: () -> Unit,
    onClickCheckWin: () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .background(color = LottoMateGray10, shape = RoundedCornerShape(Dimens.RadiusLarge))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                // TODO : API 연결 후, 데이터들 동적으로 변경 예정
                val year = 2024
                val month = 8
                val totalCount = 10
                val winCount = 1
                val loseCount = 10
                val winRate = String.format("%2d", ((winCount.toDouble() / winCount.plus(loseCount)) * 100).roundToInt()).plus("%")

                val message = pluralStringResource(id = R.plurals.pocket_text_my_number_history, 1, year, month, totalCount)

                val totalCountStartIndex = message.indexOf(totalCount.toString())
                val totalCountEndIndex = totalCountStartIndex.plus(totalCount.toString().length).plus(1)
                val annotatedMessage = AnnotatedString.Builder(message).apply {
                    addStyle(SpanStyle(fontWeight = FontWeight.Bold), totalCountStartIndex, totalCountEndIndex)
                }.toAnnotatedString()

                LottoMateAnnotatedText(
                    annotatedString = annotatedMessage,
                    style = LottoMateTheme.typography.body1,
                    modifier = Modifier,
                )

                val rateMessage = pluralStringResource(id = R.plurals.pocket_text_my_number_history_rate, 1, winCount, loseCount, winRate)

                val rateStartIndex = rateMessage.indexOf(winRate)
                val rateEndIndex = rateStartIndex.plus(winRate.length).plus(1)
                val annotatedRateMessage = AnnotatedString.Builder(rateMessage).apply {
                    addStyle(SpanStyle(fontWeight = FontWeight.Bold), rateStartIndex, rateEndIndex)
                }.toAnnotatedString()

                LottoMateAnnotatedText(
                    annotatedString = annotatedRateMessage,
                    style = LottoMateTheme.typography.body1,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        val lottoHistoriesByRound = myLottoHistories.groupBy { it.round }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            LottoMateText(
                text = stringResource(id = R.string.pocket_text_my_number_history_edit),
                textAlign = TextAlign.End,
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray80),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onClickEdit() },
            )

            Column {
                lottoHistoriesByRound.forEach { (round, lottoDetails) ->
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp),
                    ) {
                        MyLottoHistoryRowItemTitle(round, lottoDetails)

                        Spacer(modifier = Modifier.height(12.dp))

                        lottoDetails.forEach { detail ->
                            MyLottoHistoryRowItem(
                                detail = detail,
                                onClickCheckWin = onClickCheckWin,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MyLottoHistoryRowItem(
    detail: LottoDetail,
    onClickCheckWin: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(
                    id = when (detail.type) {
                        LottoType.L645 -> R.drawable.icon_lotto645_rank_first
                        LottoType.L720 -> R.drawable.icon_lotto720_rank_first
                        else -> R.drawable.icon_speetto_rank_first
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )

            Spacer(modifier = Modifier.width(4.dp))

            detail.numbers.forEachIndexed { index, number ->
                when (detail.type) {
                    LottoType.L645 -> {
                        LottoBall645(
                            modifier = Modifier.padding(end = 8.dp),
                            number = number,
                            size = 28.dp,
                        )
                    }

                    LottoType.L720 -> {
                        LottoBall720(
                            index = index,
                            number = number,
                            isBonusNumber = false,
                            size = 28.dp,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                    else -> {}
                }
            }
        }

        when (detail.condition) {
            LottoCondition.NOT_WON -> {
                LottoMateTextButton(
                    buttonText = stringResource(id = R.string.pocket_text_my_number_history_failed),
                    buttonSize = LottoMateButtonProperty.Size.SMALL,
                    textColor = LottoMateError,
                    onClick = {},
                )
            }
            LottoCondition.CHECKED_WIN -> {
                LottoMateTextButton(
                    buttonText = stringResource(id = R.string.pocket_text_my_number_history_win),
                    buttonSize = LottoMateButtonProperty.Size.SMALL,
                    textColor = LottoMatePositive,
                    onClick = {},
                )
            }
            LottoCondition.NOT_CHECKED -> {
                LottoMateOutLineButton(
                    text = stringResource(id = R.string.pocket_text_my_number_history_check),
                    buttonSize = LottoMateButtonProperty.Size.XSMALL,
                    buttonShape = LottoMateButtonProperty.Shape.NORMAL_XSMALL,
                    onClick = { onClickCheckWin() },
                )
            }
            LottoCondition.NOT_CHECKED_END -> {
                LottoMateOutLineButton(
                    text = stringResource(id = R.string.pocket_text_my_number_history_check),
                    buttonSize = LottoMateButtonProperty.Size.XSMALL,
                    buttonShape = LottoMateButtonProperty.Shape.NORMAL_XSMALL,
                    buttonBorderColor = LottoMateGray60,
                    textColor = LottoMateGray60,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun MyLottoHistoryRowItemTitle(
    round: Int,
    lottoDetails: List<LottoDetail>,
) {
    Row(
        verticalAlignment = Alignment.Bottom,
    ) {
        LottoMateText(
            text = round.toString().plus(stringResource(id = R.string.pocket_text_my_number_history_round)),
            style = LottoMateTheme.typography.label2,
        )

        LottoMateText(
            text = lottoDetails.first().date,
            style = LottoMateTheme.typography.caption
                .copy(color = LottoMateGray80),
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun MyLottoSituationSection(
    modifier: Modifier = Modifier,
    onClickLottoInfo: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        LottoMateText(
            text = stringResource(id = R.string.pocket_text_my_number_situation),
            style = LottoMateTheme.typography.headline1,
        )

        Row {
            LottoMateCard(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .weight(1f),
                onClick = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp)
                        .padding(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        LottoMateText(
                            text = stringResource(id = R.string.pocket_text_my_number_possible_check),
                            style = LottoMateTheme.typography.headline2,
                        )

                        val availableCount = "0"
                        val message = pluralStringResource(id = R.plurals.pocket_text_my_number_situation_my_lotto, 1, availableCount)

                        val startIndex = 5
                        val endIndex = startIndex.plus(availableCount.length)
                        val annotatedMessage = AnnotatedString.Builder(message).apply {
                            addStyle(SpanStyle(color = LottoMateRed50), startIndex, endIndex)
                        }.toAnnotatedString()

                        LottoMateAnnotatedText(
                            annotatedString = annotatedMessage,
                            style = LottoMateTheme.typography.label2,
                        )
                    }

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_pocket_my_number_available),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(15.dp))

            LottoMateCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                onClick = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp)
                        .padding(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        LottoMateText(
                            text = stringResource(id = R.string.pocket_text_my_number_before_draw),
                            style = LottoMateTheme.typography.headline2,
                        )

                        val beforeCount = "0"
                        val message = pluralStringResource(id = R.plurals.pocket_text_my_number_situation_my_lotto, 1, beforeCount)

                        val startIndex = 5
                        val endIndex = startIndex.plus(beforeCount.length)
                        val annotatedMessage = AnnotatedString.Builder(message).apply {
                            addStyle(SpanStyle(color = LottoMateRed50), startIndex, endIndex)
                        }.toAnnotatedString()

                        LottoMateAnnotatedText(
                            annotatedString = annotatedMessage,
                            style = LottoMateTheme.typography.label2,
                        )
                    }

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_pocket_my_number_before),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(color = LottoMateGray10, shape = RoundedCornerShape(Dimens.RadiusLarge))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_lotto645_rank_first),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )

                    val isToday = DateUtils.getDaysUntilNextDay(Calendar.SATURDAY) == 0

                    val annotatedMessage = when(isToday) {
                        true -> {
                            val keyword = stringResource(id = R.string.pocket_text_my_number_notice_keyword)
                            val message = stringResource(id = R.string.pocket_text_my_number_notice_645_today)

                            val startIndex = message.indexOf(keyword)
                            val endIndex = startIndex + keyword.length
                            AnnotatedString.Builder(message).apply {
                                addStyle(SpanStyle(color = LottoMateRed50, fontWeight = FontWeight.Bold), startIndex, endIndex)
                            }.toAnnotatedString()
                        }
                        false -> {
                            val days = DateUtils.getDaysUntilNextDay(Calendar.SATURDAY)
                            val message = pluralStringResource(id = R.plurals.pocket_text_my_number_notice_645, 1, days)

                            val startIndex = 11
                            val endIndex = startIndex.plus(2)
                            AnnotatedString.Builder(message).apply {
                                addStyle(SpanStyle(
                                    color = when (days) {
                                        in 1..3 -> LottoMateBlue50
                                        else -> LottoMateBlack
                                    },
                                    fontWeight = FontWeight.Bold
                                ), startIndex, endIndex)
                            }.toAnnotatedString()
                        }
                    }

                    LottoMateAnnotatedText(
                        annotatedString = annotatedMessage,
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_lotto720_rank_first),
                        contentDescription = null,
                    )

                    val isToday = DateUtils.getDaysUntilNextDay(Calendar.TUESDAY) == 0

                    val annotatedMessage = when(isToday) {
                        true -> {
                            val keyword = stringResource(id = R.string.pocket_text_my_number_notice_keyword)
                            val message = stringResource(id = R.string.pocket_text_my_number_notice_720_today)

                            val startIndex = message.indexOf(keyword)
                            val endIndex = startIndex + keyword.length
                            AnnotatedString.Builder(message).apply {
                                addStyle(SpanStyle(color = LottoMateRed50, fontWeight = FontWeight.Bold), startIndex, endIndex)
                            }.toAnnotatedString()
                        }
                        false -> {
                            val days = DateUtils.getDaysUntilNextDay(Calendar.THURSDAY)
                            val message = pluralStringResource(id = R.plurals.pocket_text_my_number_notice_720, 1, days)

                            val startIndex = 13
                            val endIndex = startIndex.plus(2)
                            AnnotatedString.Builder(message).apply {
                                addStyle(SpanStyle(
                                    color = when (days) {
                                        in 1..3 -> LottoMateBlue50
                                        else -> LottoMateBlack
                                    },
                                    fontWeight = FontWeight.Bold
                                ), startIndex, endIndex)
                            }.toAnnotatedString()
                        }
                    }

                    LottoMateAnnotatedText(
                        annotatedString = annotatedMessage,
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .clickable { onClickLottoInfo() },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LottoMateText(
                text = "역대 당첨금 확인하기",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray100),
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = LottoMateGray100,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyNumberContentPreview() {
    LottoMateTheme {
        MyNumberContent(
            onClickQRScan = {},
            onClickSaveNumbers = {},
            onClickLottoInfo = {},
            onClickBanner = {},
        )
    }
}