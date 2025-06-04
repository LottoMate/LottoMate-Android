package com.lottomate.lottomate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreDetailEntity(
    val storeNo: Int = 0,
    val storeNm: String = "",
    val storeTel: String = "",
    val storeAddr: String = "",
    val addrLot: Double = 0.0,
    val addrLat: Double = 0.0,
    val distance: String = "",
    val lottoTypeList: List<String> = emptyList(),
    val lottoInfos: List<LottoInfoEntity> = emptyList(),
)

@Serializable
data class LottoInfoEntity(
    val lottoType: String = "",
    val place: Long = 0,
    val lottoJackpot: Long? = null,
    val drwNum: Int = 0,
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