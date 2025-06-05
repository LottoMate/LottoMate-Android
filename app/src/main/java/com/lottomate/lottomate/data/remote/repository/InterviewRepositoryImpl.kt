package com.lottomate.lottomate.data.remote.repository

import android.util.Log
import com.lottomate.lottomate.data.manager.InterviewViewManager
import com.lottomate.lottomate.data.mapper.toUIModel
import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.data.remote.api.InterviewApi
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewDetailUiModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class InterviewRepositoryImpl @Inject constructor(
    private val interviewApi: InterviewApi,
    private val interviewViewManager: InterviewViewManager,
) : InterviewRepository {
    private val _interviews = MutableStateFlow<List<InterviewUiModel>>(emptyList())
    override val interviews: StateFlow<List<InterviewUiModel>>
        get() = _interviews.asStateFlow()

    override suspend fun fetchInterviews(): Result<Unit> {
        return try {
            val interviewLatestNo = fetchLatestInterviewNo().getOrThrow()
            val interviewNo = interviewViewManager.getUnviewedInterviewIds(interviewLatestNo)

            val interviewNoToString = interviewNo.joinToString(",")
            val response = interviewApi.getInterviews(interviewNoToString)

            _interviews.update { response.map { it.toUiModel() }.sortedByDescending { it.no } }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchInterview(interviewNo: Int): Flow<InterviewDetailUiModel> = flow {
        val result = interviewApi.getInterviewDetail(interviewNo)
        interviewViewManager.addInterviewId(result.reviewNo)

        fetchInterviews()
        emit(result.toUIModel())
    }

    private suspend fun fetchLatestInterviewNo(): Result<Int> {
        return try {
            val result = interviewApi.getLatestNoOfInterview()

            Result.success(result)
        } catch (e: Exception) {
            Log.d("InterviewRepo", "fetchLatestNoOfInterview: $e")
            Result.failure(e)
        }
    }
}