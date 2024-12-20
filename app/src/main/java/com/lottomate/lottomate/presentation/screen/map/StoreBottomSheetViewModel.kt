package com.lottomate.lottomate.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StoreBottomSheetViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {
    val stores: StateFlow<List<StoreInfo>> = storeRepository.stores
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )
    val store: StateFlow<StoreInfo?> = storeRepository.store
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun setFavoriteStore(key: Int) = storeRepository.setFavoriteStore(key)
    fun selectStore(key: Int) = storeRepository.selectStore(key)
    fun unselectStore() = storeRepository.unselectStore()
}