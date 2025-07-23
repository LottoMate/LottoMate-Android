package com.lottomate.lottomate.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyNumber<T>(
    override val code: Int,
    override val message: String,
    val result: T,
) : BaseResponse