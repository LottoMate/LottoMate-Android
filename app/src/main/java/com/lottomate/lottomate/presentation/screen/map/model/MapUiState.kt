package com.lottomate.lottomate.presentation.screen.map.model

import com.lottomate.lottomate.presentation.screen.map.MapViewModel
import com.naver.maps.geometry.LatLng

sealed interface MapUiState {
    data class State(
        val isLoading: Boolean = true,
        val stores: List<StoreInfo> = emptyList(),
        val mapState: MapState = MapState(),
        val isWinStores: Boolean = false,
        val isFavoriteStores: Boolean = false,
        val showLottoTypeSelectionBottomSheet: Boolean = false,
    ) : MapUiState

    data class MapState(
        val zoomLevel: Double = MapViewModel.DEFAULT_ZOOM_LEVEL,
        val userLocation: LatLng = MapViewModel.DEFAULT_LATLNG,
        val viewportTopLeft: LatLng = MapViewModel.DEFAULT_LATLNG,
        val viewportBottomRight: LatLng = MapViewModel.DEFAULT_LATLNG,
    ) : MapUiState
}