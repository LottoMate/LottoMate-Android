package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import kotlinx.coroutines.flow.StateFlow

interface StoreRepository {
    val stores: StateFlow<List<StoreInfo>>

    suspend fun fetchStoreList(type: Int, locationInfo: StoreInfoRequestBody)
    fun selectStore(key: Int)
    fun unselectStore()
    suspend fun fetchStoreList(type: Int, locationInfo: StoreInfoRequestBody): Result<Unit>
    suspend fun fetchNextStoreList(type: Int, locationInfo: StoreInfoRequestBody): Result<Unit>
    suspend fun fetchStoreList(type: Int, locationInfo: StoreInfoRequestBody, drwStore: Boolean): Result<Unit>
    suspend fun fetchNextStoreList(type: Int, locationInfo: StoreInfoRequestBody, drwStore: Boolean): Result<Unit>
    fun setFavoriteStore(key: Int)
    fun applyStoreFilter(filter: StoreListFilter)
}