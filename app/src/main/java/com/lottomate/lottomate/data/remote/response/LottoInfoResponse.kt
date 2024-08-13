package com.lottomate.lottomate.data.remote.response

import com.lottomate.lottomate.data.model.Lotto645InfoEntity
import com.lottomate.lottomate.data.model.Lotto720InfoEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LottoInfoResponse(
    override val code: Int,
    override val message: String,
    @SerialName("645") val lotto645: Lotto645InfoEntity? = null,
    @SerialName("720") val lotto720: Lotto720InfoEntity? = null,
): BaseResponse