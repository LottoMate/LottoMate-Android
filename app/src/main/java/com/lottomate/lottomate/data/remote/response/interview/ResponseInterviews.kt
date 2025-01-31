package com.lottomate.lottomate.data.remote.response.interview

import kotlinx.serialization.Serializable

@Serializable
data class ResponseInterviews(
    val interviews: List<ResponseInterviewsInfo>,
)

@Serializable
data class ResponseInterviewsInfo(
    val reviewNo: Int,
    val reviewHref: Int,
    val reviewTitle: String,
    val reviewThumb: String,
    val intrvDate: String,
    val reviewPlace: String,
)
