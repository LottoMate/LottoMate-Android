package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateTextButton
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.pixelsToDp
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray30
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

private const val BOTTOM_SHEET_TOP_SPACER = 78

@Composable
fun StoreInfoBottomSheet(
    bottomSheetTopPadding: Int,
) {
    val bottomSheetTopPaddingToDp = pixelsToDp(pixels = bottomSheetTopPadding)
    val bottomSheetHeight = LocalConfiguration.current.screenHeightDp
        .minus(Dimens.NavigationBarHeight.value)
        .minus(BOTTOM_SHEET_TOP_SPACER)
        .minus(bottomSheetTopPaddingToDp.value)

    var selectFilterIndex by remember { mutableIntStateOf(0) }

    StoreInfoBottomSheetContent(
        modifier = Modifier.fillMaxWidth(),
        bottomSheetHeight = bottomSheetHeight,
        selectFilterIndex = selectFilterIndex,
        onClickFilter = { selectFilterIndex = it }
    )
}

@Composable
private fun StoreInfoBottomSheetContent(
    modifier: Modifier = Modifier,
    bottomSheetHeight: Float,
    selectFilterIndex: Int,
    onClickFilter: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .requiredHeight(bottomSheetHeight.dp)
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(LottoMateGray30, RoundedCornerShape(8.dp))
                .size(width = 40.dp, height = 4.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(28.dp))

        FilterRow(
            modifier = Modifier.fillMaxWidth(),
            filters = stringArrayResource(id = R.array.map_store_info_top_filters),
            selectFilterIndex = selectFilterIndex,
            onClickFilter = onClickFilter,
        )
    }
}

@Composable
private fun FilterRow(
    modifier: Modifier = Modifier,
    filters: Array<String>,
    selectFilterIndex: Int,
    onClickFilter: (Int) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        filters.forEachIndexed { index, filter ->
            FilterItem(
                filter = filter,
                isSelected = selectFilterIndex == index,
                onClickFilter = { onClickFilter(index) }
            )

            if (index != filters.lastIndex) {
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(width = 1.dp, height = 16.dp)
                        .align(Alignment.CenterVertically),
                    color = LottoMateGray20,
                )
            }
        }
    }
}

@Composable
private fun FilterItem(
    modifier: Modifier = Modifier,
    filter: String,
    isSelected: Boolean = false,
    onClickFilter: () -> Unit,
) {
    LottoMateTextButton(
        buttonText = filter,
        buttonSize = LottoMateButtonProperty.Size.SMALL,
        textColor = if (isSelected) LottoMateRed50 else LottoMateGray60,
        modifier = modifier,
        onClick = onClickFilter,
    )
}

@Preview(showBackground = true)
@Composable
private fun StoreInfoBottomSheetPreview() {
    LottoMateTheme {
        StoreInfoBottomSheet(
            bottomSheetTopPadding = 720,
        )
    }
}