package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTextButton
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.pixelsToDp
import com.lottomate.lottomate.presentation.screen.map.StoreBottomSheetViewModel
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMock
import com.lottomate.lottomate.presentation.screen.map.model.WinningDetail
import com.lottomate.lottomate.presentation.ui.LottoMateBlue5
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray30
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateGreen5
import com.lottomate.lottomate.presentation.ui.LottoMateGreen70
import com.lottomate.lottomate.presentation.ui.LottoMatePeach5
import com.lottomate.lottomate.presentation.ui.LottoMatePeach50
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.utils.noInteractionClickable

private const val BOTTOM_SHEET_TOP_SPACER = 78

@Composable
fun StoreInfoBottomSheet(
    vm: StoreBottomSheetViewModel = hiltViewModel(),
    bottomSheetTopPadding: Int,
) {
    val bottomSheetTopPaddingToDp = pixelsToDp(pixels = bottomSheetTopPadding)
    val bottomSheetHeight = LocalConfiguration.current.screenHeightDp
        .minus(Dimens.StatusBarHeight.value)
        .minus(BOTTOM_SHEET_TOP_SPACER)
        .minus(bottomSheetTopPaddingToDp.value)

    val stores by vm.stores.collectAsStateWithLifecycle()
    val store = vm.store.value

    var selectFilterIndex by remember { mutableIntStateOf(0) }

    StoreInfoBottomSheetContent(
        modifier = Modifier.fillMaxWidth(),
        stores = stores,
        selectedStore = store,
        selectFilterIndex = selectFilterIndex,
        bottomSheetHeight = bottomSheetHeight,
        onClickStore = { vm.selectStore(it) },
        onClickFilter = { selectFilterIndex = it }
    )
}

@Composable
private fun StoreInfoBottomSheetContent(
    modifier: Modifier = Modifier,
    stores: List<StoreInfo>,
    selectedStore: StoreInfo?,
    selectFilterIndex: Int,
    bottomSheetHeight: Float,
    onClickStore: (StoreInfo) -> Unit,
    onClickFilter: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .requiredHeight(bottomSheetHeight.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(LottoMateGray30, RoundedCornerShape(8.dp))
                .size(width = 40.dp, height = 4.dp)
                .align(Alignment.CenterHorizontally)
        )

        selectedStore?.let { store ->
            SelectStoreInfoContent(
                modifier = Modifier.fillMaxWidth(),
                store = store,
            )
        } ?: run {
            StoreInfoListContent(
                modifier = Modifier.fillMaxWidth(),
                stores = stores,
                selectFilterIndex = selectFilterIndex,
                onClickStore = onClickStore,
                onClickFilter = onClickFilter,
            )
        }

    }
}

@Composable
private fun SelectStoreInfoContent(
    modifier: Modifier = Modifier,
    store: StoreInfo,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        StoreInfoListItem(
            store = store,
        )
    }
}

