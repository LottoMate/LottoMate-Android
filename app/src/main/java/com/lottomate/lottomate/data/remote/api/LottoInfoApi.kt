package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.remote.response.LottoInfoResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LottoInfoApi {
    @GET("lottoInfo/home")
    suspend fun getAllLatestLottoInfo(): LottoInfoResponse

    @GET("lottoInfo/{lottoType}/{lottoRndNum}")
    suspend fun fetchLottoInfo(
        @Path("lottoType") lottoType: Int,
        @Path("lottoRndNum") lottoRndNum: Int,
    ): LottoInfoResponse

    @GET("oauth2/authorization/google")
    suspend fun googleLogin(): Response<ResponseBody>
}