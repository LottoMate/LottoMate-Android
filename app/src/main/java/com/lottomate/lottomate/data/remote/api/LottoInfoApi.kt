package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.remote.response.LottoInfoResponse
import retrofit2.http.GET

interface LottoInfoApi {
    @GET("lottoInfo/home")
    suspend fun getLatestLottoInfo(): LottoInfoResponse
}