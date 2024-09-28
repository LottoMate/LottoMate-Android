package com.lottomate.lottomate.presentation.screen.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreBottomSheetViewModel @Inject constructor(
    storeRepository: StoreRepository,
) : ViewModel() {
    private var _stores = MutableStateFlow<List<StoreInfo>>(emptyList())
    var store = mutableStateOf<StoreInfo?>(null)
        private set
    val stores: StateFlow<List<StoreInfo>> get() = _stores.asStateFlow()

    init {
        viewModelScope.launch {
            storeRepository.fetchStores()
                .collectLatest { collectStores ->
                    _stores.update { collectStores }
                }
        }
    }

    fun selectStore(selectedStore: StoreInfo) {
        store.value = selectedStore
    }
}