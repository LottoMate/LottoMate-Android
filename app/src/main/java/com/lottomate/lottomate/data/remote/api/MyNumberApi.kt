package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.model.MyNumberEntity
import com.lottomate.lottomate.data.remote.request.RegisterLottoNumberEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MyNumberApi {
    @GET("my-lotto-numbers")
    suspend fun getAllMyNumbers(): List<MyNumberEntity>

    @POST("my-lotto-numbers")
    suspend fun insertMyNumber(
        @Body body: RegisterLottoNumberEntity,
    ): Int?

    @DELETE("my-lotto-numbers/{numberId}")
    suspend fun deleteMyNumber(
        @Path("numberId") id: Int,
    ): Int?
}