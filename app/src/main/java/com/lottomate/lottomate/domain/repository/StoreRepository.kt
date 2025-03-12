package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    val stores: Flow<List<StoreInfo>>
    val store: Flow<StoreInfo?>

    suspend fun fetchStoreList(type: Int, locationInfo: StoreInfoRequestBody)
    fun selectStore(key: Int)
    fun unselectStore()
    fun setFavoriteStore(key: Int)
    fun applyStoreFilter(filter: StoreListFilter)
}