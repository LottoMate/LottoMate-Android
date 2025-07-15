package com.lottomate.lottomate.presentation.screen.pocket.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberContract
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberDetailUiModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberRowUiModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberUiModel
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
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
import com.lottomate.lottomate.utils.noInteractionClickable
import java.util.Calendar

@Composable
fun MyNumberScreen(
    vm: MyNumberViewModel = hiltViewModel(),
    onClickQRScan: () -> Unit,
    onClickSaveNumbers: () -> Unit,
    onClickLottoInfo: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
    onShowGlobalSnackBar: (String) -> Unit,
    moveToLotteryResult: (LottoType, MyLottoInfo) -> Unit,
) {
    val myNumbers by vm.myNumbers.collectAsStateWithLifecycle()
    val isEdit by vm.isEdit.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is MyNumberContract.Effect.ShowSnackBar -> onShowGlobalSnackBar(effect.message)
                is MyNumberContract.Effect.NavigateToLotteryResult -> moveToLotteryResult(effect.type, effect.myLottoInfo)
            }
        }
    }

    MyNumberContent(
        modifier = Modifier.padding(top = 24.dp),
        isEdit = isEdit,
        myNumbers = myNumbers,
        onClickQRScan = onClickQRScan,
        onClickSaveNumbers = onClickSaveNumbers,
        onClickLottoInfo = onClickLottoInfo,
        onClickBanner = onClickBanner,
        onClickRemove = { vm.handleEvent(MyNumberContract.Event.DeleteMyNumber(it)) },
        onClickChangeMode = { vm.handleEvent(MyNumberContract.Event.ChangeMode) },
        onClickCheckWin = { detail, numbers ->
            vm.handleEvent(MyNumberContract.Event.ClickCheckWin(detail, numbers))
        }
    )
}

@Composable
private fun MyNumberContent(
    modifier: Modifier = Modifier,
    isEdit: Boolean,
    myNumbers: MyNumberUiModel,
    onClickQRScan: () -> Unit,
    onClickSaveNumbers: () -> Unit,
    onClickLottoInfo: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
    onClickRemove: (Int) -> Unit,
    onClickChangeMode: () -> Unit,
    onClickCheckWin: (MyNumberDetailUiModel, List<Int>) -> Unit,
) {
    Column(
        modifier = modifier.background(LottoMateWhite),
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        TopCardsSection(
            onClickQRScan = onClickQRScan,
            onClickSaveNumbers = onClickSaveNumbers,
        )

        MyLottoSituationSection(
            onClickLottoInfo = onClickLottoInfo,
        )

        MyLottoHistorySection(
            myNumbers = myNumbers,
            isEdit = isEdit,
            onClickEdit = onClickChangeMode,
            onClickCheckWin = onClickCheckWin,
            onClickBanner = onClickBanner,
            onClickRemove = onClickRemove,
        )
    }
}

@Composable
private fun TopCardsSection(
    onClickQRScan: () -> Unit,
    onClickSaveNumbers: () -> Unit,
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
}

