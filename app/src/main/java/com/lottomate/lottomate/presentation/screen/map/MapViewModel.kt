package com.lottomate.lottomate.presentation.screen.map

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storeRepository: StoreRepository,
) : ViewModel() {
    private var isFirstLoading = true
    private var leftTopPosition = mutableStateOf(DEFAULT_LATLNG)
    private var rightBottomPosition = mutableStateOf(DEFAULT_LATLNG)
    var currentPosition = mutableStateOf(DEFAULT_LATLNG)
        private set

    var currentZoomLevel = mutableDoubleStateOf(DEFAULT_ZOOM_LEVEL)
        private set
    var lottoTypeState = mutableStateListOf(LottoTypeFilter.All.kr)
        private set
    var winStoreState = mutableStateOf(false)
        private set
    var favoriteStoreState = mutableStateOf(false)
        private set

    private var _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> get() = _uiState.asStateFlow()

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

    fun fetchStoreList() {
        viewModelScope.launch {
            val userLocationInfo = StoreInfoRequestBody(
                leftLot = leftTopPosition.value.latitude,
                leftLat = leftTopPosition.value.longitude,
                rightLot = rightBottomPosition.value.latitude,
                rightLat = rightBottomPosition.value.longitude,
                personLot = currentPosition.value.longitude,
                personLat = currentPosition.value.latitude,
            )

            val type = when {
                lottoTypeState.contains(LottoTypeFilter.Lotto720.kr) && lottoTypeState.contains(LottoTypeFilter.Speetto.kr) -> 6
                lottoTypeState.contains(LottoTypeFilter.Lotto645.kr) && lottoTypeState.contains(LottoTypeFilter.Speetto.kr) -> 5
                lottoTypeState.contains(LottoTypeFilter.Lotto645.kr) && lottoTypeState.contains(LottoTypeFilter.Lotto720.kr) -> 4
                lottoTypeState.contains(LottoTypeFilter.Speetto.kr) -> 3
                lottoTypeState.contains(LottoTypeFilter.Lotto720.kr) -> 2
                lottoTypeState.contains(LottoTypeFilter.Lotto645.kr) -> 1
                lottoTypeState.contains(LottoTypeFilter.All.kr) -> 0
                else -> 0
            }

            storeRepository.fetchStoreList(type = type, locationInfo = userLocationInfo)
                .onStart {
                    // 처음으로 진입했을 때에만 로딩 화면 표시
                    if (isFirstLoading) {
                        _uiState.update { MapUiState.Loading }
                        isFirstLoading = false
                    }
                }
                .catch {
                    Log.d("MapVM", it.stackTraceToString())
                }
                .collectLatest { collectStoreInfo ->
                    _uiState.update {
                        MapUiState.Success(collectStoreInfo.toList())
                    }
                }
        }
    }

    fun changeCurrentPosition(newCurrentPosition: Pair<Double, Double>) {
        currentPosition.value = LatLng(newCurrentPosition.first, newCurrentPosition.second)
    }

    fun changeCurrentZoomLevel(newZoomLevel: Double) {
        currentZoomLevel.value = newZoomLevel
    }

    companion object {
        private val DEFAULT_LATLNG = LatLng(37.566499, 126.968555)
        private const val DEFAULT_ZOOM_LEVEL = 14.0
    }
}

sealed interface MapUiState {
    data object Loading : MapUiState
    data class Success(val storeInfo: List<StoreInfo>) : MapUiState
}