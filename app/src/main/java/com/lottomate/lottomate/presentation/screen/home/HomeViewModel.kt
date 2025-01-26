package com.lottomate.lottomate.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.domain.repository.InterviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository,
) : ViewModel() {
    private var _interviews = MutableStateFlow<List<ResponseInterviewsInfo>?>(null)
    val interviews = _interviews.asStateFlow()

    init {
        loadLatestNoOfInterview()
    }

    private suspend fun loadInterviews(latestInterviewNo: Int) {
        val interviewNumbers = (latestInterviewNo downTo (latestInterviewNo - 5 + 1))
            .toList()
            .sorted()

        interviewRepository.fetchInterviews(interviewNumbers)
            .collectLatest { collectInterviews ->
                _interviews.update { collectInterviews }
            }

    }

    private fun loadLatestNoOfInterview() {
        viewModelScope.launch {
            val latestInterviewNo = async { interviewRepository.fetchLatestNoOfInterview() }

            loadInterviews(latestInterviewNo.await())
        }
    }
}