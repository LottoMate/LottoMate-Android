package com.lottomate.lottomate.data.remote.response

import com.lottomate.lottomate.data.remote.model.StoreInfoEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreListResponse(
    override val code: Int,
    override val message: String,
    @SerialName("store_info") val storeInfoList: List<StoreInfoEntity>,
) : BaseResponse