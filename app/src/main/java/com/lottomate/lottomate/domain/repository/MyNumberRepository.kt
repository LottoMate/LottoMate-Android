package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.domain.model.MyNumber
import com.lottomate.lottomate.domain.model.RegisterLottoNumber
import kotlinx.coroutines.flow.Flow

interface MyNumberRepository {
    val myNumbers: Flow<List<MyNumber>>

    suspend fun getAllMyNumber(): Result<Unit>
    suspend fun insertMyNumber(registerLottoNumber: RegisterLottoNumber): Result<Unit>
    suspend fun deleteMyNumber(id: Int): Result<Unit>
}