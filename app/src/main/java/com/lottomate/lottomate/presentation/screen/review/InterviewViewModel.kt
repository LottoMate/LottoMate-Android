package com.lottomate.lottomate.presentation.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.presentation.screen.review.model.Interview
import com.lottomate.lottomate.presentation.screen.review.model.InterviewMockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(

) : ViewModel(){
    private val _errorFlow = MutableSharedFlow<Throwable>()
    private var _winnerInterviews = MutableStateFlow(InterviewUiState.Loading as InterviewUiState<List<Interview>>)
    private var _interview = MutableStateFlow(InterviewUiState.Loading as InterviewUiState<Interview>)
    val errorFlow get() = _errorFlow.asSharedFlow()
    val interview: StateFlow<InterviewUiState<Interview>> get() = _interview.asStateFlow()
    val winnerInterviews: StateFlow<InterviewUiState<List<Interview>>> get() = _winnerInterviews.asStateFlow()

    init {
        loadInterview()
        loadWinnerInterviews()
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