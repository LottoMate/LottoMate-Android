package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.remote.model.StoreInfoRequestBody
import com.lottomate.lottomate.data.remote.response.StoreListResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface StoreApi {
    @POST("store/list")
    suspend fun getStoreList(
        @Query("type") type: Int,
        @Body body: StoreInfoRequestBody,
    ): StoreListResponse
}