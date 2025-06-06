package com.lottomate.lottomate.data.remote.repository

import android.util.Log
import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.data.remote.api.StoreApi
import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMocks
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeApi: StoreApi,
) : StoreRepository {
    private var _stores = MutableStateFlow<List<StoreInfo>>(emptyList())

    override val stores: StateFlow<List<StoreInfo>> get() = _stores.asStateFlow()

    private var currentPage = 1
    private var endReached = false

    override suspend fun fetchStoreList(
        type: Int,
        locationInfo: StoreInfoRequestBody,
        drwStore: Boolean,
        favorite: Boolean,
        dis: Boolean,
        drwt: Boolean,
    ): Result<Unit> {
        return try {
            val result = storeApi.getStoreList(
                type = type,
                body = locationInfo,
                drwtStore = drwStore,
                favorite = favorite,
                dis = dis,
                drwt = drwt,
            )

            if (result.code == 200) {
                val stores = result.storeInfoList.content.map { it.toUiModel() }


                if (result.storeInfoList.totalPages == currentPage) {
                    endReached = true
                } else {
                    currentPage = 2
                    endReached = false
                }

                Log.d("StoreRepositoryImpl", "판매점 목록 가져오기 성공")
                _stores.update { stores.sortedBy { it.distance } }
                Result.success(Unit)
            } else {
                // TODO : 오류 발생 case
                _stores.update { emptyList() }
                Log.d("StoreRepositoryImpl", "판매점 목록 가져오기 실패")
                Result.failure(IllegalArgumentException("판매점 데이터를 가져오는 데 실패하였습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchNextStoreList(
        type: Int,
        locationInfo: StoreInfoRequestBody,
        drwStore: Boolean,
        favorite: Boolean,
        dis: Boolean,
        drwt: Boolean,
    ): Result<Unit> {
        if (endReached) return Result.success(Unit)

        return try {
            val response = storeApi.getStoreList(
                type = type,
                page = currentPage,
                body = locationInfo,
                drwtStore = drwStore,
                favorite = favorite,
                dis = dis,
                drwt = drwt,
            )

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

    override fun resetStoreList() {
        _stores.update { emptyList() }
    }

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

//        if (_store.value?.key == key) {
//            _store.update { currentStore ->
//                currentStore?.copy(isLike = !store.isLike, countLike = changeCountLike)
//            }
//        }
    }

    override fun applyStoreFilter(filter: StoreListFilter) {
        when (filter) {
            StoreListFilter.DISTANCE -> {
                _stores.update { _stores.value.sortedBy { it.distance } }
            }
            StoreListFilter.RANK -> {
                _stores.update { _stores.value.sortedByDescending { it.getCountLotto645().plus(it.getCountLotto720()).plus(it.getCountSpeetto()) } }
            }
        }
    }
}