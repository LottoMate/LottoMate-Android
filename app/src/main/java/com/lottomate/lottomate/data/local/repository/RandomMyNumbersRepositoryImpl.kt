package com.lottomate.lottomate.data.local.repository

import com.lottomate.lottomate.data.local.api.RandomMyNumbersDao
import com.lottomate.lottomate.data.mapper.toDomain
import com.lottomate.lottomate.data.mapper.toEntity
import com.lottomate.lottomate.domain.model.RandomMyNumbers
import com.lottomate.lottomate.domain.model.RandomMyNumbersGroup
import com.lottomate.lottomate.domain.repository.local.RandomMyNumbersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RandomMyNumbersRepositoryImpl @Inject constructor(
    private val randomMyNumbersDao: RandomMyNumbersDao,
) : RandomMyNumbersRepository {
    override fun getAllRandomMyNumbers(): Flow<List<RandomMyNumbersGroup>> =
        randomMyNumbersDao.fetchAllRandomMyNumbers()
            .map { entities ->
                entities.groupBy { it.createAt }
                    .map { (date, randomMyNumbers) ->
                        val randomMyNumbersEntities = randomMyNumbers.map { it.toDomain() }
                        RandomMyNumbersGroup(date, randomMyNumbersEntities)
                    }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun insertRandomMyNumbers(randomMyNumbers: RandomMyNumbers): Result<Unit> {
        return try {
            randomMyNumbersDao.insertRandomMyNumbers(randomMyNumbers.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteRandomMyNumbers(key: Int): Result<Unit> {
        return try {
            randomMyNumbersDao.deleteRandomMyNumbers(key)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}