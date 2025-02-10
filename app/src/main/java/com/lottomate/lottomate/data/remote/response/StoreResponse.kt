package com.lottomate.lottomate.data.remote.response

import com.lottomate.lottomate.data.remote.model.StoreDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreListResponse(
    override val code: Int,
    override val message: String,
    @SerialName("store_info") val storeInfoList: StoreInfoEntity,
) : BaseResponse

@Serializable
data class StoreInfoEntity(
    val pageNum: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalElements: Int,
    val content: List<StoreDetail>,
)