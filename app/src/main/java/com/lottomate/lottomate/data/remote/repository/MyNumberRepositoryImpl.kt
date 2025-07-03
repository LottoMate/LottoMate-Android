package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.mapper.toDomain
import com.lottomate.lottomate.data.remote.api.MyNumberApi
import com.lottomate.lottomate.domain.model.MyNumber
import com.lottomate.lottomate.domain.repository.MyNumberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MyNumberRepositoryImpl @Inject constructor(
    private val myNumberApi: MyNumberApi,
) : MyNumberRepository {
    private val _myNumbers = MutableStateFlow<List<MyNumber>>(emptyList())
    override val myNumbers: Flow<List<MyNumber>>
        get() = _myNumbers.asStateFlow()

    override suspend fun getAllMyNumber(): Result<Unit> {
        return try {
            val response = myNumberApi.getAllMyNumbers()
            _myNumbers.update { response.map { it.toDomain() } }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}