package com.lottomate.lottomate.presentation.screen.lottoinfo.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoRoundViewModel
import com.lottomate.lottomate.presentation.screen.lottoinfo.PickerState
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.DateUtils.calLottoRoundDate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    val lastRound by vm.latestLottoRound.collectAsStateWithLifecycle()
    val lastDate by vm.latestLottoDate.collectAsStateWithLifecycle()
    val lottoRoundRange by vm.lottoRoundRange

    val visibleItemCount = 3
    val visibleItemsMiddle = visibleItemCount / 2

    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = maxOf(0, lastRound - currentLottoRound)
    )
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState)

    LaunchedEffect(key1 = currentLottoRound, key2 = lastRound) {
        val targetIndex = maxOf(0, lastRound - currentLottoRound)
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
        lastDate = lastDate,
        visibleItemCount = visibleItemCount,
        lottoRoundRange = lottoRoundRange,
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
    lastDate: String,
    visibleItemCount: Int,
    lottoRoundRange: List<String>,
    scaffoldState: BottomSheetScaffoldState,
    scrollState: LazyListState,
    flingBehavior: FlingBehavior,
    onClickSelect: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    var itemHeightPixel by remember { mutableIntStateOf(0) }
    val itemHeightToDp = pixelsToDp(pixels = itemHeightPixel)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    Column(modifier = modifier.background(LottoMateWhite)) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = modifier) {
            LazyColumn(
                state = scrollState,
                flingBehavior = flingBehavior,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeightToDp * visibleItemCount)
                    .fadingEdge(fadingEdgeGradient)
            ) {
                items(lottoRoundRange.size) { index ->
                    Text(
                        text = if (index == 0) {
                            lottoRoundRange[index]
                        } else {
                            lottoRoundRange[index]
                                .plus("회")
                                .plus(" (")
                                .plus(calLottoRoundDate(lastDate, index))
                                .plus(")")
                        },
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .onSizeChanged { size -> itemHeightPixel = size.height }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LottoMateSolidButton(
            text = "선택",
            buttonSize = LottoMateButtonProperty.Size.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                onClickSelect()

                coroutineScope.launch {
                    scaffoldState.bottomSheetState.partialExpand()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }