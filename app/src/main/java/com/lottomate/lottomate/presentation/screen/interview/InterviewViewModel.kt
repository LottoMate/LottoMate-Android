package com.lottomate.lottomate.presentation.screen.interview

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
) : BaseViewModel(errorHandler){
    private var _winnerInterviews = MutableStateFlow(InterviewUiState.Loading as InterviewUiState<List<InterviewUIModel>>)
    private var _interview = MutableStateFlow(InterviewUiState.Loading as InterviewUiState<InterviewUIModel>)
    val interview: StateFlow<InterviewUiState<InterviewUIModel>> get() = _interview.asStateFlow()
    val winnerInterviews: StateFlow<InterviewUiState<List<InterviewUIModel>>> get() = _winnerInterviews.asStateFlow()

    init {
        runCatching {
            loadInterview()
            loadWinnerInterviews()
        }.onFailure { handleException(it) }
    }

    private fun loadInterview() {
        viewModelScope.launch {
            _interview.update {
                InterviewUiState.Success(InterviewMockData)
            }
        }
    }

    private fun loadWinnerInterviews() {
        viewModelScope.launch {
            _winnerInterviews.update {
                InterviewUiState.Success(List(5) { InterviewMockData })
            }
        }
    }
}

sealed interface InterviewUiState<out T> {
    data object Loading: InterviewUiState<Nothing>
    data class Success<T> (val data: T): InterviewUiState<T>
}