package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.remote.api.LottoApi
import com.lottomate.lottomate.data.remote.response.ResponseLottoWinResult
import com.lottomate.lottomate.di.NetworkModule
import com.lottomate.lottomate.domain.repository.LottoRepository
import javax.inject.Inject

class LottoRepositoryImpl @Inject constructor(
    @NetworkModule.DHLottery private val lottoApi: LottoApi,
) : LottoRepository {
    override suspend fun fetchLottoWinResultByRound(round: String): List<Int> {
        val result = lottoApi.fetchLottoResultByRound(
            method = "getLottoNumber",
            drwNo = round,
        )

        return if (result.returnValue == "success") {
            listOf(result.drwtNo1!!, result.drwtNo2!!, result.drwtNo3!!, result.drwtNo4!!, result.drwtNo5!!, result.drwtNo6!!, result.bnusNo!!)
        } else emptyList()
    }
}