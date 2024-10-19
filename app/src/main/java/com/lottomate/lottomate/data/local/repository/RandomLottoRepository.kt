package com.lottomate.lottomate.data.local.repository

import com.lottomate.lottomate.data.local.entity.RandomLotto
import kotlinx.coroutines.flow.Flow

interface RandomLottoRepository {
    fun getAllRandomLotto(): Flow<List<RandomLotto>>
    suspend fun insertRandomLotto(randomNumbers: List<Int>)
}