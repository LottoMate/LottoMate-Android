package com.lottomate.lottomate.presentation.screen.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.BottomSheetState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarWithButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.main.model.FullScreenType
import com.lottomate.lottomate.presentation.screen.map.component.FilterButton
import com.lottomate.lottomate.presentation.screen.map.component.LocationNoticeDialog
import com.lottomate.lottomate.presentation.screen.map.component.LocationPermissionObserver
import com.lottomate.lottomate.presentation.screen.map.component.LottoTypeSelectorBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.MapInitPopupBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.StoreBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.checkLocationPermission
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
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

private const val BottomSheetPeekHeight = 48

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapRoute(
    vm: MapViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLogin: () -> Unit,
    onShowFullScreen: (FullScreenType) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    // 지도 진입 시, 로딩 화면 표시
    LaunchedEffect(true) {
        onShowFullScreen(FullScreenType.MAP_LOADING)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val showInitPopup by LottoMateDataStore.mapInitPopupFlow.collectAsState(initial = null)
    var showInitPopupBottomSheet by remember { mutableStateOf(false) }

    val currentPosition by vm.currentPosition
    val currentCameraPosition by vm.currentCameraPosition
    val currentZoomLevel by vm.currentZoomLevel
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val selectedStore by vm.selectedStore.collectAsState(initial = null)

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

    var showLottoTypeSelectorBottomSheet by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    var showLocationNoticeDialog by remember { mutableStateOf(false) }
    // 지도 진입 시, 위치 권한 확인 -> 다이얼로그 표시
    checkLocationPermission(
        onChangeState = { showLocationNoticeDialog = !it }
    )

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

    // 지도 첫 진입 시, 초기 안내 팝업
    LaunchedEffect(showInitPopup) {
        showInitPopup?.let { showPopup ->
            if (!showPopup) showInitPopupBottomSheet = true
        }
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

    // Activity의 라이플사이크를 관찰한 후, 사용자의 GPS를 가져옵니다.
    LocationPermissionObserver(
        onStartLocationPermissionGranted = {
            LocationManager.getCurrentLocation()
        },
        onStartLocationPermissionDenied = { },
    )

    // 다이얼로그
    when {
        showInitPopupBottomSheet -> {
            MapInitPopupBottomSheet(
                onDismiss = {
                    coroutineScope.launch {
                        LottoMateDataStore.changeMapInitPopupState()
                    }.invokeOnCompletion {
                        showInitPopupBottomSheet = false
                    }
                },
                onClickRequestOpen = {
                    showInitPopupBottomSheet = false

                    // TODO : 로그인 여부 체크 후, 로그인 페이지 이동(비로그인) or 요청 페이지 이동 (로그인)
                    moveToLogin()
                },
            )
        }
        showLocationNoticeDialog -> {
            LocationNoticeDialog(
                onDismiss = { showLocationNoticeDialog = false },
            )
        }
    }

    MapScreen(
        padding = padding,
        uiState = uiState,
        selectStore = selectedStore,
        cameraPositionState = cameraPositionState,
        currentPosition = currentPosition,
        currentCameraPosition = currentCameraPosition,
        lottoTypeState = lottoTypeState.toList().joinToString(", "),
        winStoreState = winStoreState,
        favoriteStoreState = favoriteStoreState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        onClickLottoType = { showLottoTypeSelectorBottomSheet = true },
        onClickWinLottoStore = { vm.changeWinStoreState() },
        onClickFavoriteStore = { vm.changeFavoriteStoreState() },
        onChangeCameraPositionWithZoom = { newPosition, newZoomLevel ->
            // 판매점 새로고침 -> 카메라 이동,
            vm.changeCurrentCameraPosition(newPosition)
            vm.changeCurrentZoomLevel(newZoomLevel)
        },
        onClickStoreList = { vm.showStoreList() },
        onClickLocationFocus = { userLocationPosition ->
            if (LocationManager.hasLocationPermission(context)) {
                LocationManager.updateLocation(context)
                vm.changeCurrentPosition(Pair(userLocationPosition.latitude, userLocationPosition.longitude))
            } else {
                showLocationNoticeDialog = true
            }
        },
        onClickSelectStoreMarker = { vm.selectStoreMarker(it) },
        onClickUnSelectStoreMarker = { vm.unselectStoreMarker() },
        onClickJustLooking = {
            vm.changeCurrentCameraPosition(Pair(MapViewModel.DEFAULT_LATLNG.latitude, MapViewModel.DEFAULT_LATLNG.longitude))
        },
        onClickRefresh = { isInSeoul ->
            when (isInSeoul) {
                true -> vm.fetchStoreList()
                false -> vm.resetStoreList()
            }
        },
        onChangeCurrentPosition = { vm.changeCurrentPosition(it) },
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalNaverMapApi::class,)
@Composable
private fun MapScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    uiState: MapUiState,
    currentPosition: LatLng,
    currentCameraPosition: LatLng,
    cameraPositionState: CameraPositionState,
    selectStore: StoreInfo? = null,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onChangeCurrentPosition: (Pair<Double, Double>) -> Unit,
    onClickRefresh: (Boolean) -> Unit,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onChangeCameraPositionWithZoom: (Pair<Double, Double>, Double) -> Unit,
    onClickStoreList: () -> Unit,
    onClickLocationFocus: (LatLng) -> Unit,
    onClickSelectStoreMarker: (StoreInfo) -> Unit,
    onClickUnSelectStoreMarker: () -> Unit,
    onClickJustLooking: () -> Unit,
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

    var showNonSeoulSnackBarAndButton by remember { mutableStateOf(false) }

    val userLocation by remember {
        derivedStateOf { LocationManager.getCurrentLocation() }
    }

    fun checkIsInSeoul(position: LatLng): Boolean {
        val isInSeoul = position.latitude in (37.413294..37.715133) && position.longitude in (126.269311..127.734086)
        showNonSeoulSnackBarAndButton = !isInSeoul

        return isInSeoul
    }

    // 지도 이동 시, 서울 여부 확인 & BottomSheet 접기
    LaunchedEffect(cameraPositionState.isMoving) {
        val cameraPosition = cameraPositionState.position.target
        checkIsInSeoul(cameraPosition)

        bottomSheetScaffoldState.bottomSheetState.collapse()
    }

    val density = LocalDensity.current
    val topButtonPadding = with(density) { PaddingValues(top = bottomSheetTopPadding.toDp())  }

//    LaunchedEffect(currentPosition) {
//
//        onClickRefresh(true)
////        if (checkIsInSeoul(currentPosition)) loadStoreList()
//
//            cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(LatLng(currentCameraPosition.latitude, currentCameraPosition.longitude), cameraPositionState.position.zoom)))
//
//    }

    /**
     * 카메라 위치가 변경되었을 때 (새로고침)
     *
     * 1. 서울 여부 판단
     */
    LaunchedEffect(currentCameraPosition) {
        // 현재 카메라 위치가 서울인 경우에만 로또 판매점 정보 조회
        if (checkIsInSeoul(currentCameraPosition)) onClickRefresh(true)
        // 서울이 아니므로 리스트 비워주어야 함
        else onClickRefresh(false)

        cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(LatLng(currentCameraPosition.latitude, currentCameraPosition.longitude), cameraPositionState.position.zoom)))
    }

    // 지도 첫 진입 시, 사용자 GPS가 있을 경우 호출
    LaunchedEffect(userLocation) {
        Log.d("MapScreen", "현재 사용자 GPS : $userLocation")

        if (LocationManager.hasGpsLocation()) {
            onChangeCurrentPosition(userLocation)
            checkIsInSeoul(LatLng(userLocation.first, userLocation.second))

            cameraPositionState.animate(
                update = CameraUpdate.toCameraPosition(
                    CameraPosition(LatLng(userLocation.first, userLocation.second), cameraPositionState.position.zoom)
                ),
                animation = CameraAnimation.Easing,
                durationMs = 1_000,
            )
        }
    }

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
                        isInSeoul = checkIsInSeoul(currentCameraPosition),
                        onClickJustLooking = onClickJustLooking,
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

                is MapUiState.Failed -> {
                    // TODO : 데이터를 가져오는 중에 오류가 발생했을 때
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
                                        val zoomLevel = cameraPositionState.position.zoom
                                        onClickSelectStoreMarker(store)
                                        onChangeCameraPositionWithZoom(Pair(it.position.latitude, it.position.longitude), zoomLevel)
                                        true
                                    }
                                )
                            }

                            if (LocationManager.hasGpsLocation()) {
                                Marker(
                                    state = MarkerState(position = currentPosition),
                                    icon = OverlayImage.fromResource(R.drawable.icon_current_location),
                                )
                            }
                        }

                        // 로또 판매점이 없을 경우 -> 서울일 경우에만 토스트 표시
                        if (stores.isEmpty() && checkIsInSeoul(currentCameraPosition)) {
                            LottoMateSnackBar(
                                message = "조건에 맞는 지점이 없어요",
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(
                                        top = Dimens.StatusBarHeight
                                            .plus(30.dp)
                                            .plus(topButtonPadding.calculateTopPadding())
                                    )
                            )
                        }
                    }

                    MapButtons(
                        lottoTypeState = lottoTypeState,
                        winStoreState = winStoreState,
                        favoriteStoreState = favoriteStoreState,
                        bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
                        currentCameraPosition = currentCameraPosition,
                        cameraPositionState = cameraPositionState,
                        onSizeBottomSheetHeight = { height -> bottomSheetTopPadding = height },
                        onClickLottoType = onClickLottoType,
                        onClickWinLottoStore = onClickWinLottoStore,
                        onClickFavoriteStore = onClickFavoriteStore,
                        onClickRefresh = {
                            val cameraPosition = cameraPositionState.position.target
                            val zoomLevel = cameraPositionState.position.zoom

                            onChangeCameraPositionWithZoom(Pair(cameraPosition.latitude, cameraPosition.longitude), zoomLevel)
                        },
                        onClickStoreList = onClickStoreList,
                        onClickLocationFocus = { onClickLocationFocus(LatLng(userLocation.first, userLocation.second)) },
                    )
                }
            }

            if (showNonSeoulSnackBarAndButton) {
                NonSeoulSnackBarWithButton(
                    topSnackBarPadding = topButtonPadding.calculateTopPadding(),
                    bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
                    onClickRequestOpen = {

                    },
                    onClickButton = onClickJustLooking,
                )
            }
        }
    }
}

