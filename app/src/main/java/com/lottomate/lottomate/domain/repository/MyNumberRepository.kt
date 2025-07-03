package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.domain.model.MyNumber
import kotlinx.coroutines.flow.Flow

interface MyNumberRepository {
    val myNumbers: Flow<List<MyNumber>>

    suspend fun getAllMyNumber(): Result<Unit>
}