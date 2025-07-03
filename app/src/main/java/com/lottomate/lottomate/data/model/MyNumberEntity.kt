package com.lottomate.lottomate.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MyNumberEntity(
    val memberLottoNo: Int,
    val lottoType: String,
    val lottoDrwNo: Int,
    val lottoNum: List<Int>,
    val winnYn: Boolean,
)