@Composable
private fun StoreInfoListContent(
    modifier: Modifier = Modifier,
    stores: List<StoreInfo>,
    selectFilterIndex: Int,
    onClickStore: (StoreInfo) -> Unit,
    onClickFilter: (Int) -> Unit
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))

        FilterRow(
            modifier = Modifier.fillMaxWidth(),
            filters = stringArrayResource(id = R.array.map_store_info_top_filters),
            selectFilterIndex = selectFilterIndex,
            onClickFilter = onClickFilter,
        )

        Spacer(modifier = Modifier.height(24.dp))

        stores.forEachIndexed { index, store ->
            if (index != 0) Spacer(modifier = Modifier.height(20.dp))

            StoreInfoListItem(
                store = store,
                onClickStore = onClickStore,
            )

            if (index != stores.lastIndex) {
                Divider(
                    color = LottoMateGray20,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun StoreInfoListItem(
    modifier: Modifier = Modifier,
    store: StoreInfo,
    onClickStore: (StoreInfo) -> Unit = {},
) {
    var storeNameLineCount by remember { mutableIntStateOf(1) }
    var expendStoreWinHistory by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(bottom = 20.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row {
                store.hasLottoType.forEach { type ->
                    val containerColor = when (type) {
                        LottoTypeFilter.Lotto645.kr -> LottoMateGreen5
                        LottoTypeFilter.Lotto720.kr -> LottoMateBlue5
                        LottoTypeFilter.Speetto.kr -> LottoMatePeach5
                        else -> Color.Unspecified
                    }

                    val textColor = when (type) {
                        LottoTypeFilter.Lotto645.kr -> LottoMateGreen70
                        LottoTypeFilter.Lotto720.kr -> LottoMateBlue50
                        LottoTypeFilter.Speetto.kr -> LottoMatePeach50
                        else -> Color.Unspecified
                    }

                    Box(
                        modifier = modifier
                            .background(containerColor, RoundedCornerShape(Dimens.RadiusExtraSmall))
                    ) {
                        LottoMateText(
                            text = type,
                            style = LottoMateTheme.typography.caption1
                                .copy(color = textColor),
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.wrapContentHeight()
                ) {
                    LottoMateText(
                        text = store.storeName,
                        style = LottoMateTheme.typography.headline1,
                        onTextLayout = {
                            storeNameLineCount = it.lineCount
                        },
                        modifier = Modifier
                            .alignByBaseline()
                            .noInteractionClickable { onClickStore(store) },
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    LottoMateText(
                        text = "${store.distance}km",
                        style = LottoMateTheme.typography.caption1
                            .copy(color = LottoMateGray80),
                        modifier = Modifier.alignByBaseline(),
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = if (store.isLike) painterResource(id = R.drawable.icon_like)
                        else painterResource(id = R.drawable.icon_unlike),
                        contentDescription = "The Favorite Store Icon",
                        tint = LottoMateGray80,
                    )

                    LottoMateText(
                        text = store.countLike.toString(),
                        style = LottoMateTheme.typography.caption2
                            .copy(color = LottoMateGray80),
                    )
                }
            }

//            if (storeNameLineCount == 2) { Spacer(modifier = Modifier.height(4.dp)) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_place),
                    contentDescription = "",
                    tint = LottoMateGray100,
                    modifier = Modifier.size(12.dp),
                )

                Spacer(modifier = Modifier.width(4.dp))

                LottoMateText(
                    text = store.address,
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray100)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_call),
                    contentDescription = "",
                    tint = LottoMateGray100,
                    modifier = Modifier.size(12.dp),
                )

                Spacer(modifier = Modifier.width(4.dp))

                LottoMateText(
                    text = store.phone,
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray100)
                )
            }

            if (store.hasWinLotto645 || store.hasWinLotto720 || store.hasWinSpeetto) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WinLottoTypeChipGroup(store = store)

                    Icon(
                        painter = painterResource(id = if (expendStoreWinHistory) R.drawable.icon_arrow_up else R.drawable.icon_arrow_down),
                        contentDescription = "Win Lotto History More Button Icon",
                        tint = LottoMateGray80,
                        modifier = Modifier
                            .size(20.dp)
                            .noInteractionClickable { expendStoreWinHistory = !expendStoreWinHistory },
                    )
                }
            }
        }

        if (expendStoreWinHistory && store.winCountOfLottoType.isNotEmpty()) {
            val winHistories = store.winCountOfLottoType.flatMap { it.winningDetails }

            StoreWinHistory(
                modifier = Modifier.fillMaxWidth(),
                histories = winHistories,
            )
        }
    }
}

@Composable
private fun StoreWinHistory(
    modifier: Modifier = Modifier,
    histories: List<WinningDetail>,
) {
    val groupHistories = histories.chunked(5)

    Column(modifier = modifier) {
        Divider(
            color = LottoMateGray20,
            modifier = Modifier.padding(20.dp),
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 20.dp)) {
            itemsIndexed(groupHistories) { index, history ->
                StoreWinHistoryChipGroup(histories = history)
                
                if (index != groupHistories.lastIndex) {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
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
        modifier = modifier.padding(horizontal = 12.dp),
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
        SelectStoreInfoContent(
            store = StoreInfoMock,
        )
    }
}