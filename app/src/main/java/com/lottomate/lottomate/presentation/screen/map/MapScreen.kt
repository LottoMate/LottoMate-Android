package com.lottomate.lottomate.presentation.screen.map

import android.view.Gravity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.component.LocationNoticeDialog
import com.lottomate.lottomate.presentation.screen.map.component.LocationPermissionObserver
import com.lottomate.lottomate.presentation.screen.map.component.LottoTypeSelectorBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.MapButtons
import com.lottomate.lottomate.presentation.screen.map.component.MapInitPopupBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.MapLoadingScreen
import com.lottomate.lottomate.presentation.screen.map.component.ShowNonSeoulSnackBarWithButton
import com.lottomate.lottomate.presentation.screen.map.component.StoreBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.checkLocationPermission
import com.lottomate.lottomate.presentation.screen.map.model.DefaultViewportBounds
import com.lottomate.lottomate.presentation.screen.map.model.MapContract
import com.lottomate.lottomate.presentation.screen.map.model.MapType
import com.lottomate.lottomate.presentation.screen.map.model.StoreBottomSheetExpendedType
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import com.lottomate.lottomate.utils.GeoPositionCalculator
import com.lottomate.lottomate.utils.LocationStateManager
import com.naver.maps.geometry.LatLng
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

const val BottomSheetPeekHeight = 48
private const val ZOOM_LEVEL_MINIMUM = 13.0
private const val ZOOM_LEVEL_MAXIMUM = 20.0

