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
        @Query("drwtStore") drwtStore: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("like") favorite: Boolean = false,
        @Query("dis") dis: Boolean = true,
        @Query("drwt") drwt: Boolean = false,
        @Body body: StoreInfoRequestBody,
    ): StoreListResponse
}