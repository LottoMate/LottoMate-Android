package com.lottomate.lottomate.presentation.screen.map

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val storeRepository: StoreRepository,
) : ViewModel() {
    var lottoTypeState = mutableStateListOf(LottoTypeFilter.All.kr)
        private set
    var winStoreState = mutableStateOf(false)
        private set
    var favoriteStoreState = mutableStateOf(false)

    private var _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> get() = _uiState.asStateFlow()

    init {
        fetchStoreList()
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

    private fun fetchStoreList() {
        viewModelScope.launch {
            val userLocationInfo = StoreInfoRequestBody()

            storeRepository.fetchStoreList(type = 1, locationInfo = userLocationInfo)
                .onStart {
                    _uiState.update { MapUiState.Loading }
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
}

sealed interface MapUiState {
    data object Loading : MapUiState
    data class Success(val storeInfo: List<StoreInfo>) : MapUiState
}