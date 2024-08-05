package com.lottomate.lottomate.presentation.screen.lottoinfo.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.screen.lottoinfo.PickerState
import com.lottomate.lottomate.presentation.screen.lottoinfo.rememberPickerState
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.DateUtils.calLottoRoundDate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LottoRoundWheelPicker(
    modifier: Modifier = Modifier,
    initialRound: Int,
    scaffoldState: BottomSheetScaffoldState,
    pickerState: PickerState,
    onSelectRound: () -> Unit,
) {
    // TODO : ViewModel로 옮기기
    val lastRound = 1131
    val lastDate = "2024-08-03"

    val roundRange = (1..lastRound+1).map { round ->
        if (round > lastRound) ""
        else round.toString()
    }.toList().reversed()

    val visibleItemCount = 3
    val visibleItemsMiddle = visibleItemCount / 2

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = lastRound - initialRound
    )
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState)

    var itemHeightPixel by remember { mutableIntStateOf(0) }
    val itemHeightToDp = pixelsToDp(pixels = itemHeightPixel)

    fun getItem(index: Int) = roundRange[index % roundRange.size]

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item ->
                pickerState.selectedItem = item
            }
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
                items(roundRange.size) { index ->
                    Text(
                        text = if (index == 0) {
                            getItem(index)
                        } else {
                            getItem(index)
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
                onSelectRound()

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LottoRoundWheelPickerPreview() {
    LottoMateTheme {
        LottoRoundWheelPicker(
            initialRound = 1131,
            scaffoldState = rememberBottomSheetScaffoldState(),
            pickerState = rememberPickerState(),
            onSelectRound = {}
        )
    }
}