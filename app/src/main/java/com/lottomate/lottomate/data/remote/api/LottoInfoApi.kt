package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.remote.response.LottoInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LottoInfoApi {
    @GET("lottoInfo/home")
    suspend fun getLatestLottoInfo(): LottoInfoResponse

    @GET("lottoInfo/{lottoType}/{lottoRndNum}")
    suspend fun fetchLottoInfo(
        @Path("lottoType") lottoType: Int,
        @Path("lottoRndNum") lottoRndNum: Int,
    ): LottoInfoResponse
}