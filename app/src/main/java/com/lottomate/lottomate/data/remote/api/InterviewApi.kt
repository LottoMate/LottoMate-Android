package com.lottomate.lottomate.data.remote.api

import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewDetail
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewMaxNo
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviews
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 인터뷰 관련 api interface 입니다.
 */
interface InterviewApi {
    @GET("/review/max")
    suspend fun getLatestNoOfInterview(): Int

    @GET("/review/list")
    suspend fun getInterviews(
        @Query("reviewNoList") reviewNoList: String,
    ): List<ResponseInterviewsInfo>

    @GET("/review/{no}")
    suspend fun getInterviewDetail(
        @Path("no") interviewNo: Int,
    ): ResponseInterviewDetail
}