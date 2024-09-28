package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor() : StoreRepository {
    override fun fetchStores(): Flow<List<StoreInfo>> = flow {
        emit(StoreInfoMocks)
    }
}