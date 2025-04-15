package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUIModel
import kotlinx.coroutines.flow.Flow

interface InterviewRepository {
    suspend fun fetchLatestNoOfInterview(): Int
    suspend fun fetchInterviews(interviewNo: List<Int>): Flow<List<ResponseInterviewsInfo>>
    suspend fun fetchInterview(interviewNo: Int): Flow<InterviewUIModel>
}