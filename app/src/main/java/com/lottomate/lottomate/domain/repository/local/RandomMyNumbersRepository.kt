package com.lottomate.lottomate.domain.repository.local

import com.lottomate.lottomate.domain.model.RandomMyNumbers
import com.lottomate.lottomate.domain.model.RandomMyNumbersGroup
import kotlinx.coroutines.flow.Flow

interface RandomMyNumbersRepository {
    fun getAllRandomMyNumbers(): Flow<List<RandomMyNumbersGroup>>
    suspend fun insertRandomMyNumbers(randomMyNumbers: RandomMyNumbers): Result<Unit>
    suspend fun deleteRandomMyNumbers(key: Int): Result<Unit>
}