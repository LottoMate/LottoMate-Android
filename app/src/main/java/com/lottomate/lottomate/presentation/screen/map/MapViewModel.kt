package com.lottomate.lottomate.presentation.screen.map

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(

) : ViewModel() {
    var lottoTypeState = mutableStateListOf(LottoTypeFilter.All.kr)
        private set
    var winStoreState = mutableStateOf(false)
        private set
    var favoriteStoreState = mutableStateOf(false)
        private set
    private var _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            MapUiState.Success(StoreInfoMocks)
        }
    }

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