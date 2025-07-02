package com.lottomate.lottomate.presentation.screen.login

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginCompleteViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val interviewRepository: InterviewRepository,
) : BaseViewModel(errorHandler) {
    val interviews = interviewRepository.interviews.value

    init {
        viewModelScope.launch {
            interviewRepository.fetchInterviews()
                .onSuccess {
//                    if (interviews.isEmpty()) {
//                        handleException(Exception("No interviews found"))
//                    }
                }
                .onFailure { handleException(it) }
        }
    }
}