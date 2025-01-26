package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.remote.api.InterviewApi
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.domain.repository.InterviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InterviewRepositoryImpl @Inject constructor(
    private val interviewApi: InterviewApi,
) : InterviewRepository {
    override suspend fun fetchLatestNoOfInterview(): Int {
        val result = interviewApi.getLatestNoOfInterview()

        return result
    }

    override suspend fun fetchInterviews(interviewNo: List<Int>): Flow<List<ResponseInterviewsInfo>> = flow {
        val interviewNoToString = interviewNo.joinToString(",")
        val interviews = interviewApi.getInterviews(interviewNoToString)

        emit(interviews)
    }
}