/**
 * 서울을 벗어났을 때 보여지는 상단 스낵바 & 하단 둘러보기 버튼
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NonSeoulSnackBarWithButton(
    topSnackBarPadding: Dp = 0.dp,
    bottomSheetState: BottomSheetState,
    onClickRequestOpen: () -> Unit,
    onClickButton: () -> Unit = {},
) {
    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp


    // BottomSheet의 높이 측정
    val bottomSheetOffset by remember {
        mutableStateOf(with(density) { bottomSheetState.requireOffset().toDp() })
    }

    // BottomSheet의 높이에 맞춰 버튼 하단 padding값 설정
    val buttonBottomPadding by remember {
        Log.d("MapScreen", "NonSeoulSnackBarWithButton: ${screenHeight - bottomSheetOffset}")
        mutableStateOf(screenHeight - bottomSheetOffset)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LottoMateSnackBarWithButton(
            message = "조건에 맞는 지점이 없어요",
            buttonText = "오픈 요청하기",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = Dimens.StatusBarHeight
                        .plus(30.dp)
                        .plus(topSnackBarPadding)
                ),
            onClick = {
                // TODO : 지도 오픈 요청 페이지 이동
                onClickRequestOpen()
            }
        )

        // BottomSheet가 확장되지 않은 상태에서만 보이도록 설정
        if (bottomSheetState.isCollapsed) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = buttonBottomPadding.plus(BottomSheetPeekHeight))
                    .dropShadow(
                        shape = RoundedCornerShape(30.dp),
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        blur = 8.dp,
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .clickable { onClickButton() }
                    .background(LottoMateWhite, RoundedCornerShape(30.dp))
                    .padding(horizontal = Dimens.RadiusLarge, vertical = Dimens.RadiusSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "로또 지도 둘러보기",
                    style = LottoMateTheme.typography.label2,
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(12.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MapButtons(
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    currentCameraPosition: LatLng,
    bottomSheetState: BottomSheetState,
    cameraPositionState: CameraPositionState,
    onSizeBottomSheetHeight: (Int) -> Unit,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onClickRefresh: () -> Unit,
    onClickStoreList: () -> Unit,
    onClickLocationFocus: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier.align(Alignment.BottomCenter),
            bottomSheetState = bottomSheetState,
            currentCameraPosition = currentCameraPosition,
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
            isSelected = true,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    currentCameraPosition: LatLng,
    bottomSheetState: BottomSheetState,
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
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // BottomSheet의 높이를 실시간 관찰
    val bottomSheetOffset by remember {
        derivedStateOf {
            with(density) { bottomSheetState.requireOffset().toDp() }
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
                .padding(bottom = buttonBottomPadding.plus(BottomSheetPeekHeight))
            ,
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
}