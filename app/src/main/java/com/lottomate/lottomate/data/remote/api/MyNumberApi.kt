package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.model.MyNumberEntity
import retrofit2.http.GET

interface MyNumberApi {
    @GET("my-lotto-numbers")
    suspend fun getAllMyNumbers(): List<MyNumberEntity>
}