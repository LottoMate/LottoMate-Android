package com.lottomate.lottomate.data.local.repository

import com.lottomate.lottomate.data.local.entity.RandomLotto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface RandomLottoRepository {
    val dataChanged: SharedFlow<Unit>

    suspend fun fetchAllRandomLotto(): List<RandomLotto>
    fun fetchAllRandomLottoOnlyNumbers(): Flow<List<List<Int>>>
    suspend fun insertRandomLotto(randomNumbers: List<Int>)
    suspend fun deleteAllRandomLotto()
    suspend fun deleteOneOfRandomLotto(key: Int)
}