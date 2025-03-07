package com.lottomate.lottomate.presentation.screen.map

import android.util.Log
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
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.component.FilterButton
import com.lottomate.lottomate.presentation.screen.map.component.LottoTypeSelectorBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.MapInitPopupBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.StoreBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.checkLocationPermission
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateDim
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.GeoPositionCalculator
import com.lottomate.lottomate.utils.LocationManager
import com.lottomate.lottomate.utils.dropShadow
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

private val LoadingBackgroundSize = 160.dp
private val BottomSheetPeekHeight = 48.dp
private val BOTTOM_BUTTON_DEFAULT_BOTTOM_PADDING = 76.dp


@OptIn(ExperimentalMaterialApi::class, ExperimentalNaverMapApi::class)
@Composable
fun MapRoute(
    vm: MapViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLogin: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val showInitPopup = LottoMateDataStore.mapInitPopupFlow?.collectAsState(initial = false)
    val context = LocalContext.current
    val currentPosition by vm.currentPosition
    val currentCameraPosition by vm.currentCameraPosition
    val currentZoomLevel by vm.currentZoomLevel
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val lottoTypeState = vm.lottoTypeState
    val winStoreState by vm.winStoreState
    val favoriteStoreState by vm.favoriteStoreState

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(
            if (!LocationManager.hasGpsLocation()) { MapViewModel.DEFAULT_LATLNG } else currentPosition,
            MapViewModel.DEFAULT_ZOOM_LEVEL
        )
    }

    checkLocationPermission()

    var showLottoTypeSelectorBottomSheet by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        vm.snackBarFlow.collectLatest { message ->
            snackBarHostState.showSnackbar(
                message = message,
            )
        }
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val userLocation by remember {
        derivedStateOf { LocationManager.getCurrentLocation() }
    }

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

    var hideInitPopupBottomSheet by remember { mutableStateOf(showInitPopup?.value ?: false) }

    if (!hideInitPopupBottomSheet) {
        MapInitPopupBottomSheet(
            onDismiss = { hideInitPopupBottomSheet = false },
            onClickRequestOpen = {
                // TODO : 로그인 여부 체크 후, 로그인 페이지 이동(비로그인) or 요청 페이지 이동 (로그인)
                moveToLogin()
            },
        )
    }

    val leftTopPosition = GeoPositionCalculator.calculateLeftTopGeoPosition(
        currentLat = currentCameraPosition.latitude,
        currentLon = currentCameraPosition.longitude,
        zoom = currentZoomLevel
    )

    val rightBottomPosition = GeoPositionCalculator.calculateBottomRightGeoPosition(
        currentLat = currentCameraPosition.latitude,
        currentLon = currentCameraPosition.longitude,
        zoom = currentZoomLevel
    )

    LaunchedEffect(leftTopPosition, rightBottomPosition) {
        vm.changeLeftTopPosition(leftTopPosition)
        vm.changeRightBottomPosition(rightBottomPosition)
    }

    LaunchedEffect(currentPosition) {
        vm.fetchStoreList()
        cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(LatLng(currentCameraPosition.latitude, currentCameraPosition.longitude), cameraPositionState.position.zoom)))
    }

    LaunchedEffect(currentCameraPosition) {
        vm.fetchStoreList(currentCameraPosition)
        cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(LatLng(currentCameraPosition.latitude, currentCameraPosition.longitude), cameraPositionState.position.zoom)))
    }

    LaunchedEffect(userLocation) {
        Log.d("MapScreen", "현재 GPS : $userLocation")

        if (LocationManager.hasGpsLocation()) {
            vm.changeCurrentPosition(userLocation)
            cameraPositionState.animate(
                update = CameraUpdate.toCameraPosition(
                    CameraPosition(LatLng(userLocation.first, userLocation.second), cameraPositionState.position.zoom)
                ),
                animation = CameraAnimation.Easing,
                durationMs = 1_000,
            )
        }
    }

    MapScreen(
        padding = padding,
        uiState = uiState,
        cameraPositionState = cameraPositionState,
        currentPosition = currentPosition,
        currentCameraPosition = currentCameraPosition,
        leftTopPosition = leftTopPosition,
        rightBottomPosition = rightBottomPosition,
        lottoTypeState = lottoTypeState.toList().joinToString(", "),
        winStoreState = winStoreState,
        favoriteStoreState = favoriteStoreState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        onClickLottoType = { showLottoTypeSelectorBottomSheet = true },
        onClickWinLottoStore = { vm.changeWinStoreState() },
        onClickFavoriteStore = { vm.changeFavoriteStoreState() },
        onClickRefresh = { newPosition, newZoomLevel ->
            // 판매점 새로고침 -> 카메라 이동,
            vm.changeCurrentCameraPosition(newPosition)
            vm.changeCurrentZoomLevel(newZoomLevel)
        },
        onClickStoreList = { vm.showStoreList() },
        onClickLocationFocus = {
            LocationManager.updateLocation(context)
            vm.changeCurrentPosition(Pair(userLocation.first, userLocation.second))
                               },
        onClickSelectStoreMarker = { vm.selectStoreMarker(it) },
        onClickUnSelectStoreMarker = { vm.unselectStoreMarker() },
        onShowSnackBar = { vm.sendSnackBar(it) },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.BaseTopPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        snackBarHostState.currentSnackbarData?.let {
            LottoMateSnackBarHost(snackBarHostState = snackBarHostState) {
                LottoMateSnackBar(message = it.visuals.message)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalNaverMapApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun MapScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    uiState: MapUiState,
    currentPosition: LatLng,
    currentCameraPosition: LatLng,
    leftTopPosition: Pair<Double, Double>,
    rightBottomPosition: Pair<Double, Double>,
    cameraPositionState: CameraPositionState,
    selectStore: StoreInfo? = null,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onClickRefresh: (Pair<Double, Double>, Double) -> Unit,
    onClickStoreList: () -> Unit,
    onClickLocationFocus: () -> Unit,
    onClickSelectStoreMarker: (StoreInfo) -> Unit,
    onClickUnSelectStoreMarker: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isZoomControlEnabled = false,
            )
        )
    }

    val coroutineScope = rememberCoroutineScope()
    var bottomSheetTopPadding by remember { mutableIntStateOf(0) }
    var bottomSheetHeight by remember { mutableIntStateOf(0) }

    BottomSheetScaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding()),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            when (uiState) {
                is MapUiState.Success -> {
                    StoreBottomSheet(
                        bottomSheetState = bottomSheetScaffoldState,
                        bottomSheetTopPadding = bottomSheetTopPadding,
                        onShowSnackBar = onShowSnackBar,
                        onSizeChanged = { bottomSheetHeight = it }
                    )
                }

                else -> {}
            }
        },
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetPeekHeight = BottomSheetPeekHeight,
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            when (uiState) {
                MapUiState.Loading -> {
                    MapLoadingScreen(modifier = Modifier.fillMaxSize())

                }

                is MapUiState.Success -> {
                    val stores = uiState.storeInfo

                    Box(modifier = Modifier.fillMaxSize()) {
                        NaverMap(
                            modifier = Modifier.fillMaxSize(),
                            uiSettings = mapUiSettings,
                            properties = MapProperties(
                                locationTrackingMode = LocationTrackingMode.Follow,
                            ),
                            cameraPositionState = cameraPositionState,
                        ) {
                            selectStore?.let { store ->
                                Marker(
                                    state = MarkerState(position = store.latLng),
                                    icon = OverlayImage.fromResource(R.drawable.marker_select),
                                    onClick = {
                                        onClickUnSelectStoreMarker()

                                        coroutineScope.launch {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                        true
                                    }
                                )
                            }

                            stores.forEach { store ->
                                Marker(
                                    state = MarkerState(position = store.latLng),
                                    icon = if (store.winCountOfLottoType.isEmpty()) OverlayImage.fromResource(
                                        R.drawable.marker_default
                                    )
                                    else if (store.isLike) OverlayImage.fromResource(R.drawable.marker_like)
                                    else OverlayImage.fromResource(R.drawable.marker_win),
                                    onClick = {
                                        onClickSelectStoreMarker(store)
                                        true
                                    }
                                )
                            }

                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        leftTopPosition.first,
                                        leftTopPosition.second
                                    )
                                ),
                                icon = OverlayImage.fromResource(R.drawable.marker_like),
                            )

                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        rightBottomPosition.first,
                                        rightBottomPosition.second
                                    )
                                ),
                                icon = OverlayImage.fromResource(R.drawable.marker_win),
                            )

                            if (LocationManager.hasGpsLocation()) {
                                Marker(
                                    state = MarkerState(position = currentPosition),
                                    icon = OverlayImage.fromResource(R.drawable.icon_current_location),
                                )
                            }
                        }
                    }

                    MapButtons(
                        modifier = Modifier.fillMaxSize(),
                        lottoTypeState = lottoTypeState,
                        winStoreState = winStoreState,
                        favoriteStoreState = favoriteStoreState,
                        currentCameraPosition = currentCameraPosition,
                        bottomSheetHeight = bottomSheetHeight,
                        cameraPositionState = cameraPositionState,
                        bottomButtonPadding = 0.dp,
                        onSizeBottomSheetHeight = { height -> bottomSheetTopPadding = height },
                        onClickLottoType = onClickLottoType,
                        onClickWinLottoStore = onClickWinLottoStore,
                        onClickFavoriteStore = onClickFavoriteStore,
                        onClickRefresh = {
                            val cameraPosition = cameraPositionState.position.target
                            val zoomLevel = cameraPositionState.position.zoom

                            onClickRefresh(Pair(cameraPosition.latitude, cameraPosition.longitude), zoomLevel)
                        },
                        onClickStoreList = onClickStoreList,
                        onClickLocationFocus = onClickLocationFocus,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun MapButtons(
    modifier: Modifier = Modifier,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    currentCameraPosition: LatLng,
    bottomSheetHeight: Int,
    cameraPositionState: CameraPositionState,
    bottomButtonPadding: Dp,
    onSizeBottomSheetHeight: (Int) -> Unit,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onClickRefresh: () -> Unit,
    onClickStoreList: () -> Unit,
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
            bottomButtonPadding = bottomButtonPadding,
            currentCameraPosition = currentCameraPosition,
            bottomSheetHeight = bottomSheetHeight,
            cameraPositionState = cameraPositionState,
            onClickRefresh = onClickRefresh,
            onClickStoreList = onClickStoreList,
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
            .padding(top = Dimens.StatusBarHeight.plus(8.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    bottomButtonPadding: Dp,
    currentCameraPosition: LatLng,
    bottomSheetHeight: Int,
    cameraPositionState: CameraPositionState,
    onClickRefresh: () -> Unit,
    onClickStoreList: () -> Unit,
    onClickLocationFocus: () -> Unit,
) {
    fun isApproximatelyEqual(coord1: LatLng, coord2: LatLng): Boolean {
        val roundedLat1 = BigDecimal(coord1.latitude).setScale(6, RoundingMode.HALF_UP).toDouble()
        val roundedLon1 = BigDecimal(coord1.longitude).setScale(6, RoundingMode.HALF_UP).toDouble()

        val roundedLat2 = BigDecimal(coord2.latitude).setScale(6, RoundingMode.HALF_UP).toDouble()
        val roundedLon2 = BigDecimal(coord2.longitude).setScale(6, RoundingMode.HALF_UP).toDouble()

        return roundedLat1 == roundedLat2 && roundedLon1 == roundedLon2
    }
    val isCurrentMoving = cameraPositionState.isMoving || isApproximatelyEqual(currentCameraPosition, cameraPositionState.position.target)
    val density = LocalDensity.current
    val selectedBottomSheetHeight = with(density) { bottomSheetHeight.toDp() }

    Row(
        modifier = modifier
            .padding(horizontal = Dimens.DefaultPadding20)
            .padding(
                bottom = selectedBottomSheetHeight
                    .plus(28.dp)
                    .plus(12.dp)
                    .plus(20.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
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
                contentDescription = stringResource(id = R.string.desc_refresh_icon_map),
                tint = LottoMateGray100,
            )
        }

        Column {
            IconButton(
                onClick = onClickStoreList,
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
                    painter = painterResource(id = R.drawable.icon_list),
                    contentDescription = stringResource(id = R.string.desc_lotto_store_list_icon_map),
                    tint = LottoMateGray100,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            IconButton(
                onClick = onClickRefresh,
                modifier = Modifier
                    .dropShadow(
                        shape = CircleShape,
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        blur = if (isCurrentMoving) 16.dp else 8.dp,
                        color = if (isCurrentMoving) LottoMateBlue50.copy(
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
                    tint = if (isCurrentMoving) LottoMateBlue50 else LottoMateGray100,
                )
            }
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

//@OptIn(ExperimentalMaterialApi::class)
//@Preview(showBackground = true)
//@Composable
//private fun MapScreenPreview() {
//    LottoMateTheme {
//        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
//
//        MapScreen(
//            uiState = MapUiState.Success(StoreInfoMocks),
//            selectStore = null,
//            lottoTypeState = "복권 전체",
//            winStoreState = true,
//            favoriteStoreState = true,
//            onClickLottoType = {},
//            onClickWinLottoStore = {},
//            onClickFavoriteStore = {},
//            onClickRefresh = { newPosition, newZoomLevel -> },
//            onClickStoreList = {},
//            onClickLocationFocus = {},
//            onClickSelectStoreMarker = {},
//            onClickUnSelectStoreMarker = {},
//            padding = PaddingValues(32.dp),
//            leftTopPosition = Pair(0.0, 0.0),
//            rightBottomPosition = Pair(0.0, 0.0),
//            currentPosition = LatLng(37.566499, 126.968555),
//            bottomSheetScaffoldState = bottomSheetScaffoldState,
//            onShowSnackBar = {},
//        )
//    }
//}