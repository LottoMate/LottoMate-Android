package com.lottomate.lottomate.domain.repository

import kotlinx.coroutines.flow.Flow

interface TestRepository {
    suspend fun getTest(): Flow<String>
}