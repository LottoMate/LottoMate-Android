package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun fetchStores(): Flow<List<StoreInfo>>
}