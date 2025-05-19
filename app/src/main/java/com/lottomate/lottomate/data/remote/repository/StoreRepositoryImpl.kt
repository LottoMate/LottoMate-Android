package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.data.remote.api.StoreApi
import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeApi: StoreApi,
) : StoreRepository {
    private var _stores = MutableStateFlow<List<StoreInfo>>(emptyList())
    private var _store = MutableStateFlow<StoreInfo?>(null)

    override val stores: StateFlow<List<StoreInfo>> get() = _stores.asStateFlow()

    override suspend fun fetchStoreList(type: Int, locationInfo: StoreInfoRequestBody) {
        val result = storeApi.getStoreList(type = type, body = locationInfo)
    private var currentPage = 1
    private var endReached = false

    override suspend fun fetchStoreList(type: Int, locationInfo: StoreInfoRequestBody, drwStore: Boolean): Result<Unit> {
        return try {
            val result = storeApi.getStoreList(type = type, body = locationInfo, drwtStore = drwStore)

            if (result.code == 200) {
                val stores = result.storeInfoList.content.map { it.toUiModel() }

                _stores.update { stores.sortedBy { it.distance } }
                if (result.storeInfoList.totalPages == currentPage) {
                    endReached = true
                } else {
                    currentPage = 2
                    endReached = false
                }
                Result.success(Unit)
            } else {
                // TODO : 오류 발생 case
                _stores.update { emptyList() }
                Result.failure(IllegalArgumentException("판매점 데이터를 가져오는 데 실패하였습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun selectStore(key: Int) {
        _store.update { _stores.value.firstOrNull { it.key == key } }
    override suspend fun fetchNextStoreList(
        type: Int,
        locationInfo: StoreInfoRequestBody,
        drwStore: Boolean,
    ): Result<Unit> {
        if (endReached) return Result.success(Unit)

        return try {
            val response = storeApi.getStoreList(type = type, page = currentPage, body = locationInfo, drwtStore = drwStore)

            if (response.code == 200) {
                if (response.storeInfoList.totalPages == currentPage) {
                    endReached = true
                } else {
                    endReached = false
                    currentPage++
                }

                val stores = response.storeInfoList.content.map { it.toUiModel() }.sortedBy { it.distance }
                _stores.update { it + stores }

                Result.success(Unit)
            } else {
                Result.failure(IllegalArgumentException("판매점 데이터를 가져오는 데 실패하였습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    override fun applyStoreFilter(filter: StoreListFilter) {
//        when (filter) {
//            StoreListFilter.DISTANCE -> {
//                _stores.update { stores.value.sortedBy { it.distance } }
//            }
//            StoreListFilter.RANK -> {
//                _stores.update { stores.value.sortedByDescending { it.getCountLotto645().plus(it.getCountLotto720()).plus(it.getCountSpeetto()) } }
//            }
//        }
    }
}