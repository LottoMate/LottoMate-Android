package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor() : StoreRepository {
    private var _stores = StoreInfoMocks.toMutableList()
    private var _store: StoreInfo? = null

    override val stores: List<StoreInfo>
        get() = _stores.toList()
    override val store: StoreInfo?
        get() = _store

    override fun fetchStores(): Flow<List<StoreInfo>> = flow { emit(stores) }
    override fun fetchStore(): Flow<StoreInfo?> = flow { emit(store) }
    override fun selectStore(key: Int) { _store = _stores.firstOrNull { it.key == key } }
    override fun unselectStore() { _store = null }

    override fun setFavoriteStore(key: Int) {
        val store = _stores.first { it.key == key }
        val changeCountLike = if (store.isLike) {
            store.countLike.minus(1)
        } else store.countLike.plus(1)

        _stores.replaceAll {
            if (it.key == key) {
                it.copy(isLike = !store.isLike, countLike = changeCountLike)
            } else {
                it
            }
        }

        if (_store?.key == key) {
            _store = _store?.copy(isLike = !store.isLike, countLike = changeCountLike)
        }
    }
}