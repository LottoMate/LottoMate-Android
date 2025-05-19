package com.lottomate.lottomate.presentation.screen.map

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.LottoMateDialog
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.main.model.FullScreenType
import com.lottomate.lottomate.presentation.screen.map.component.LocationNoticeDialog
import com.lottomate.lottomate.presentation.screen.map.component.LocationPermissionObserver
import com.lottomate.lottomate.presentation.screen.map.component.LottoTypeSelectorBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.MapButtons
import com.lottomate.lottomate.presentation.screen.map.component.MapInitPopupBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.ShowNonSeoulSnackBarWithButton
import com.lottomate.lottomate.presentation.screen.map.component.StoreBottomSheet
import com.lottomate.lottomate.presentation.screen.map.component.checkLocationPermission
import com.lottomate.lottomate.presentation.screen.map.model.StoreBottomSheetExpendedType
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.utils.GeoPositionCalculator
import com.lottomate.lottomate.utils.LocationStateManager
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

const val BottomSheetPeekHeight = 48
private const val ZOOM_LEVEL_MINIMUM = 13.0
private const val ZOOM_LEVEL_MAXIMUM = 20.0

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapRoute(
    vm: MapViewModel = hiltViewModel(),
    padding: PaddingValues,
    onShowFullScreen: (FullScreenType) -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    val locationState by LocationStateManager.shouldShowLocationSettingDialog.collectAsState()
    val currentLocation by LocationStateManager.currentLocation.collectAsState()

    var showLocationSettingDialog by remember { mutableStateOf(false) }

    // 지도 진입 시, 로딩 화면 표시
    LaunchedEffect(true) {
        onShowFullScreen(FullScreenType.MAP_LOADING)

        vm.errorFlow.collectLatest { error -> onShowErrorSnackBar(error) }
    }

    LaunchedEffect(locationState) {
        if (locationState) showLocationSettingDialog = true
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val showInitPopup by LottoMateDataStore.mapInitPopupFlow.collectAsState(initial = null)
    var showInitPopupBottomSheet by remember { mutableStateOf(false) }

    val currentPosition by vm.currentPosition
    val currentCameraPosition by vm.currentCameraPosition
    val currentZoomLevel by vm.currentZoomLevel

    val stores by vm.stores.collectAsState()
    val selectedStore by vm.selectedStore.collectAsState(initial = null)

    val cameraCenterState by vm.cameraCenter.collectAsStateWithLifecycle()
    val isRefreshAvailable by vm.isRefreshAvailable.collectAsStateWithLifecycle()
    val lottoTypeState = vm.lottoTypeState
    val winStoreState by vm.winStoreState
    val favoriteStoreState by vm.favoriteStoreState

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(
            currentLocation ?: currentPosition,
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
            currentLocation ?: LocationStateManager.updateLocation()
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

                    // TODO : 요청폼으로 이동
                },
            )
        }
        showLocationNoticeDialog -> {
            LocationNoticeDialog(
                onDismiss = { showLocationNoticeDialog = false },
            )
        }
        // TODO : 추후 관련 기획이 나오면 변경 예정
        showLocationSettingDialog -> {
            LottoMateDialog(
                title = """
                    내 위치를 보려면
                    설정 > 위치 사용을 허용해야
                    확인할 수 있어요
                """.trimIndent(),
                cancelText = "취소",
                confirmText = "설정으로 이동",
                onDismiss = {
                    LocationStateManager.setShouldShowLocationSettingDialog(false)
                    showLocationSettingDialog = false
                },
                onConfirm = {
                    LocationStateManager.setShouldShowLocationSettingDialog(false)
                    showLocationSettingDialog = false

                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                },
            )
        }
    }

    MapScreen(
        padding = padding,
        stores = stores,
        currentLocation = currentLocation,
        selectStore = selectedStore,
        cameraPositionState = cameraPositionState,
        currentPosition = currentPosition,
        currentCameraPosition = currentCameraPosition,
        lottoTypeState = lottoTypeState.toList().joinToString(", "),
        winStoreState = winStoreState,
        favoriteStoreState = favoriteStoreState,
        isRefreshAvailable= isRefreshAvailable,
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
            if (LocationStateManager.hasLocationPermission(context)) {
                LocationStateManager.updateLocation()
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
        onLoadNextPage = { vm.loadNextPage() },
        onCheckRefreshAvailable = { vm.onCameraIdle(it) },
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
    stores: List<StoreInfo>,
    currentLocation: LatLng?,
    currentPosition: LatLng,
    currentCameraPosition: LatLng,
    cameraPositionState: CameraPositionState,
    selectStore: StoreInfo? = null,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    isRefreshAvailable: Boolean,
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
    onCheckRefreshAvailable: (LatLng) -> Unit,
    onShowSnackBar: (String) -> Unit,
    onLoadNextPage: () -> Unit,
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

    fun checkIsInSeoul(position: LatLng): Boolean {
        val isInSeoul = position.latitude in (37.413294..37.715133) && position.longitude in (126.269311..127.734086)
        showNonSeoulSnackBarAndButton = !isInSeoul

        return isInSeoul
    }

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.minus(62)
    // BottomSheet Height 설정
    var expendedType by remember {
        mutableStateOf(StoreBottomSheetExpendedType.COLLAPSED)
    }
    val storeBottomSheetHeight by animateIntAsState(
        targetValue = when (expendedType) {
            // 화면의 60%
            StoreBottomSheetExpendedType.HALF -> (screenHeightDp * 0.6f).toInt()
            // 화면의 90%
            StoreBottomSheetExpendedType.FULL -> (screenHeightDp * 0.9f).toInt()
            StoreBottomSheetExpendedType.COLLAPSED -> BottomSheetPeekHeight
        }, label = ""
    )

    // 지도 이동 시, 서울 여부 확인 & BottomSheet 접기
    LaunchedEffect(cameraPositionState.isMoving) {
        val cameraPosition = cameraPositionState.position.target
        checkIsInSeoul(cameraPosition)
        onCheckRefreshAvailable(cameraPosition)

        expendedType = StoreBottomSheetExpendedType.COLLAPSED
    }

    val density = LocalDensity.current
    val topButtonPadding = with(density) { PaddingValues(top = bottomSheetTopPadding.toDp())  }

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
    LaunchedEffect(currentLocation) {
        Log.d("MapScreen", "현재 사용자 GPS : $currentLocation")

        currentLocation?.let { location ->
            onChangeCurrentPosition(Pair(location.latitude, location.longitude))
            checkIsInSeoul(LatLng(location.latitude, location.longitude))

            cameraPositionState.animate(
                update = CameraUpdate.toCameraPosition(
                    CameraPosition(location, cameraPositionState.position.zoom)
                ),
                animation = CameraAnimation.Easing,
                durationMs = 1_000,
            )
        }
    }

    var selectedStore by remember { mutableStateOf<StoreInfo?>(null) }

    BottomSheetScaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding()),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            var isUpdated = false

            StoreBottomSheet(
                modifier = Modifier
                    .height(storeBottomSheetHeight.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                if (!isUpdated) {
                                    expendedType = when {
                                        // 위로 드래그 (확장)
                                        dragAmount < 0 && expendedType == StoreBottomSheetExpendedType.COLLAPSED -> StoreBottomSheetExpendedType.HALF
                                        dragAmount < 0 && expendedType == StoreBottomSheetExpendedType.HALF && stores.isEmpty() -> StoreBottomSheetExpendedType.HALF
                                        dragAmount < 0 && expendedType == StoreBottomSheetExpendedType.HALF && stores.isNotEmpty() -> StoreBottomSheetExpendedType.FULL
                                        // 아래로 드래그 (축소)
                                        dragAmount > 0 && expendedType == StoreBottomSheetExpendedType.FULL -> StoreBottomSheetExpendedType.HALF
                                        dragAmount > 0 && expendedType == StoreBottomSheetExpendedType.HALF -> StoreBottomSheetExpendedType.COLLAPSED
                                        else -> StoreBottomSheetExpendedType.COLLAPSED
                                    }
                                }
                                isUpdated = true
                            },
                            onDragEnd = { isUpdated = false }
                        )
                    },
                bottomSheetState = bottomSheetScaffoldState,
                bottomSheetTopPadding = bottomSheetTopPadding,
                selectedStore = selectedStore,
                onShowSnackBar = onShowSnackBar,
                isInSeoul = checkIsInSeoul(currentCameraPosition),
                onClickJustLooking = onClickJustLooking,
                onSizeChanged = { bottomSheetHeight = it }
                onLoadNextPage = onLoadNextPage,
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
                selectedStore?.let { store ->
                    Marker(
                        state = MarkerState(position = store.latLng),
                        icon = OverlayImage.fromResource(R.drawable.marker_select),
                        onClick = {
                            selectedStore = null
                            expendedType = StoreBottomSheetExpendedType.COLLAPSED

                            true
                        }
                    )
                }

                // 복권 판매점 목록
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

                            selectedStore = store
                            expendedType = StoreBottomSheetExpendedType.HALF

                            onChangeCameraPositionWithZoom(Pair(it.position.latitude, it.position.longitude), zoomLevel)
                            true
                        }
                    )
                }

                // 사용자의 현재 위치 표시 마커
                currentLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
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
            isRefreshAvailable = isRefreshAvailable,
            bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
            onSizeBottomSheetHeight = { height -> bottomSheetTopPadding = height },
            onClickLottoType = onClickLottoType,
            onClickWinLottoStore = onClickWinLottoStore,
            onClickFavoriteStore = onClickFavoriteStore,
            onClickRefresh = {
                val cameraPosition = cameraPositionState.position.target
                val zoomLevel = cameraPositionState.position.zoom

                onChangeCameraPositionWithZoom(Pair(cameraPosition.latitude, cameraPosition.longitude), zoomLevel)
            },
            onClickMapType = {
                expendedType = when (it) {
                    MapType.MAP -> StoreBottomSheetExpendedType.COLLAPSED
                    MapType.LIST -> {
                        selectedStore = null
                        StoreBottomSheetExpendedType.FULL
                    }
                }
            },
            onClickLocationFocus = {
                currentLocation?.let { location ->
                    onClickLocationFocus(location)
                } ?: LocationStateManager.updateLocation()
            },
        )

        // 서울이 아닌 경우 스낵바 + 하단 버튼 표시
        if (showNonSeoulSnackBarAndButton) {
            ShowNonSeoulSnackBarWithButton(
                topSnackBarPadding = topButtonPadding.calculateTopPadding(),
                bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
                onClickRequestOpen = {

                },
                onClickButton = onClickJustLooking,
            )
        }
    }
}