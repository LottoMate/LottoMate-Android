package com.lottomate.lottomate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreInfoEntity(
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
    val leftLot: Double = 127.017424,
    val leftLat: Double = 37.499954,
    val rightLot: Double = 127.038351,
    val rightLat: Double = 37.494208,
    val personLot: Double = 127.027619,
    val personLat: Double = 37.497952,
)