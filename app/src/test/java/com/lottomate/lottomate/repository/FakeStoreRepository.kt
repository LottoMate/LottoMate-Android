package com.lottomate.lottomate.repository

import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeStoreRepository : StoreRepository {
    private val _stores = MutableStateFlow<List<StoreInfo>>(emptyList())
    override val stores: StateFlow<List<StoreInfo>>
        get() = _stores.asStateFlow()

    override suspend fun fetchStoreList(
        type: Int,
        locationInfo: StoreInfoRequestBody,
        drwStore: Boolean,
        favorite: Boolean,
        dis: Boolean,
        drwt: Boolean
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun fetchNextStoreList(
        type: Int,
        locationInfo: StoreInfoRequestBody,
        drwStore: Boolean,
        favorite: Boolean,
        dis: Boolean,
        drwt: Boolean
    ): Result<Unit> {
        return Result.success(Unit)

    }


    override fun resetStoreList() {
        _stores.update { emptyList() }
    }

    override fun setFavoriteStore(key: Int) {

    }

    override fun applyStoreFilter(filter: StoreListFilter) {

    }
}