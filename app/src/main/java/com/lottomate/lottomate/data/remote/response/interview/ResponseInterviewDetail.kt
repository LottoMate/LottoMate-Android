package com.lottomate.lottomate.data.remote.response.interview

import kotlinx.serialization.Serializable

/**
 * 인터뷰 한 회차에 대한 정보를 가져올 때 사용하는 Response Model 입니다.
 */
@Serializable
data class ResponseInterviewDetail(
    val reviewNo: Int,
    val reviewHref: Int,
    val reviewTitle: String,
    val reviewCont: String,
    val reviewThumb: String,
    val reviewImg: List<String>,
    val intrvDate: String,
    val reviewDate: String,
    val lottoDrwNo: Int,
    val storeNo: Int,
    val storeAddr: String,
)
