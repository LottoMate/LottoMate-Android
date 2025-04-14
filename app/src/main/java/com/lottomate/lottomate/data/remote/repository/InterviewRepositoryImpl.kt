package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.mapper.toUIModel
import com.lottomate.lottomate.data.remote.api.InterviewApi
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUIModel
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

    override suspend fun fetchInterview(interviewNo: Int): Flow<InterviewUIModel> = flow {
        val result = interviewApi.getInterviewDetail(interviewNo)

        emit(result.toUIModel())
    }
}