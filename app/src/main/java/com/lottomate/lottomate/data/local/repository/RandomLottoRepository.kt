package com.lottomate.lottomate.data.local.repository

import kotlinx.coroutines.flow.Flow

interface RandomLottoRepository {
    fun getAllRandomLotto(): Flow<List<List<Int>>>
    suspend fun insertRandomLotto(randomNumbers: List<Int>)
}