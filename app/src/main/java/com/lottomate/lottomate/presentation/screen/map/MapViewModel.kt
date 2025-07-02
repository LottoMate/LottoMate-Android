package com.lottomate.lottomate.presentation.screen.map

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.domain.repository.UserRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.map.model.MapContract
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
) : BaseViewModel(errorHandler) {
    private var lastCameraCenter: LatLng? = null

    private val _effect = Channel<MapContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(MapContract.State())
    val state get() = _state.asStateFlow()

    private val _cameraCenter = MutableStateFlow(DEFAULT_LATLNG)
    val cameraCenter get() = _cameraCenter.asStateFlow()

    init {
        observeStoresFlow()
        fetchStores()
    }

    fun handleEvent(event: MapContract.Event) {
        when (event) {
            MapContract.Event.ClickLotterySelection -> {
                sendEffect(MapContract.Effect.ShowLotterySelectionBottomSheet)
            }
            is MapContract.Event.ClickMyLocation -> {
                handleMyLocationClick(event.currentMyLocation)
            }
            is MapContract.Event.ClickRefresh -> {
                handleRefreshClick(event.currentCameraCenter)
            }
            is MapContract.Event.ClickExploreMap -> {
                handleExploreMap(event.topLeft, event.bottomRight)
            }
            MapContract.Event.ClickWinStores -> {
                handleWinStoresClick()
            }
            MapContract.Event.ClickFavoriteStores -> {
                handleFavoriteStoresClick()
            }
            MapContract.Event.DeselectStore -> {
                _state.update { it.copy(selectedStore = null) }
            }
            is MapContract.Event.SelectStoreListFilter -> {
                handleStoreListFilterSelect(event.selectedFilter)
            }
            is MapContract.Event.MoveCamera -> {
                handleCameraMove(event.center)
            }
            is MapContract.Event.SelectStore -> {
                _state.update { it.copy(selectedStore = event.store) }
            }
            is MapContract.Event.SelectLotteryType -> {
                handleLotteryTypeSelect(event.selectedType)
            }
        }
    }

    fun handleTopLeftViewport(newViewport: Pair<Double, Double>) {
        val newTopLeftViewport = LatLng(newViewport.first, newViewport.second)

        _state.update { state ->
            state.copy(mapState = state.mapState.copy(viewportTopLeft = newTopLeftViewport))
        }
    }

    fun handleBottomRightViewport(newViewport: Pair<Double, Double>) {
        val newBottomRightViewport = LatLng(newViewport.first, newViewport.second)

        _state.update { state ->
            state.copy(mapState = state.mapState.copy(viewportBottomRight = newBottomRightViewport))
        }
    }

    /**
     * 로또 판매점을 가져옵니다.
     */
    fun fetchStores(isNextPage: Boolean = false) {
        viewModelScope.launch {
            if (!isNextPage) {
                _state.update { it.copy(isLoading = true, isRefreshAvailable = false) }
            }

            val type = LottoType.Group.toServerCode(state.value.selectedLotteryType)

            val userLocationInfo = StoreInfoRequestBody(
                leftLot = state.value.mapState.viewportTopLeft.longitude,
                leftLat = state.value.mapState.viewportTopLeft.latitude,
                rightLot = state.value.mapState.viewportBottomRight.longitude,
                rightLat = state.value.mapState.viewportBottomRight.latitude,
                personLot = if (state.value.isInSeoul) {
                    state.value.myLocation?.longitude ?: cameraCenter.value.longitude
                } else {
                    cameraCenter.value.longitude
                },
                personLat = if (state.value.isInSeoul) {
                    state.value.myLocation?.latitude ?: cameraCenter.value.latitude
                } else {
                    cameraCenter.value.latitude
                },
            )

            runCatching {
                val dis = state.value.selectedStoreListFilter == StoreListFilter.DISTANCE
                val drwt = state.value.selectedStoreListFilter == StoreListFilter.RANK

                if (isNextPage) {
                    storeRepository.fetchNextStoreList(type, userLocationInfo, state.value.isWinStores, state.value.isFavoriteStores, dis, drwt)
                } else {
                    storeRepository.fetchStoreList(type, userLocationInfo, state.value.isWinStores, state.value.isFavoriteStores, dis, drwt)
                        .onSuccess {
                            _state.update { it.copy(isLoading = false, isRefreshAvailable = false) }
                        }
                        .onFailure {
                            handleException(it)
                            _state.update { it.copy(isLoading = false, isRefreshAvailable = false) }
                            Log.d("MapVM(fetchStores)", it.stackTraceToString())
                        }
                }
            }.onFailure {
                handleException(it)
            }
        }
    }

    private fun handleLotteryTypeSelect(selectedTypes: List<LottoType.Group>) {
        _state.update { it.copy (selectedLotteryType = selectedTypes) }
        fetchStores()
    }

    private fun handleStoreListFilterSelect(selectedFilter: StoreListFilter) {
        val isInSeoul = checkIsInSeoul(cameraCenter.value)

        _state.update {
            it.copy(
                isInSeoul = isInSeoul,
                showNonSeoulSnackBar = !isInSeoul,
                selectedStoreListFilter = selectedFilter,
            )
        }

        fetchStores()
    }
    private fun handleWinStoresClick() {
        val isInSeoul = checkIsInSeoul(cameraCenter.value)

        _state.update {
            it.copy(
                isInSeoul = isInSeoul,
                showNonSeoulSnackBar = !isInSeoul,
                isWinStores = !it.isWinStores
            )
        }

        fetchStores()
    }

    private fun handleFavoriteStoresClick() {
        userRepository.userProfile.value ?: run {
            sendEffect(MapContract.Effect.ShowLogin)
            return
        }

        val isInSeoul = checkIsInSeoul(cameraCenter.value)

        _state.update {
            it.copy(
                isInSeoul = isInSeoul,
                showNonSeoulSnackBar = !isInSeoul,
                isFavoriteStores = !it.isFavoriteStores
            )
        }

        fetchStores()
    }

    private fun handleExploreMap(topLeft: LatLng, bottomRight: LatLng) {
        _state.update { state ->
            state.copy(mapState = state.mapState.copy(viewportTopLeft = topLeft, viewportBottomRight = bottomRight))
        }

        handleRefreshClick(DEFAULT_LATLNG)
    }

    private fun handleCameraMove(target: LatLng) {
        handleCameraChange(target)
    }

    private fun handleRefreshClick(currentCameraCenter: LatLng) {
        _state.update { it.copy(selectedStore = null) }

        handleCameraChange(currentCameraCenter)
        fetchStores()

        val isInSeoul = checkIsInSeoul(currentCameraCenter)
        _state.update {
            it.copy(
                isInSeoul = isInSeoul,
                showNonSeoulSnackBar = !isInSeoul,
            )
        }
    }

    private fun handleMyLocationClick(myLocation: LatLng) {
        handleCameraChange(myLocation)
    }

    /**
     * 카메라가 이동했을 때 호출됩니다.
     */
    private fun handleCameraChange(target: LatLng) {
        val hasRefresh = checkCameraCenterMoved(target)

        _cameraCenter.update { target }
        _state.update { state ->
            state.copy(
                isRefreshAvailable = hasRefresh,
                showRefreshToolTip = hasRefresh,
            )
        }
    }

    private fun checkCameraCenterMoved(currentCameraCenter: LatLng): Boolean {
        return if (lastCameraCenter == null || lastCameraCenter != currentCameraCenter) {
            lastCameraCenter = currentCameraCenter
            true
        } else { false }
    }

    fun changeInitPopupBottomSheet() {
        _state.update { it.copy(showInitPopupBottomSheet = !it.showInitPopupBottomSheet) }
    }

    fun changeRefreshToolTipState(state: Boolean) {
        _state.update { it.copy(showRefreshToolTip = state) }
    }

    fun sendEffect(effect: MapContract.Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun checkIsInSeoul(position: LatLng): Boolean {
        return position.latitude in (SEOUL_LATITUDE_RANGE) && position.longitude in (SEOUL_LONGITUDE_RANGE)
    }

    private fun observeStoresFlow() {
        viewModelScope.launch {
            storeRepository.stores
                .onStart { _state.update { it.copy(isLoading = true) } }
                .catch { handleException(it) }
                .onEach { stores ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            stores = stores,
                            showNoStoresSnackBar = stores.isEmpty(),
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    companion object {
        val DEFAULT_LATLNG = LatLng(37.566499, 126.968555)
        val TOP_LEFT_DEFAULT_LATLNG = LatLng(37.572364194154034, 126.96492563913249)
        val BOTTOM_RIGHT_DEFAULT_LATLNG = LatLng(37.56063380584597, 126.9721843608675)
        const val DEFAULT_ZOOM_LEVEL = 15.0
        private val SEOUL_LATITUDE_RANGE = 37.413294..37.715133
        private val SEOUL_LONGITUDE_RANGE = 126.269311..127.734086
    }
}