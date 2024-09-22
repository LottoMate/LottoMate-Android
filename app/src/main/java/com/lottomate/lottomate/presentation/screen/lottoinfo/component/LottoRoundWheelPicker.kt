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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoBottomWheelUiState
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoRoundViewModel
import com.lottomate.lottomate.presentation.screen.lottoinfo.PickerState
import com.lottomate.lottomate.presentation.screen.lottoinfo.rememberPickerState
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateRed5
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.DateUtils.calLottoRoundDate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LottoRoundWheelPicker(
    vm: LottoRoundViewModel = hiltViewModel(),
    scaffoldState: BottomSheetScaffoldState,
    currentLottoRound: Int,
    currentTabIndex: Int,
    pickerState: PickerState,
    onClickSelect: () -> Unit,
) {
    LaunchedEffect(currentTabIndex) {
        vm.getLatestLottoInfo(currentTabIndex)
    }

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val lottoRoundRange by vm.lottoRoundRange

    when (uiState) {
        LottoBottomWheelUiState.Loading -> { }
        is LottoBottomWheelUiState.Success -> {
            val latestLottoRound = (uiState as LottoBottomWheelUiState.Success).round
            val latestLottoDate = (uiState as LottoBottomWheelUiState.Success).date

            val visibleItemCount = 3
            val visibleItemsMiddle = visibleItemCount / 2

            val scrollState = rememberLazyListState(
                initialFirstVisibleItemIndex = maxOf(0, latestLottoRound - currentLottoRound)
            )
            val flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState)

            LaunchedEffect(key1 = currentLottoRound, key2 = latestLottoRound, key3 = scaffoldState.bottomSheetState.currentValue) {
                val targetIndex = maxOf(0, latestLottoRound - currentLottoRound)
                scrollState.scrollToItem(targetIndex)
                pickerState.selectedItem = lottoRoundRange.getOrNull(targetIndex + visibleItemsMiddle) ?: ""
            }

            LaunchedEffect(scrollState) {
                snapshotFlow { scrollState.firstVisibleItemIndex }
                    .map { index -> lottoRoundRange.getOrNull(index + visibleItemsMiddle) ?: "" }
                    .distinctUntilChanged()
                    .collect { item ->
                        pickerState.selectedItem = item
                    }
            }

            LottoRoundWheelPickerContent(
                modifier = Modifier.fillMaxWidth(),
                lastDate = latestLottoDate,
                visibleItemCount = visibleItemCount,
                lottoRoundRange = lottoRoundRange,
                currentIndex = lottoRoundRange.indexOf(pickerState.selectedItem),
                scaffoldState = scaffoldState,
                scrollState = scrollState,
                flingBehavior = flingBehavior,
                onClickSelect = onClickSelect
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LottoRoundWheelPickerContent(
    modifier: Modifier = Modifier,
    lastDate: String,
    visibleItemCount: Int,
    lottoRoundRange: List<String>,
    currentIndex: Int,
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
            text = "회차 선택",
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
                            text = if (index == 0) lottoRoundRange[index] else lottoRoundRange[index].plus("회"),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = LottoMateTheme.typography.headline1
                                .copy(if (currentIndex == index) LottoMateBlack else LottoMateGray90),
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        LottoMateText(
                            text = if (index == 0) "" else calLottoRoundDate(lastDate, index),
                            style = LottoMateTheme.typography.body1
                                .copy(if (currentIndex == index) LottoMateBlack else LottoMateGray90),
                        )
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
                text = "취소",
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
                text = "확인",
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
        val ranges = List(20) { it.toString() }

        LottoRoundWheelPickerContent(
            lastDate = "2024-03-01",
            lottoRoundRange = ranges,
            visibleItemCount = 3,
            scaffoldState = rememberBottomSheetScaffoldState(),
            scrollState = scrollState,
            currentIndex = ranges.indexOf(pickerState.selectedItem),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState),
            onClickSelect = {}
        )
    }
}