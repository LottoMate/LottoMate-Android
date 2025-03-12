package com.lottomate.lottomate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreDetail(
    val storeNo: Int,
    val storeNm: String,
    val storeTel: String,
    val storeAddr: String,
    val addrLot: Double,
    val addrLat: Double,
    val lottoTypeList: List<String>
)

@Serializable
data class StoreInfoRequestBody(
    val leftLot: Double,
    val leftLat: Double,
    val rightLot: Double,
    val rightLat: Double,
    val personLot: Double,
    val personLat: Double,
)