package com.lottomate.lottomate.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LatestLoginInfo(
    val type: LoginType,
    val date: Long,
)