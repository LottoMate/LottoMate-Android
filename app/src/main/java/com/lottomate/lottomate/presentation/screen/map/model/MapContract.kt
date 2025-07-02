package com.lottomate.lottomate.presentation.screen.map.model

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.screen.map.MapViewModel
import com.naver.maps.geometry.LatLng

sealed interface MapContract {
    data class State(
        val isLoading: Boolean = true,
        val myLocation: LatLng? = null,
        val stores: List<StoreInfo> = emptyList(),
        val selectedStore: StoreInfo? = null,
        val selectedLotteryType: List<LottoType.Group> = listOf(LottoType.Group.ALL),
        val selectedStoreListFilter: StoreListFilter = StoreListFilter.DISTANCE,
        val mapState: MapState = MapState(),
        val isWinStores: Boolean = false,
        val isFavoriteStores: Boolean = false,
        val isInSeoul: Boolean = false,
        val isRefreshAvailable: Boolean = false,
        val showInitPopupBottomSheet: Boolean = false,
        val showNonSeoulSnackBar: Boolean = false,
        val showRefreshToolTip: Boolean = false,
        val showNoStoresSnackBar: Boolean = false,
    ) : MapContract

    data class MapState(
        val viewportTopLeft: LatLng = MapViewModel.TOP_LEFT_DEFAULT_LATLNG,
        val viewportBottomRight: LatLng = MapViewModel.BOTTOM_RIGHT_DEFAULT_LATLNG,
    ) : MapContract

    sealed interface Event {
        data object ClickLotterySelection : Event
        data class ClickMyLocation(val currentMyLocation: LatLng) : Event
        data class ClickRefresh(val currentCameraCenter: LatLng) : Event
        data class ClickExploreMap(val topLeft: LatLng, val bottomRight: LatLng) : Event
        data object ClickWinStores : Event
        data object ClickFavoriteStores : Event
        data class SelectStoreListFilter(val selectedFilter: StoreListFilter) : Event
        data class SelectStore(val store: StoreInfo) : Event
        data object DeselectStore : Event
        data class MoveCamera(val center: LatLng) : Event
        data class SelectLotteryType(val selectedType: List<LottoType.Group>) : Event
    }

    sealed interface Effect : MapContract {
        data class ShowSnackBar(val message: String) : Effect
        data object ShowLocationPermissionDialog : Effect
        data object ShowLotterySelectionBottomSheet : Effect
        data object ShowLogin : Effect
    }
}