@OptIn(ExperimentalMaterialApi::class, ExperimentalNaverMapApi::class)
@Composable
fun MapRoute(
    vm: MapViewModel = hiltViewModel(),
    padding: PaddingValues,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val myLocation by LocationStateManager.currentLocation.collectAsState()
    val locationState by LocationStateManager.shouldShowLocationSettingDialog.collectAsState()
    val showInitPopup by LottoMateDataStore.mapInitPopupFlow.collectAsState(initial = null)
    var showLocationPermissionDialog by remember { mutableStateOf(false) }
    var showLotterySelectionBottomSheet by remember { mutableStateOf(false) }

    // 지도 진입 시, 로딩 화면 표시
    LaunchedEffect(true) {
        vm.errorFlow.collectLatest { error -> onShowErrorSnackBar(error) }
    }

    LaunchedEffect(locationState) {
        if (locationState) showLocationPermissionDialog = true
    }

    val uiState by vm.state.collectAsStateWithLifecycle()
    val cameraCenterState by vm.cameraCenter.collectAsStateWithLifecycle()
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(
            myLocation ?: MapViewModel.DEFAULT_LATLNG,
            MapViewModel.DEFAULT_ZOOM_LEVEL
        )
    }

    // 지도 진입 시, 위치 권한 확인 -> 다이얼로그 표시
    checkLocationPermission(
        onChangeState = { isGranted -> showLocationPermissionDialog = !isGranted }
    )

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is MapContract.Effect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(message = effect.message)
                }
                MapContract.Effect.ShowLocationPermissionDialog -> {
                    showLocationPermissionDialog = true
                }
                MapContract.Effect.ShowLotterySelectionBottomSheet -> {
                    showLotterySelectionBottomSheet = true
                }
            }
        }
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed),
    )

    // 지도 첫 진입 시, 초기 안내 팝업
    LaunchedEffect(showInitPopup) {
        showInitPopup?.let { showPopup ->
            if (!showPopup) { vm.changeInitPopupBottomSheet() }
        }
    }

    val topLeftViewport = GeoPositionCalculator.calculateLeftTopGeoPosition(
        currentLat = cameraCenterState.latitude,
        currentLon = cameraCenterState.longitude,
        zoom = cameraPositionState.position.zoom,
    )

    val bottomRightViewport = GeoPositionCalculator.calculateBottomRightGeoPosition(
        currentLat = cameraCenterState.latitude,
        currentLon = cameraCenterState.longitude,
        zoom = cameraPositionState.position.zoom,
    )

    LaunchedEffect(topLeftViewport) {
        vm.handleTopLeftViewport(topLeftViewport)
    }

    LaunchedEffect(bottomRightViewport) {
        vm.handleBottomRightViewport(bottomRightViewport)
    }

    // Activity의 라이플사이크를 관찰한 후, 사용자의 GPS를 가져옵니다.
    LocationPermissionObserver(
        onStartLocationPermissionGranted = {
            myLocation?.let {
                cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(it, cameraPositionState.position.zoom)))
                vm.handleEvent(MapContract.Event.ClickMyLocation(it))
            }
        },
        onStartLocationPermissionDenied = { },
    )

    when {
        uiState.isLoading -> { MapLoadingScreen() }
        uiState.showInitPopupBottomSheet -> {
            MapInitPopupBottomSheet(
                onDismiss = {
                    coroutineScope.launch {
                        LottoMateDataStore.changeMapInitPopupState()
                    }.invokeOnCompletion {
                        vm.changeInitPopupBottomSheet()
                    }
                },
                onClickRequestOpen = {
                    vm.changeInitPopupBottomSheet()

                    // TODO : 요청폼으로 이동
                },
            )
        }
        showLocationPermissionDialog -> {
            LocationNoticeDialog(
                onDismiss = { showLocationPermissionDialog = false },
            )
        }
        showLotterySelectionBottomSheet -> {
            LottoTypeSelectorBottomSheet(
                selectedLotteryType = uiState.selectedLotteryType,
                onDismiss = { showLotterySelectionBottomSheet = false },
                SelectLotteryType = {
                    showLotterySelectionBottomSheet = false
                    vm.handleEvent(MapContract.Event.SelectLotteryType(it))
                },
            )
        }
    }

    MapScreen(
        padding = padding,
        uiState = uiState,
        myLocation = myLocation,
        cameraPositionState = cameraPositionState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        onLotteryTypeClicked = { vm.handleEvent(MapContract.Event.ClickLotterySelection) },
        onClickWinLottoStore = { vm.handleEvent(MapContract.Event.ClickWinStores) },
        onClickFavoriteStore = { vm.handleEvent(MapContract.Event.ClickFavoriteStores) },
        onMyLocationClicked = {
            myLocation?.let {
                cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(it, cameraPositionState.position.zoom)))
                vm.handleEvent(MapContract.Event.ClickMyLocation(it))
            } ?: run { vm.sendEffect(MapContract.Effect.ShowLocationPermissionDialog) }
        },
        onExploreMapClicked = {
            val zoom = cameraPositionState.position.zoom
            val defaultBounds = DefaultViewportBounds.fromZoomLevel(zoom)

            cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(MapViewModel.DEFAULT_LATLNG, zoom)))
            vm.handleEvent(MapContract.Event.ClickExploreMap(defaultBounds.topLeft, defaultBounds.bottomRight))
        },
        onRefreshClicked = {
            val target = cameraPositionState.position.target
            vm.handleEvent(MapContract.Event.ClickRefresh(target))
        },
        onShowSnackBar = { vm.sendEffect(MapContract.Effect.ShowSnackBar(it)) },
        onLoadNextPage = { vm.fetchStores(true) },
        onSelectStore = {
            cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(it.latLng, cameraPositionState.position.zoom)))
            vm.handleEvent(MapContract.Event.SelectStore(it))
        },
        onDeselectStore = { vm.handleEvent(MapContract.Event.DeselectStore) },
        onChangeRefreshToolTip = { vm.changeRefreshToolTipState(it) },
        onCameraMoved = { vm.handleEvent(MapContract.Event.MoveCamera(it)) },
        onFilterSelected = { vm.handleEvent(MapContract.Event.SelectStoreListFilter(it)) },
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalNaverMapApi::class,)
@Composable
private fun MapScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    uiState: MapContract.State,
    myLocation: LatLng?,
    cameraPositionState: CameraPositionState,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onRefreshClicked: () -> Unit,
    onLotteryTypeClicked: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onSelectStore: (StoreInfo) -> Unit,
    onDeselectStore: () -> Unit,
    onMyLocationClicked: () -> Unit,
    onExploreMapClicked: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    onCameraMoved: (LatLng) -> Unit,
    onChangeRefreshToolTip: (Boolean) -> Unit,
    onFilterSelected: (StoreListFilter) -> Unit,
) {
    val density = LocalDensity.current
    val mapNaverLogoTopPadding = Dimens.StatusBarHeight.plus(16.dp)
    var topButtonsHeight by remember { mutableIntStateOf(0) }
    var bottomSheetHeight by remember { mutableIntStateOf(0) }
    val topButtonsHeightDp = with(density) { PaddingValues(top = topButtonsHeight.toDp())  }

    val mapUiSettings by remember(topButtonsHeight) {
        mutableStateOf(
            MapUiSettings(
                isZoomControlEnabled = false,
                logoGravity = Gravity.END or Gravity.TOP,
                logoMargin = PaddingValues(
                    top = with(density) { topButtonsHeight.toDp() }.plus(mapNaverLogoTopPadding),
                    end = Dimens.DefaultPadding20
                )
            )
        )
    }

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.minus(62)
    var bottomSheetExpendedType by remember { mutableStateOf(StoreBottomSheetExpendedType.COLLAPSED) }
    val storeBottomSheetHeight by remember {
        derivedStateOf {
            if (bottomSheetExpendedType == StoreBottomSheetExpendedType.COLLAPSED) BottomSheetPeekHeight
            else (screenHeightDp * bottomSheetExpendedType.ratio).toInt()
        }
    }

    val collapsedHeight = BottomSheetPeekHeight
    val halfHeight = screenHeightDp * StoreBottomSheetExpendedType.HALF.ratio
    val fullHeight = screenHeightDp * StoreBottomSheetExpendedType.FULL.ratio

    val heightAnim = remember(bottomSheetExpendedType) {
        Animatable(initialValue = collapsedHeight.dp.value)
    }

    // BottomSheet Animation
    LaunchedEffect(bottomSheetExpendedType) {
        val target = when (bottomSheetExpendedType) {
            StoreBottomSheetExpendedType.COLLAPSED -> collapsedHeight.dp.value
            StoreBottomSheetExpendedType.HALF -> halfHeight.dp.value
            StoreBottomSheetExpendedType.FULL -> fullHeight.dp.value
        }

        heightAnim.animateTo(
            targetValue = target.dp.value,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }

    // 지도 이동 시, 서울 여부 확인 & BottomSheet 접기
    LaunchedEffect(cameraPositionState.isMoving) {
        val target = cameraPositionState.position.target
        onCameraMoved(target)

        bottomSheetExpendedType = StoreBottomSheetExpendedType.COLLAPSED
    }

    BottomSheetScaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding()),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            var isUpdated = false

            StoreBottomSheet(
                modifier = Modifier
                    .height(heightAnim.value.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                if (!isUpdated) {
                                    bottomSheetExpendedType = when {
                                        // 위로 드래그 (확장)
                                        dragAmount < 0 && bottomSheetExpendedType == StoreBottomSheetExpendedType.COLLAPSED -> StoreBottomSheetExpendedType.HALF
                                        dragAmount < 0 && bottomSheetExpendedType == StoreBottomSheetExpendedType.COLLAPSED -> StoreBottomSheetExpendedType.HALF
                                        dragAmount < 0 && bottomSheetExpendedType == StoreBottomSheetExpendedType.HALF && uiState.stores.isEmpty() -> StoreBottomSheetExpendedType.HALF
                                        dragAmount < 0 && bottomSheetExpendedType == StoreBottomSheetExpendedType.HALF && uiState.stores.isNotEmpty() -> StoreBottomSheetExpendedType.FULL
                                        // 아래로 드래그 (축소)
                                        dragAmount > 0 && bottomSheetExpendedType == StoreBottomSheetExpendedType.FULL -> StoreBottomSheetExpendedType.HALF
                                        dragAmount > 0 && bottomSheetExpendedType == StoreBottomSheetExpendedType.HALF -> StoreBottomSheetExpendedType.COLLAPSED
                                        else -> StoreBottomSheetExpendedType.COLLAPSED
                                    }
                                }
                                isUpdated = true
                            },
                            onDragEnd = { isUpdated = false }
                        )
                    },
                bottomSheetExpendedType = bottomSheetExpendedType,
                selectedStore = uiState.selectedStore,
                selectedStoreListFilter = uiState.selectedStoreListFilter,
                onShowSnackBar = onShowSnackBar,
                isInSeoul = uiState.isInSeoul,
                onExploreMapClicked = {
                    bottomSheetExpendedType = StoreBottomSheetExpendedType.COLLAPSED
                    onExploreMapClicked()
                },
                onSelectStore = onSelectStore,
                onSizeChanged = { bottomSheetHeight = it },
                onLoadNextPage = onLoadNextPage,
                onFilterSelected = onFilterSelected,
            )
        },
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetPeekHeight = storeBottomSheetHeight.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                uiSettings = mapUiSettings,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    minZoom = ZOOM_LEVEL_MINIMUM,
                    maxZoom = ZOOM_LEVEL_MAXIMUM,
                ),
                cameraPositionState = cameraPositionState,
            ) {
                // 선택한 복권 판매점 표시
                uiState.selectedStore?.let { store ->
                    Marker(
                        state = MarkerState(position = store.latLng),
                        icon = OverlayImage.fromResource(R.drawable.marker_select),
                        onClick = {
                            bottomSheetExpendedType = StoreBottomSheetExpendedType.COLLAPSED
                            onDeselectStore()

                            true
                        }
                    )
                }

                // 복권 판매점 목록
                uiState.stores.forEach { store ->
                    Marker(
                        state = MarkerState(position = store.latLng),
                        icon = if (store.winCountOfLottoType.isEmpty()) OverlayImage.fromResource(
                            R.drawable.marker_default
                        )
                        else if (store.isLike) OverlayImage.fromResource(R.drawable.marker_like)
                        else OverlayImage.fromResource(R.drawable.marker_win),
                        onClick = {
                            bottomSheetExpendedType = StoreBottomSheetExpendedType.HALF
                            onSelectStore(store)

                            true
                        }
                    )
                }

                // 사용자의 현재 위치 표시 마커
                myLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        icon = OverlayImage.fromResource(R.drawable.icon_current_location),
                    )
                }
            }

            MapButtons(
                selectedStore = uiState.selectedStore,
                bottomSheetExpendedType = bottomSheetExpendedType,
                selectedLotteryType = LottoType.Group.getDisplayNames(uiState.selectedLotteryType),
                winStoreState = uiState.isWinStores,
                favoriteStoreState = uiState.isFavoriteStores,
                isRefreshAvailable = uiState.isRefreshAvailable,
                showRefreshToolTip = uiState.showRefreshToolTip,
                bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
                onSizeTopBottonsHeight = { height -> topButtonsHeight = height },
                onLotteryTypeClicked = onLotteryTypeClicked,
                onClickWinLottoStore = onClickWinLottoStore,
                onClickFavoriteStore = onClickFavoriteStore,
                onRefreshClicked = onRefreshClicked,
                onClickMapType = {
                    bottomSheetExpendedType = when (it) {
                        MapType.MAP -> StoreBottomSheetExpendedType.COLLAPSED
                        MapType.LIST -> {
                            onDeselectStore()
                            StoreBottomSheetExpendedType.FULL
                        }
                    }
                },
                onMyLocationClicked = {
                    bottomSheetExpendedType = StoreBottomSheetExpendedType.COLLAPSED
                    onMyLocationClicked()
                },
                onChangeRefreshToolTip = onChangeRefreshToolTip
            )

            when {
                uiState.showNonSeoulSnackBar -> {
                    ShowNonSeoulSnackBarWithButton(
                        topSnackBarPadding = topButtonsHeightDp.calculateTopPadding(),
                        bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
                        onClickRequestOpen = {

                        },
                        onClickButton = onExploreMapClicked,
                    )
                }
                uiState.showNoStoresSnackBar -> {
                    LottoMateSnackBar(
                        message = "조건에 맞는 지점이 없어요",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(
                                top = Dimens.StatusBarHeight
                                    .plus(30.dp)
                                    .plus(topButtonsHeightDp.calculateTopPadding())
                            ),
                    )
                }
            }
        }
    }
}