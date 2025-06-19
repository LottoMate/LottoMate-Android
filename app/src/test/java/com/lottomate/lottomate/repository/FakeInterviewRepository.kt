package com.lottomate.lottomate.repository

import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewDetailUiModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FakeInterviewRepository : InterviewRepository {
    private val _interviews = MutableStateFlow<List<InterviewUiModel>>(emptyList())
    override val interviews: StateFlow<List<InterviewUiModel>>
        get() = _interviews.asStateFlow()

    override suspend fun fetchInterview(interviewNo: Int): Flow<InterviewDetailUiModel> = flow {

    }

    override suspend fun fetchInterviews(): Result<Unit> {
        return try {
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}