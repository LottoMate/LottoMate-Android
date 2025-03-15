package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.remote.response.ResponseLottoWinResult
import retrofit2.http.GET
import retrofit2.http.Query

interface LottoApi {
    @GET("common.do")
    suspend fun fetchLottoResultByRound(
        @Query("method") method: String,
        @Query("drwNo") drwNo: String,
    ): ResponseLottoWinResult
}