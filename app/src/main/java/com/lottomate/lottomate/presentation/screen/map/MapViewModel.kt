package com.lottomate.lottomate.presentation.screen.map

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {
    var lottoTypeState = mutableStateListOf(LottoTypeFilter.All.kr)
        private set
    var winStoreState = mutableStateOf(false)
        private set
    var favoriteStoreState = mutableStateOf(false)

    private var _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> get() = _uiState.asStateFlow()
    val selectStore: StateFlow<StoreInfo?> get() = storeRepository.store
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    init {
        _uiState.update {
            MapUiState.Success(StoreInfoMocks)
        }
    }

    fun selectStoreMarker(store: StoreInfo) = storeRepository.selectStore(store.key)
    fun unselectStoreMarker() = storeRepository.unselectStore()
    fun showStoreList() = storeRepository.unselectStore()

    fun changeLottoTypeState(selectedLottoTypes: List<Boolean>) {
        val tempLottoTypeState = mutableListOf<String>()

        if (selectedLottoTypes.all { !it }) {
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
    }

    fun changeWinStoreState() {
        winStoreState.value = !winStoreState.value
    }

    fun changeFavoriteStoreState() {
        favoriteStoreState.value = !favoriteStoreState.value
    }
}

sealed interface MapUiState {
    data object Loading : MapUiState
    data class Success(val storeInfo: List<StoreInfo>) : MapUiState
}