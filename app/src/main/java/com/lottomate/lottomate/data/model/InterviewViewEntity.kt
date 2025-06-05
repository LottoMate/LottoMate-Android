package com.lottomate.lottomate.data.model

import kotlinx.serialization.Serializable

@Serializable
data class InterviewViewEntity(
    val date: String,
    val interviewIds: Set<Int>,
)