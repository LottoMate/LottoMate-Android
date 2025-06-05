package com.lottomate.lottomate.presentation.screen.lounge

import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoungeViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val interviewRepository: InterviewRepository,
) : BaseViewModel(errorHandler) {
    val interviews: StateFlow<List<InterviewUiModel>> = interviewRepository.interviews
}