@Composable
private fun MyLottoHistorySection(
    modifier: Modifier = Modifier,
    myNumbers: MyNumberUiModel,
    isEdit: Boolean,
    onClickEdit: () -> Unit,
    onClickCheckWin: (MyNumberDetailUiModel, List<Int>) -> Unit,
    onClickBanner: (BannerType) -> Unit,
    onClickRemove: (Int) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        LottoMateText(
            text = stringResource(id = R.string.pocket_title_my_number_history),
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20)
        )

        if (myNumbers.myNumberDetails.isEmpty()) {
            MyLottoHistoryEmpty(
                modifier = Modifier.padding(top = 12.dp),
                onClickBanner = onClickBanner,
            )
        } else {
            MyLottoHistory(
                myNumbers = myNumbers,
                isEdit = isEdit,
                onClickEdit = onClickEdit,
                onClickCheckWin = onClickCheckWin,
                onClickRemove = onClickRemove,
            )
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
    myNumbers: MyNumberUiModel,
    isEdit: Boolean,
    onClickEdit: () -> Unit,
    onClickCheckWin: (MyNumberDetailUiModel, List<Int>) -> Unit,
    onClickRemove: (Int) -> Unit,
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
                val (year, month, day) = myNumbers.firstPurchaseDate.split(".").map { it.toInt() }
                val message = pluralStringResource(id = R.plurals.pocket_text_my_number_history, 1, year, month, myNumbers.totalCount)

                val totalCountStartIndex = message.indexOf(myNumbers.totalCount.toString())
                val totalCountEndIndex = totalCountStartIndex.plus(myNumbers.totalCount.toString().length).plus(1)
                val annotatedMessage = AnnotatedString.Builder(message).apply {
                    addStyle(SpanStyle(fontWeight = FontWeight.Bold), totalCountStartIndex, totalCountEndIndex)
                }.toAnnotatedString()

                LottoMateAnnotatedText(
                    annotatedString = annotatedMessage,
                    style = LottoMateTheme.typography.body1,
                    modifier = Modifier,
                )

                val rateMessage = pluralStringResource(id = R.plurals.pocket_text_my_number_history_rate, 1, myNumbers.winCount, myNumbers.loseCount, myNumbers.getWinRate())

                val rateStartIndex = rateMessage.indexOf(myNumbers.getWinRate())
                val rateEndIndex = rateStartIndex.plus(myNumbers.getWinRate().length).plus(1)
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            LottoMateText(
                text = stringResource(id = if (isEdit) R.string.pocket_text_my_number_history_complete else R.string.pocket_text_my_number_history_edit),
                textAlign = TextAlign.End,
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray80),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onClickEdit() },
            )

            PaginatedGroupedHistoryColumn(
                isEdit = isEdit,
                myNumberDetails = myNumbers.myNumberDetails,
                onClickCheckWin = onClickCheckWin,
                onClickRemove = onClickRemove,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun PaginatedGroupedHistoryColumn(
    isEdit: Boolean,
    myNumberDetails: List<MyNumberDetailUiModel>,
    onClickCheckWin: (MyNumberDetailUiModel, List<Int>) -> Unit,
    onClickRemove: (Int) -> Unit,
) {
    val initialCount = 5
    var itemsToShow by remember { mutableIntStateOf(initialCount) }
    val totalRows = remember(myNumberDetails) {
        myNumberDetails.sumOf { it.numberRows.size }
    }

    // 회차별로 남은 itemsToShow 만큼만 잘라낸 리스트
    val truncatedGroups: List<MyNumberDetailUiModel> = remember(myNumberDetails, itemsToShow) {
        var remain = itemsToShow

        myNumberDetails.mapNotNull { detail ->
            if (remain <= 0) return@mapNotNull null

            val taken = detail.numberRows.take(remain)
            if (taken.isEmpty()) return@mapNotNull null
            remain -= taken.size
            detail.copy(numberRows = taken)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1) 잘라낸 회차별 항목 렌더링
        truncatedGroups.forEach { detail ->
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MyLottoHistoryRowItemTitle(
                    round = detail.round,
                    date = detail.date
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    detail.numberRows.forEach { row ->
                        MyLottoHistoryRowItem(
                            type = detail.type,
                            myNumber = row,
                            isEdit = isEdit,
                            onClickDelete = { onClickRemove(row.id) },
                            onClickCheckWin = { onClickCheckWin(detail, row.numbers)},
                        )
                    }
                }
            }
        }

        if (totalRows > initialCount) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        itemsToShow = if (itemsToShow < totalRows) (itemsToShow + 10).coerceAtMost(totalRows)
                        else initialCount
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = if (itemsToShow < totalRows) stringResource(id = R.string.common_extend)
                    else stringResource(id = R.string.common_collapse),
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray100),
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = if (itemsToShow < totalRows) painterResource(id = R.drawable.icon_arrow_down)
                    else painterResource(id = R.drawable.icon_arrow_up),
                    contentDescription = stringResource(id = R.string.desc_common_extend_or_collapse_icon),
                    tint = LottoMateGray100,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun MyLottoHistoryRowItem(
    type: LottoType,
    myNumber: MyNumberRowUiModel,
    isEdit: Boolean,
    onClickCheckWin: () -> Unit,
    onClickDelete: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(
                    id = when (type) {
                        LottoType.L645 -> R.drawable.icon_lotto645_rank_first
                        LottoType.L720 -> R.drawable.icon_lotto720_rank_first
                        else -> R.drawable.icon_speetto_rank_first
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(if (type == LottoType.L645) 8.dp else 6.dp),
            ) {
                myNumber.numbers.forEachIndexed { index, number ->
                    when (type) {
                        LottoType.L645 -> {
                            LottoBall645(
                                number = number,
                                size = 28.dp,
                            )
                        }

                        LottoType.L720 -> {
                            LottoBall720(
                                index = index+1,
                                number = number,
                                isBonusNumber = false,
                                size = 28.dp,
                            )
                        }
                        else -> {}
                    }
                }
            }
        }

        if (isEdit) {
            Icon(
                painter = painterResource(R.drawable.icon_trash),
                contentDescription = "My Number Delete",
                tint = LottoMateGray100,
                modifier = Modifier
                    .size(24.dp)
                    .noInteractionClickable { onClickDelete() },
            )
        } else {
            when (myNumber.condition) {
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
}

@Composable
private fun MyLottoHistoryRowItemTitle(
    round: Int,
    date: String,
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        LottoMateText(
            text = round.toString().plus(stringResource(id = R.string.pocket_text_my_number_history_round)),
            style = LottoMateTheme.typography.label1
                .copy(color = LottoMateBlack),
        )

        LottoMateText(
            text = date,
            style = LottoMateTheme.typography.caption
                .copy(color = LottoMateGray80),
        )
    }
}

@Composable
private fun MyLottoSituationSection(
    onClickLottoInfo: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.DefaultPadding20),
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
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
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_lotto720_rank_first),
                        contentDescription = null,
                    )

                    val isToday = DateUtils.getDaysUntilNextDay(Calendar.THURSDAY) == 0

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
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .align(Alignment.End)
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

@Preview(showBackground = true, widthDp = 375, heightDp = 1200)
@Composable
private fun MyNumberScreenPreview() {
    LottoMateTheme {
//        MyNumberContent(
//            myNumbers = MyNumberUiModel(
//                myNumberDetails = listOf(
//                    MyNumberDetailUiModel(
//                        type = LottoType.L645,
//                        round = 1,
//                        date = "2024.01.01",
//                        numberRows = List(5) {
//                            MyNumberRowUiModel(
//                                numbers = listOf(1, 2, 3, 4, 5, 6),
//                                isWin = true,
//                                condition = LottoCondition.CHECKED_WIN
//                            )
//                        }
//                    )
//                )
//            ),
//            onClickQRScan = {},
//            onClickSaveNumbers = {},
//            onClickLottoInfo = {},
//            onClickBanner = {},
//        )

        MyLottoHistoryRowItem(
            type = LottoType.L645,
            myNumber = MyNumberRowUiModel(
                id = 1,
                numbers = listOf(1, 2, 3, 4, 5, 6),
                isWin = false,
                condition = LottoCondition.NOT_CHECKED
            ),
            isEdit = false,
            onClickCheckWin = {}
        )
    }
}