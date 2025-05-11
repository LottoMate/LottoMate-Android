package com.lottomate.lottomate.presentation.screen.interview

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val interviewRepository: InterviewRepository,
) : BaseViewModel(errorHandler){
    private var _winnerInterviews = MutableStateFlow(InterviewUiState.Loading as InterviewUiState<List<InterviewUIModel>>)
    private var _interview = MutableStateFlow(InterviewUiState.Loading as InterviewUiState<InterviewUIModel>)
    val interview: StateFlow<InterviewUiState<InterviewUIModel>> get() = _interview.asStateFlow()
    val winnerInterviews: StateFlow<InterviewUiState<List<InterviewUIModel>>> get() = _winnerInterviews.asStateFlow()

    fun getInterview(interviewNo: Int) {
        runCatching {
            viewModelScope.launch {
                interviewRepository.fetchInterview(interviewNo)
                    .collectLatest { collectData ->
                        _interview.update {
                            InterviewUiState.Success(collectData)
                        }
                    }
            }
        }.onFailure {
            Log.d("InterviewViewModel", it.stackTraceToString())
            handleException(it)
        }
    }
}

sealed interface InterviewUiState<out T> {
    data object Loading: InterviewUiState<Nothing>
    data class Success<T> (val data: T): InterviewUiState<T>
}