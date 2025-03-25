package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomLottoNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


/**
 * 로또 번호 관련 Repository
 */
interface LottoNumberRepository {
    val lottoNumbers: StateFlow<List<RandomLottoNumber>>

    suspend fun getLottoNumbers(): Flow<List<RandomLottoNumber>>
    suspend fun saveLottoNumber(lottoNumbers: List<Int>)
    suspend fun removeLottoNumber(id: Int)
}