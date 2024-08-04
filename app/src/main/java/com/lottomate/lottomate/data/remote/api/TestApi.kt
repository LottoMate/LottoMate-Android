package com.lottomate.lottomate.data.remote.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface TestApi {
    @GET("test/hello")
    suspend fun getTest(): Response<ResponseBody>
}