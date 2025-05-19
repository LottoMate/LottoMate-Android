package com.lottomate.lottomate.presentation.screen.map

import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val storeRepository: StoreRepository,
) : BaseViewModel(errorHandler) {
    val stores: StateFlow<List<StoreInfo>> = storeRepository.stores
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )
    val selectedStore = storeRepository.store
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private var isFirstLoading = true
    private var leftTopPosition = mutableStateOf(DEFAULT_LATLNG)
    private var rightBottomPosition = mutableStateOf(DEFAULT_LATLNG)
    var currentPosition = mutableStateOf(DEFAULT_LATLNG)
        private set
    var currentCameraPosition = mutableStateOf(DEFAULT_LATLNG)
        private set
    var currentZoomLevel = mutableDoubleStateOf(DEFAULT_ZOOM_LEVEL)
        private set
    var lottoTypeState = mutableStateListOf(LottoTypeFilter.All.kr)
        private set
    var winStoreState = mutableStateOf(false)
        private set
    var favoriteStoreState = mutableStateOf(false)
        private set

    private var _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow: SharedFlow<String> get() = _snackBarFlow.asSharedFlow()

    fun selectStoreMarker(store: StoreInfo) = storeRepository.selectStore(store.key)
    fun unselectStoreMarker() = storeRepository.unselectStore()
    fun showStoreList() = storeRepository.unselectStore()

    fun changeLottoTypeState(selectedLottoTypes: List<Boolean>) {
        val tempLottoTypeState = mutableListOf<String>()

        if (selectedLottoTypes.all { it }) {
            tempLottoTypeState.add(LottoTypeFilter.All.kr)
        } else {
            selectedLottoTypes.forEachIndexed { index, isSelected ->
                if (isSelected) {
                    tempLottoTypeState.add(LottoTypeFilter.find(index.plus(1)))
                }
            }
        }

        lottoTypeState.clear()
        lottoTypeState.addAll(tempLottoTypeState)

        fetchStoreList()
    }

    fun changeWinStoreState() {
        winStoreState.value = !winStoreState.value

        fetchStoreList()
    }

    fun changeFavoriteStoreState() {
        favoriteStoreState.value = !favoriteStoreState.value

        fetchStoreList()
    }

    fun changeLeftTopPosition(newLeftTopPosition: Pair<Double, Double>) {
        leftTopPosition.value = LatLng(newLeftTopPosition.first, newLeftTopPosition.second)
    }

    fun changeRightBottomPosition(newRightBottomPosition: Pair<Double, Double>) {
        rightBottomPosition.value = LatLng(newRightBottomPosition.first, newRightBottomPosition.second)
    }

    /**
     * 로또 판매점을 가져옵니다.
     */
    fun fetchStoreList() {
        viewModelScope.launch {
            val type = getLottoType()

            val userLocationInfo = StoreInfoRequestBody(
                leftLot = leftTopPosition.value.longitude,
                leftLat = leftTopPosition.value.latitude,
                rightLot = rightBottomPosition.value.longitude,
                rightLat = rightBottomPosition.value.latitude,
                personLot = currentPosition.value.longitude,
                personLat = currentPosition.value.latitude,
            )

            runCatching {
                storeRepository.fetchStoreList(type = type, locationInfo = userLocationInfo)
            }.onFailure { handleException(it) }

//            _uiState.update { MapUiState.Loading }
//
//            storeRepository.stores
//                .onStart {
//                    // 처음으로 진입했을 때에만 로딩 화면 표시
//                    if (isFirstLoading) {
//                        _uiState.update { MapUiState.Loading }
//                        isFirstLoading = false
//                    }
//                }
//                .catch {throwable ->
//                    Log.d("MapVM", throwable.stackTraceToString())
//                    _uiState.update { MapUiState.Failed("네트워크 오류가 발생했습니다.", throwable) }
//                }
//                .collectLatest { collectStoreInfo ->
//                    _uiState.update {
//                        MapUiState.Success(collectStoreInfo)
//                    }
//                }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val type = getLottoType()

            val userLocationInfo = StoreInfoRequestBody(
                leftLot = leftTopPosition.value.longitude,
                leftLat = leftTopPosition.value.latitude,
                rightLot = rightBottomPosition.value.longitude,
                rightLat = rightBottomPosition.value.latitude,
                personLot = currentPosition.value.longitude,
                personLat = currentPosition.value.latitude,
            )

            runCatching {
                storeRepository.fetchNextStoreList(type, userLocationInfo)
            }.onFailure { handleException(it) }
        }
    }

    fun resetStoreList() {
//        viewModelScope.launch {
//            storeRepository.resetStoreList()
//
//            storeRepository.stores
//                .onStart {
//                    // 처음으로 진입했을 때에만 로딩 화면 표시
//                    if (isFirstLoading) {
//                        _uiState.update { MapUiState.Loading }
//                        isFirstLoading = false
//                    }
//                }
//                .catch {throwable ->
//                    Log.d("MapVM", throwable.stackTraceToString())
//                    _uiState.update { MapUiState.Failed("네트워크 오류가 발생했습니다.", throwable) }
//                }
//                .collectLatest { collectStoreInfo ->
//                    _uiState.update {
//                        MapUiState.Success(collectStoreInfo)
//                    }
//                }
//        }
    }

    /**
     * 사용자의 GPS 위치를 변경합니다.
     */
    fun changeCurrentPosition(newCurrentPosition: Pair<Double, Double>) {
        Log.d("MapScreen(VM)", "변경된 사용자 GPS 위치 : ${newCurrentPosition.first} / ${newCurrentPosition.second}")

        currentPosition.value = LatLng(newCurrentPosition.first, newCurrentPosition.second)

        changeCurrentCameraPosition(newCurrentPosition)
    }

    fun changeCurrentCameraPosition(newCurrentCameraPosition: Pair<Double, Double>) {
        Log.d("MapScreen(VM)", "카메라 GPS 위치 변경 : ${newCurrentCameraPosition.first} / ${newCurrentCameraPosition.second}")

        currentCameraPosition.value = LatLng(newCurrentCameraPosition.first, newCurrentCameraPosition.second)
    }

    fun changeCurrentZoomLevel(newZoomLevel: Double) {
        currentZoomLevel.value = newZoomLevel
    }

    fun sendSnackBar(message: String) {
        viewModelScope.launch {
            _snackBarFlow.emit(message)
        }
    }

    private fun getLottoType(): Int {
        return when {
            lottoTypeState.contains(LottoTypeFilter.Lotto720.kr) && lottoTypeState.contains(LottoTypeFilter.Speetto.kr) -> 6
            lottoTypeState.contains(LottoTypeFilter.Lotto645.kr) && lottoTypeState.contains(LottoTypeFilter.Speetto.kr) -> 5
            lottoTypeState.contains(LottoTypeFilter.Lotto645.kr) && lottoTypeState.contains(LottoTypeFilter.Lotto720.kr) -> 4
            lottoTypeState.contains(LottoTypeFilter.Speetto.kr) -> 3
            lottoTypeState.contains(LottoTypeFilter.Lotto720.kr) -> 2
            lottoTypeState.contains(LottoTypeFilter.Lotto645.kr) -> 1
            lottoTypeState.contains(LottoTypeFilter.All.kr) -> 0
            else -> 0
        }
    }

    companion object {
        val DEFAULT_LATLNG = LatLng(37.566499, 126.968555)
        const val DEFAULT_ZOOM_LEVEL = 17.0
    }
}

//sealed interface MapUiState {
//    data object Loading : MapUiState
//    data class Success(val storeInfo: List<StoreInfo>) : MapUiState
//    data class Failed(val message: String, val throwable: Throwable?) : MapUiState
//}