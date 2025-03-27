package com.lottomate.lottomate.presentation.screen.lounge

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoungeViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val interviewRepository: InterviewRepository,
) : BaseViewModel(errorHandler) {

    private var _interviews = MutableStateFlow<List<ResponseInterviewsInfo>>(emptyList())
    val interviews: StateFlow<List<ResponseInterviewsInfo>> get() = _interviews.asStateFlow()

    init {
        loadInterviews()
    }

    private fun loadInterviews() {
        viewModelScope.launch {
            runCatching {
                val interviewNumbers = getInterviewNumbers()

                interviewRepository.fetchInterviews(interviewNumbers)
                    .catch { handleException(it) }
                    .collectLatest { collectData ->
                        _interviews.update { collectData }
                    }
            }.onFailure { handleException(it) }
        }
    }

    /**
     * 최신 인터뷰 회차를 가져온 후, 이전 인터뷰 5회차 리스트를 생성합니다.
     *
     * @return 이전 인터뷰 5회차 리스트
     */
    private suspend fun getInterviewNumbers(): List<Int> {
        val latestInterviewNo = withContext(Dispatchers.IO) {
            interviewRepository.fetchLatestNoOfInterview()
        }

        return (latestInterviewNo downTo (latestInterviewNo - 5 + 1))
            .toList()
            .sorted()
    }
}