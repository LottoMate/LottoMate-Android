package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.interview.model.InterviewDetailUiModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InterviewRepository {
    val interviews: StateFlow<List<InterviewUiModel>>

    suspend fun fetchInterview(interviewNo: Int): Flow<InterviewDetailUiModel>
    suspend fun fetchInterviews(): Result<Unit>
}