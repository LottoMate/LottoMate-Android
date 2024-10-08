package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor() : StoreRepository {
    private var _stores = MutableStateFlow(StoreInfoMocks)
    private var _store = MutableStateFlow<StoreInfo?>(null)

    override val stores: StateFlow<List<StoreInfo>> get() = _stores.asStateFlow()
    override val store: StateFlow<StoreInfo?> get() = _store.asStateFlow()

    override fun selectStore(key: Int) {
        _store.update { _stores.value.firstOrNull { it.key == key } }
    }
    override fun unselectStore() = _store.update { null }

    override fun setFavoriteStore(key: Int) {
        val store = _stores.value.first { it.key == key }
        val changeCountLike = if (store.isLike) {
            store.countLike.minus(1)
        } else store.countLike.plus(1)

        _stores.update {
            StoreInfoMocks.map {
                if (it.key == key) {
                    it.copy(isLike = !store.isLike, countLike = changeCountLike)
                } else {
                    it
                }
            }
        }

        if (_store.value?.key == key) {
            _store.update { currentStore ->
                currentStore?.copy(isLike = !store.isLike, countLike = changeCountLike)
            }
        }
    }
}