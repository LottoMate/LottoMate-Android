package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.remote.api.TestApi
import com.lottomate.lottomate.domain.repository.TestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testApi: TestApi
) : TestRepository {
    override suspend fun getTest(): Flow<String> = flow {
        val result = testApi.getTest()

        if (result.isSuccessful) {
            result.body()?.let {
                emit(it.string())
            }
        }
    }
}