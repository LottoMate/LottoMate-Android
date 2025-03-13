package com.lottomate.lottomate.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseLottoWinResult(
    val returnValue: String,
    val drwNoDate: String? = null,
    val drwtNo1: Int? = null,
    val drwtNo2: Int? = null,
    val drwtNo3: Int? = null,
    val drwtNo4: Int? = null,
    val drwtNo5: Int? = null,
    val drwtNo6: Int? = null,
    val bnusNo: Int? = null,
)
