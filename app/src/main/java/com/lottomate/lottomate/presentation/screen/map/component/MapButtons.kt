package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateTooltip
import com.lottomate.lottomate.presentation.component.ToolTipDirection
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.BottomSheetPeekHeight
import com.lottomate.lottomate.presentation.screen.map.model.MapType
import com.lottomate.lottomate.presentation.screen.map.model.StoreBottomSheetExpendedType
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import kotlinx.coroutines.delay

/**
 * 지도에 보여지는 상/하단 버튼 컴포저블 함수
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapButtons(
    selectedStore: StoreInfo?,
    bottomSheetExpendedType: StoreBottomSheetExpendedType,
    selectedLotteryType: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    isRefreshAvailable: Boolean,
    showRefreshToolTip: Boolean,
    bottomSheetState: BottomSheetState,
    onSizeTopBottonsHeight: (Int) -> Unit,
    onLotteryTypeClicked: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onRefreshClicked: () -> Unit,
    onClickMapType: (MapType) -> Unit,
    onMyLocationClicked: () -> Unit,
    onChangeRefreshToolTip: (Boolean) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TopFilterButtons(
            modifier = Modifier.fillMaxWidth(),
            selectedLotteryType = selectedLotteryType,
            winStoreState = winStoreState,
            favoriteStoreState = favoriteStoreState,
            onSizeTopBottonsHeight = onSizeTopBottonsHeight,
            onLotteryTypeClicked = onLotteryTypeClicked,
            onClickWinLottoStore = onClickWinLottoStore,
            onClickFavoriteStore = onClickFavoriteStore,
        )

        BottomButtons(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedStore = selectedStore,
            showRefreshToolTip = showRefreshToolTip,
            bottomSheetExpendedType = bottomSheetExpendedType,
            bottomSheetState = bottomSheetState,
            isRefreshAvailable = isRefreshAvailable,
            onRefreshClicked = onRefreshClicked,
            onClickMapType = onClickMapType,
            onMyLocationClicked = onMyLocationClicked,
            onChangeRefreshToolTip = onChangeRefreshToolTip,
        )
    }
}

@Composable
private fun TopFilterButtons(
    modifier: Modifier = Modifier,
    selectedLotteryType: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    onSizeTopBottonsHeight: (Int) -> Unit,
    onLotteryTypeClicked: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(top = Dimens.StatusBarHeight.plus(8.dp))
            .onSizeChanged { onSizeTopBottonsHeight(it.height) },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FilterButton(
            text = selectedLotteryType,
            iconRes = R.drawable.icon_filter_12,
            iconDescription = stringResource(id = R.string.desc_filter_icon_map),
            isSelected = true,
            onClick = onLotteryTypeClicked,
        )

        Row {
            FilterButton(
                text = stringResource(id = R.string.map_win_store_filter_button),
                isSelected = winStoreState,
                onClick = onClickWinLottoStore,
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilterButton(
                text = stringResource(id = R.string.map_favorite_store_filter_button),
                isSelected = favoriteStoreState,
                onClick = onClickFavoriteStore,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    selectedStore: StoreInfo?,
    showRefreshToolTip: Boolean,
    bottomSheetExpendedType: StoreBottomSheetExpendedType,
    bottomSheetState: BottomSheetState,
    isRefreshAvailable: Boolean,
    onRefreshClicked: () -> Unit,
    onClickMapType: (MapType) -> Unit,
    onMyLocationClicked: () -> Unit,
    onChangeRefreshToolTip: (Boolean) -> Unit,
) {
    LaunchedEffect(isRefreshAvailable) {
        if (!showRefreshToolTip) {
            onChangeRefreshToolTip(true)
        } else {
            delay(5_000)
            onChangeRefreshToolTip(false)
        }
    }
    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var bottomSheetOffset by remember { mutableStateOf(BottomSheetPeekHeight.dp) }

    // BottomSheet의 높이를 실시간 관찰
    LaunchedEffect(bottomSheetState) {
        snapshotFlow {
            runCatching { bottomSheetState.requireOffset() }
                .getOrDefault(0f)
        }.collect { offset ->
            bottomSheetOffset = with(density) { offset.toDp() }
        }
    }

    // BottomSheet의 높이에 맞춰 버튼 하단 padding값 설정
    val buttonBottomPadding by remember {
        derivedStateOf { screenHeight - bottomSheetOffset }
    }

    // BottomSheet의 높이가 화면의 60%이상을 차지하면 버튼 숨기기
    val shouldShowButtons by remember {
        derivedStateOf { bottomSheetOffset > screenHeight * 0.4f }
    }

    if (shouldShowButtons) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(bottom = buttonBottomPadding.plus(BottomSheetPeekHeight.dp))
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            IconButton(
                onClick = onMyLocationClicked,
                modifier = Modifier
                    .dropShadow(
                        shape = CircleShape,
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        blur = 8.dp,
                    )
                    .background(
                        color = LottoMateWhite,
                        shape = CircleShape,
                    )
                    .size(40.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_location),
                    contentDescription = stringResource(id = R.string.desc_refresh_icon_map),
                    tint = LottoMateGray100,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                IconButton(
                    onClick = {
                        val type = selectedStore?.let {
                            MapType.LIST
                        } ?: run {
                            when (bottomSheetExpendedType) {
                                StoreBottomSheetExpendedType.COLLAPSED -> MapType.LIST
                                else -> MapType.MAP
                            }
                        }

                        onClickMapType(type)
                    },
                    modifier = Modifier
                        .dropShadow(
                            shape = CircleShape,
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            blur = 8.dp,
                        )
                        .background(
                            color = LottoMateWhite,
                            shape = CircleShape,
                        )
                        .size(40.dp),
                ) {
                    Icon(
                        painter = painterResource(id =
                            selectedStore?.let {
                                R.drawable.icon_list
                            } ?: run {
                                when (bottomSheetExpendedType) {
                                    StoreBottomSheetExpendedType.COLLAPSED -> R.drawable.icon_list
                                    else -> R.drawable.icon_map
                                }
                            }
                        ),
                        contentDescription = stringResource(id = R.string.desc_lotto_store_list_icon_map),
                        tint = LottoMateGray100,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isRefreshAvailable && showRefreshToolTip) {
                        LottoMateTooltip(
                            text = "현재 위치에서 검색하기",
                            direction = ToolTipDirection.RIGHT
                        )
                    }

                    IconButton(
                        onClick = onRefreshClicked,
                        modifier = Modifier
                            .dropShadow(
                                shape = CircleShape,
                                offsetX = 0.dp,
                                offsetY = 0.dp,
                                blur = if (isRefreshAvailable) 16.dp else 8.dp,
                                color = if (isRefreshAvailable) LottoMateBlue50.copy(
                                    alpha = 0.8f
                                ) else LottoMateBlack.copy(alpha = 0.16f),
                            )
                            .background(
                                color = LottoMateWhite,
                                shape = CircleShape,
                            )
                            .size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_refresh),
                            contentDescription = stringResource(id = R.string.desc_location_focus_icon_map),
                            tint = if (isRefreshAvailable) LottoMateBlue50 else LottoMateGray100,
                        )
                    }
                }
            }
        }
    }
}