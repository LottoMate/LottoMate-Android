package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    val stores: List<StoreInfo>
    val store: StoreInfo?

    fun fetchStores(): Flow<List<StoreInfo>>
    fun fetchStore(): Flow<StoreInfo?>
    fun selectStore(key: Int)
    fun unselectStore()
    fun setFavoriteStore(key: Int)
}