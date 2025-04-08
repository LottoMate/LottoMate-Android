package com.lottomate.lottomate.presentation.screen.lottoinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoRoundViewModel
import com.lottomate.lottomate.presentation.screen.lottoinfo.PickerState
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.rememberPickerState
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateRed5
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LottoRoundWheelPicker(
    vm: LottoRoundViewModel = hiltViewModel(),
    scaffoldState: BottomSheetScaffoldState,
    currentLottoRound: Int,
    lotteryType: LottoType,
    pickerState: PickerState,
    isRegisterScreen: Boolean = false,
    onClickSelect: () -> Unit,
) {
    LaunchedEffect(lotteryType) {
        vm.setLottoRoundRange(lotteryType, isRegisterScreen)
    }

    val latestLottoInfo by vm.latestLottoInfo.collectAsState(emptyMap())

    val lottoRoundRange by vm.lottoRoundRange

    val latestRoundOrPage = latestLottoInfo[lotteryType.num]?.round ?: 0

    val visibleItemCount = 3
    val visibleItemsMiddle = visibleItemCount / 2

    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = maxOf(0, latestRoundOrPage - currentLottoRound)
    )
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState)

    LaunchedEffect(key1 = currentLottoRound, key2 = latestRoundOrPage, key3 = scaffoldState.bottomSheetState.currentValue) {
        val targetIndex = when (lotteryType) {
            LottoType.S2000 -> maxOf(0, abs(latestRoundOrPage - currentLottoRound))
            else -> maxOf(0, latestRoundOrPage - currentLottoRound)
        }
        scrollState.scrollToItem(targetIndex)
        pickerState.selectedItem = lottoRoundRange.getOrNull(targetIndex + visibleItemsMiddle) ?: LatestRoundInfo(0, "")
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex }
            .map { index -> lottoRoundRange.getOrNull(index + visibleItemsMiddle) ?: LatestRoundInfo(0, "") }
            .distinctUntilChanged()
            .collect { item ->
                pickerState.selectedItem = item
            }
    }

    LottoRoundWheelPickerContent(
        modifier = Modifier.fillMaxWidth(),
        visibleItemCount = visibleItemCount,
        lottoRoundRange = lottoRoundRange,
        currentIndex = lottoRoundRange.indexOf(pickerState.selectedItem),
        currentLottoType = lotteryType,
        scaffoldState = scaffoldState,
        scrollState = scrollState,
        flingBehavior = flingBehavior,
        onClickSelect = onClickSelect
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LottoRoundWheelPickerContent(
    modifier: Modifier = Modifier,
    visibleItemCount: Int,
    lottoRoundRange: List<LatestRoundInfo>,
    currentIndex: Int,
    currentLottoType: LottoType,
    pickerMaxHeight: Dp = 116.dp,
    scaffoldState: BottomSheetScaffoldState,
    scrollState: LazyListState,
    flingBehavior: FlingBehavior,
    onClickSelect: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    var itemHeightPixel by remember { mutableIntStateOf(0) }
    val itemHeightToDp = pixelsToDp(pixels = itemHeightPixel)

    Column(modifier = modifier.background(LottoMateWhite)) {
        Spacer(modifier = Modifier.height(32.dp))

        LottoMateText(
            text = when (currentLottoType) {
                LottoType.L645, LottoType.L720 -> "회차 선택"
                else -> "페이지 선택"
            },
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier.padding(start = 20.dp),
        )

        Spacer(modifier = Modifier.height((23.372).dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = itemHeightToDp - (40.dp - itemHeightToDp) / 2)
                    .background(LottoMateRed5)
                    .height(40.dp)
            )

            LazyColumn(
                state = scrollState,
                flingBehavior = flingBehavior,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeightToDp * visibleItemCount)
            ) {
                items(lottoRoundRange.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(pickerMaxHeight * 0.333f)
                            .padding(horizontal = 20.dp)
                            .onSizeChanged { size -> itemHeightPixel = size.height },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        LottoMateText(
                            text = when (currentLottoType) {
                                LottoType.S2000, LottoType.S1000, LottoType.S500 -> lottoRoundRange[index].round
                                else -> { if (lottoRoundRange[index].round == 0) "" else lottoRoundRange[index].round.toString().plus("회") }
                            }.toString(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = LottoMateTheme.typography.headline1
                                .copy(if (currentIndex == index) LottoMateBlack else LottoMateGray90),
                        )

                        // 날짜 표시
                        lottoRoundRange[index].drawDate.let { date ->
                            Spacer(modifier = Modifier.width(20.dp))

                            LottoMateText(
                                text = if (index == 0) "" else date,
                                style = LottoMateTheme.typography.body1
                                    .copy(if (currentIndex == index) LottoMateBlack else LottoMateGray90),
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height((23.372).dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            LottoMateAssistiveButton(
                text = stringResource(id = R.string.common_cancel),
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                },
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(15.dp))

            LottoMateSolidButton(
                text = stringResource(id = R.string.common_confirm),
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                modifier = Modifier.weight(1f),
                onClick = {
                    onClickSelect()

                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LottoRoundWheelPickerContentPreview() {
    LottoMateTheme {
        val scrollState = rememberLazyListState()
        val pickerState = rememberPickerState()
        val ranges = List(20) { LatestRoundInfo(it, "")}

        LottoRoundWheelPickerContent(
            lottoRoundRange = ranges,
            visibleItemCount = 3,
            scaffoldState = rememberBottomSheetScaffoldState(),
            scrollState = scrollState,
            currentIndex = ranges.indexOf(pickerState.selectedItem),
            currentLottoType = LottoType.L645,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState),
            onClickSelect = {}
        )
    }
}

data class WheelPickerItem(
    val value: Int,
    val label: String? = null,
) {
    companion object {
        val Empty = WheelPickerItem(0)
    }
}