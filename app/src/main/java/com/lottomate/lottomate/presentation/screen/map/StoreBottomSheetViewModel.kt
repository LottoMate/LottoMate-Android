package com.lottomate.lottomate.presentation.screen.map

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
    private val storeRepository: StoreRepository,
) : ViewModel() {
    private var _stores = MutableStateFlow<List<StoreInfo>>(emptyList())
    private var _store = MutableStateFlow<StoreInfo?>(null)

    val stores: StateFlow<List<StoreInfo>> get() = _stores.asStateFlow()
    val store: StateFlow<StoreInfo?> get() = _store.asStateFlow()

    init {
        loadAllStores()
    }

    fun selectStore(key: Int) {
        storeRepository.selectStore(key)

        loadAllStores()
    }

    fun setFavoriteStore(key: Int) {
        storeRepository.setFavoriteStore(key)

        loadAllStores()
    }

    fun unselectStore() {
        storeRepository.unselectStore()

        loadAllStores()
    }

    private fun loadAllStores() {
        viewModelScope.launch {
            storeRepository.fetchStores()
                .collectLatest { collectStores ->
                    _stores.update { collectStores }
                }

            storeRepository.fetchStore()
                .collectLatest { collectStore ->
                    _store.update { collectStore }
                }
        }
    }
}