package com.lottomate.lottomate.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.home.model.HomeLotto645Info
import com.lottomate.lottomate.presentation.screen.home.model.HomeLotto720Info
import com.lottomate.lottomate.presentation.screen.home.model.HomeLottoInfo
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import com.lottomate.lottomate.utils.LocationStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val lottoInfoRepository: LottoInfoRepository,
    private val interviewRepository: InterviewRepository,
) : BaseViewModel(errorHandler) {
    private var _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState get() = _uiState.asStateFlow()

    var latestLotto645Round = mutableIntStateOf(0)
        private set
    var latestLotto720Round = mutableIntStateOf(0)
        private set

    init {
        loadAllData()
        LocationStateManager.updateLocation()
    }

    /**
     * 홈 화면에 필요한 데이터를 로드합니다.
     */
    private fun loadAllData() {
        viewModelScope.launch {
            runCatching {
                fetchLatestLottoInfo()
                interviewRepository.fetchInterviews()

                observeLottoAndInterviews()
            }.onFailure {
                handleException(it)
            }
        }
    }

    /**
     * 로또 최신 정보를 가져옵니다.
     */
    private suspend fun fetchLatestLottoInfo() {
        withContext(Dispatchers.IO) {
            lottoInfoRepository.fetchAllLatestLottoInfo()
        }
    }

    /**
     * 로또 정보 및 인터뷰 데이터를 Flow에서 감지하고 UI 상태를 업데이트 합니다.
     *
     * @param interviewNumbers 최신 인터뷰 회차부터 이전 인터뷰 회차 리스트 (5회차)
     */
    private fun observeLottoAndInterviews() {
        combine(
            lottoInfoRepository.allLatestLottoInfo,
            interviewRepository.interviews
        ) { lottoInfo, interview ->
            val homeLotto645Info = createLotto645Info(lottoInfo)
            val homeLotto720Info = createLotto720Info(lottoInfo)

            latestLotto645Round.value = homeLotto645Info.round
            latestLotto720Round.value = homeLotto720Info.round

            _uiState.update {
                HomeUiState.Success(
                    lottoInfos = mapOf(
                        LottoType.L645.num to homeLotto645Info,
                        LottoType.L720.num to homeLotto720Info,
                    ),
                    interviews = interview,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun createLotto645Info(lottoInfo: Map<Int, LottoInfo>): HomeLotto645Info {
        val lotto645LatestInfo = lottoInfo[LottoType.L645.num] as Lotto645Info

        return HomeLotto645Info(
            round = lotto645LatestInfo.lottoRound ?: 0,
            date = lotto645LatestInfo.lottoDate?.replace("-", ".") ?: "",
            winnerPrice = lotto645LatestInfo.lottoPrize.first(),
            winnerNumbers = lotto645LatestInfo.lottoNum,
            winnerBonusNumber = lotto645LatestInfo.lottoBonusNum.firstOrNull() ?: 0,
            winnerCount = lotto645LatestInfo.lottoWinnerNum.first(),
        )
    }

    private fun createLotto720Info(lottoInfo: Map<Int, LottoInfo>): HomeLotto720Info {
        val lotto720LatestInfo = lottoInfo[LottoType.L720.num] as Lotto720Info

        return HomeLotto720Info(
            round = lotto720LatestInfo.lottoRound ?: 0,
            date = lotto720LatestInfo.lottoDate.replace("-", ".") ?: "",
            winnerNumbers = lotto720LatestInfo.lottoNum,
        )
    }

    /**
     * 특정 로또 회차에 대한 정보를 가져옵니다.
     */
    fun getLottoInfo(lottoType: LottoType, round: Int) {
        viewModelScope.launch {
            lottoInfoRepository.fetchLottoInfo(lottoType.num, round)
                .onStart {
                    Log.i("HomeViewModel", "getLottoInfo: onStart")
                }
                .map {  lottoInfo ->
                    when (lottoType) {
                        LottoType.L645 -> {
                            val lotto645LatestInfo = lottoInfo as Lotto645Info

                            HomeLotto645Info(
                                round = lotto645LatestInfo.lottoRound ?: 0,
                                date = lotto645LatestInfo.lottoDate ?: "",
                                winnerPrice = lotto645LatestInfo.lottoPrize.first(),
                                winnerNumbers = lotto645LatestInfo.lottoNum,
                                winnerBonusNumber = lotto645LatestInfo.lottoBonusNum.first(),
                                winnerCount = lotto645LatestInfo.lottoWinnerNum.first(),
                            )
                        }
                        else -> {
                            val lotto720LatestInfo = lottoInfo as Lotto720Info

                            HomeLotto720Info(
                                round = lotto720LatestInfo.lottoRound ?: 0,
                                date = lotto720LatestInfo.lottoDate.replace("-", ".") ?: "",
                                winnerNumbers = lotto720LatestInfo.lottoNum,
                            )
                        }
                    }
                }
                .collectLatest { collectData ->
                    _uiState.update { originalUiState ->
                        HomeUiState.Success(
                            lottoInfos =
                                when (lottoType) {
                                    LottoType.L645 -> {
                                        mapOf(
                                            LottoType.L645.num to collectData,
                                            LottoType.L720.num to (originalUiState as HomeUiState.Success).lottoInfos.getValue(LottoType.L720.num)
                                        )
                                    }
                                    else -> {
                                        mapOf(
                                            LottoType.L645.num to (originalUiState as HomeUiState.Success).lottoInfos.getValue(LottoType.L645.num),
                                            LottoType.L720.num to collectData,
                                        )
                                    }
                                },
                            interviews = (originalUiState as HomeUiState.Success).interviews
                        )
                    }
                }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val lottoInfos: Map<Int, HomeLottoInfo>,
        val interviews: List<InterviewUiModel>,
    ) : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
}