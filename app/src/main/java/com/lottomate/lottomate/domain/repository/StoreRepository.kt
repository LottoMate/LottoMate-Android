package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import kotlinx.coroutines.flow.StateFlow

interface StoreRepository {
    val stores: StateFlow<List<StoreInfo>>
    val store: StateFlow<StoreInfo?>

    fun selectStore(key: Int)
    fun unselectStore()
    fun setFavoriteStore(key: Int)
}