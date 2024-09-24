package com.lottomate.lottomate.presentation.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.component.FilterButton
import com.lottomate.lottomate.presentation.screen.map.component.LottoTypeSelectorBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.StoreInfoBottomSheet
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateDim
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap

private val LoadingBackgroundSize = 160.dp

@Composable
fun MapRoute(
    vm: MapViewModel = hiltViewModel(),
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val lottoTypeState = vm.lottoTypeState
    val winStoreState by vm.winStoreState
    val favoriteStoreState by vm.favoriteStoreState

    var showLottoTypeSelectorBottomSheet by remember { mutableStateOf(false) }

    if (showLottoTypeSelectorBottomSheet) {
        LottoTypeSelectorBottomSheet(
            selectedLottoTypes = lottoTypeState,
            onDismiss = { showLottoTypeSelectorBottomSheet = false },
            onSelectLottoTypes = {
                vm.changeLottoTypeState(it)

                showLottoTypeSelectorBottomSheet = false
            },
        )
    }

    MapScreen(
        padding = padding,
        uiState = uiState,
        lottoTypeState = lottoTypeState.toList().joinToString(", "),
        winStoreState = winStoreState,
        favoriteStoreState = favoriteStoreState,
        onClickLottoType = { showLottoTypeSelectorBottomSheet = true },
        onClickWinLottoStore = { vm.changeWinStoreState() },
        onClickFavoriteStore = { vm.changeFavoriteStoreState() },
        onClickRefresh = {},
        onClickLocationFocus = {},
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalNaverMapApi::class)
@Composable
private fun MapScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    uiState: MapUiState,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onClickRefresh: () -> Unit,
    onClickLocationFocus: () -> Unit,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    var bottomSheetTopPadding by remember { mutableIntStateOf(0) }

    BottomSheetScaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding()),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            StoreInfoBottomSheet(
                bottomSheetTopPadding = bottomSheetTopPadding,
            )
        },
        sheetPeekHeight = 24.dp,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            when (uiState) {
                MapUiState.Loading -> {
                    MapLoadingScreen(modifier = Modifier.fillMaxSize())
                }
                is MapUiState.Success -> {

                }
            }

            MapContent(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                lottoTypeState = lottoTypeState,
                winStoreState = winStoreState,
                favoriteStoreState = favoriteStoreState,
                onSizeBottomSheetHeight = { height -> bottomSheetTopPadding = height },
                onClickLottoType = onClickLottoType,
                onClickWinLottoStore = onClickWinLottoStore,
                onClickFavoriteStore = onClickFavoriteStore,
                onClickRefresh = onClickRefresh,
                onClickLocationFocus = onClickLocationFocus,
            )
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun MapContent(
    modifier: Modifier = Modifier,
    uiState: MapUiState,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    onSizeBottomSheetHeight: (Int) -> Unit,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onClickRefresh: () -> Unit,
    onClickLocationFocus: () -> Unit,
) {

    Box(modifier = modifier) {
        TopFilterButtons(
            modifier = Modifier.fillMaxWidth(),
            lottoTypeState = lottoTypeState,
            winStoreState = winStoreState,
            favoriteStoreState = favoriteStoreState,
            onSizeBottomSheetHeight = onSizeBottomSheetHeight,
            onClickLottoType = onClickLottoType,
            onClickWinLottoStore = onClickWinLottoStore,
            onClickFavoriteStore = onClickFavoriteStore,
        )

        BottomButtons(
            modifier = Modifier.fillMaxSize(),
            onClickRefresh = onClickRefresh,
            onClickLocationFocus = onClickLocationFocus,
        )
   }
}

@Composable
private fun TopFilterButtons(
    modifier: Modifier = Modifier,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    onSizeBottomSheetHeight: (Int) -> Unit,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(top = Dimens.StatusBarHeight.plus(9.dp)),
            .onSizeChanged { onSizeBottomSheetHeight(it.height) },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FilterButton(
            text = lottoTypeState,
            iconRes = R.drawable.icon_filter_12,
            iconDescription = stringResource(id = R.string.desc_filter_icon_map),
            isSelected = lottoTypeState != LottoTypeFilter.All.kr,
            onClick = onClickLottoType,
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

@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    onClickRefresh: () -> Unit,
    onClickLocationFocus: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = Dimens.BottomTabHeight.plus(78.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        IconButton(
            onClick = onClickRefresh,
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
                painter = painterResource(id = R.drawable.icon_refresh),
                contentDescription = stringResource(id = R.string.desc_refresh_icon_map),
            )
        }
        IconButton(
            onClick = onClickLocationFocus,
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
                contentDescription = stringResource(id = R.string.desc_location_focus_icon_map),
            )
        }
    }
}

@Composable
private fun MapLoadingScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(LottoMateDim),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(LoadingBackgroundSize)
                    .dropShadow(
                        shape = CircleShape,
                        blur = 8.dp,
                        offsetY = 0.dp,
                        offsetX = 0.dp,
                    )
                    .clip(CircleShape)
                    .background(LottoMateWhite)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LottoMateText(
                text = stringResource(id = R.string.map_loading_text),
                style = LottoMateTheme.typography.title3
                    .copy(
                        color = LottoMateWhite,
                        shadow = Shadow(
                            color = LottoMateBlack.copy(0.16f),
                            blurRadius = 0.8f
                        )
                    ),
            )

            LottoMateText(
                text = stringResource(id = R.string.map_loading_sub_text),
                style = LottoMateTheme.typography.headline2
                    .copy(
                        color = LottoMateWhite,
                        shadow = Shadow(
                            color = LottoMateBlack.copy(0.16f),
                            blurRadius = 0.8f
                        )
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {
    LottoMateTheme {
        MapScreen(
            uiState = MapUiState.Success(StoreInfoMocks),
            lottoTypeState = "복권 전체",
            winStoreState = true,
            favoriteStoreState = true,
            onClickLottoType = {},
            onClickWinLottoStore = {},
            onClickFavoriteStore = {},
            onClickRefresh = {},
            onClickLocationFocus = {},
            padding = PaddingValues(32.dp)
        )
    }
}