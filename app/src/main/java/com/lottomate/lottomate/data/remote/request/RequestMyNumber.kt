package com.lottomate.lottomate.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterLottoNumberEntity(
    @SerialName("lottoType")
    val type: String,
    @SerialName("lottoDrwNo")
    val round: Int,
    @SerialName("lottoNum")
    val lottoNumbers: List<String>,
)