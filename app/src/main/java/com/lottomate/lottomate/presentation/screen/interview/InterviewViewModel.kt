package com.lottomate.lottomate.presentation.screen.interview

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewDetailUiModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val interviewRepository: InterviewRepository,
) : BaseViewModel(errorHandler){
    private var _state = MutableStateFlow<InterviewUiState>(InterviewUiState.Loading)
    val state: StateFlow<InterviewUiState> get() = _state.asStateFlow()
    val interviews: StateFlow<List<InterviewUiModel>> = interviewRepository.interviews

    fun getInterview(interviewNo: Int) {
        viewModelScope.launch {
            combine(
                interviewRepository.fetchInterview(interviewNo),
                interviewRepository.interviews
            ) { interview, interviews ->
                InterviewUiState.Success(
                    interview,
                    interviews
                )
            }
                .onStart { _state.update { InterviewUiState.Loading } }
                .catch { handleException(it) }
                .collect { newUiState ->
                    _state.update { newUiState }
                }
        }
    }
}

sealed interface InterviewUiState {
    data object Loading : InterviewUiState
    data class Success(
        val interview: InterviewDetailUiModel,
        val interviews: List<InterviewUiModel>,
    ) : InterviewUiState
}