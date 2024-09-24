package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.pixelsToDp
import com.lottomate.lottomate.presentation.ui.LottoMateGray30
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

    StoreInfoBottomSheetContent(
        modifier = Modifier.fillMaxWidth(),
        bottomSheetHeight = bottomSheetHeight,
    )
}

@Composable
private fun StoreInfoBottomSheetContent(
    modifier: Modifier = Modifier,
    bottomSheetHeight: Float,
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
    }
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