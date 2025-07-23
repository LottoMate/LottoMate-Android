package com.lottomate.lottomate.presentation.screen.scanResult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.mapper.toScanMyNumber
import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.di.DispatcherModule
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.domain.repository.MyNumberRepository
import com.lottomate.lottomate.domain.usecase.CheckLotteryResultUseCase
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.scanResult.contract.LotteryResultEffect
import com.lottomate.lottomate.presentation.screen.scanResult.contract.LottoScanResultUiState
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryInputType
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryResultFrom
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.presentation.screen.scanResult.model.ScanResultUiModel
import com.lottomate.lottomate.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LottoScanResultViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val lottoInfoRepository: LottoInfoRepository,
    private val myNumberRepository: MyNumberRepository,
    private val checkLotteryResultUseCase: CheckLotteryResultUseCase,
    private val interviewRepository: InterviewRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel(errorHandler) {
    private val handler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)

        handleException(throwable)
    }
    val interviews = interviewRepository.interviews.value

    private val _effect = Channel<LotteryResultEffect>()
    val effect = _effect.receiveAsFlow()

    private var _state = MutableStateFlow<LottoScanResultUiState>(LottoScanResultUiState.Loading)
    val state: StateFlow<LottoScanResultUiState> get() = _state.asStateFlow()

    init {
        val from = savedStateHandle.get<LotteryResultFrom>("from")
        val inputType = savedStateHandle.get<LotteryInputType>("inputType")
        val myLotto = savedStateHandle.get<MyLottoInfo>("myLotto")


        if (from != null && inputType != null && myLotto != null) {
            loadLotteryResult(from, inputType, myLotto)
        }
    }

                if (!isAnnouncement) {
                    val week = if (lottoType == LottoType.L645) Calendar.SATURDAY else Calendar.THURSDAY
                    val remainDays = DateUtils.getDaysUntilNextDay(week)
    private fun loadLotteryResult(from: LotteryResultFrom, inputType: LotteryInputType, myLotto: MyLottoInfo) {
        viewModelScope.launch(handler) {
            if (from == LotteryResultFrom.SCAN) {
                val latestLotto645RoundInfo = lottoInfoRepository.latestLottoRoundInfo.first().getValue(LottoType.L645.num)
                val latestLotto720RoundInfo = lottoInfoRepository.latestLottoRoundInfo.first().getValue(LottoType.L720.num)

                myLotto.myLotto645Info?.let { lotto645 ->
                    val notYet = checkAnnouncement(latestLotto645RoundInfo.round, LottoType.L645, lotto645.round)
                    val isExpired = checkExpire(latestLotto645RoundInfo, LottoType.L645, lotto645.round)
                    if (notYet) return@launch
                    if (isExpired) {
                        _state.update { LottoScanResultUiState.Expired }
                        return@launch
                    }
                }

                myLotto.myLotto720Info?.let { lotto720 ->
                    val notYet = checkAnnouncement(latestLotto720RoundInfo.round, LottoType.L720, lotto720.round)
                    val isExpired = checkExpire(latestLotto720RoundInfo, LottoType.L720, lotto720.round)
                    if (notYet) return@launch
                    if (isExpired) {
                        _state.update { LottoScanResultUiState.Expired }
                        return@launch
                    }
                }
            }

            getLottoResult(from, inputType, myLotto)
        }
    }

    private suspend fun checkAnnouncement(latestRound: Int, type: LottoType, round: Int): Boolean = coroutineScope {
        if (latestRound < round) {
            val week = if (type == LottoType.L645) Calendar.SATURDAY else Calendar.THURSDAY
            val remainDays = DateUtils.getDaysUntilNextDay(week)

            delay(NOT_WINNER_DELAY)
            _state.update { LottoScanResultUiState.NotYet(type, remainDays) }
            return@coroutineScope true
        }

        false
    }

    private fun checkExpire(latestRoundInfo: LatestRoundInfo, type: LottoType, round: Int): Boolean {
        val lottoDate = DateUtils.calLottoRoundDate(latestRoundInfo.drawDate, latestRoundInfo.round-round)

        return DateUtils.isPrizeExpired(lottoDate)
    }

    private suspend fun getLottoResult(from: LotteryResultFrom, type: LotteryInputType, myLotto: MyLottoInfo) = coroutineScope {
        when (type) {
            LotteryInputType.ONLY645 -> {
                val result = checkLotteryResultUseCase(LottoType.L645, myLotto).getOrThrow()
                    .toUiModel(LottoType.L645, myLotto)

                if (result.isWinner) {
                    delay(CELEBRATION_DELAY)
                    _state.update { LottoScanResultUiState.CelebrationLoading }
                    delay(CELEBRATION_DELAY)
                    _state.update { LottoScanResultUiState.Success(from, result) }
                } else {
                    delay(NOT_WINNER_DELAY)
                    _state.update { LottoScanResultUiState.NotWinner }
                }
            }
            LotteryInputType.ONLY720 -> {
                val result = checkLotteryResultUseCase(LottoType.L720, myLotto).getOrThrow()
                    .toUiModel(LottoType.L720, myLotto)

                if (result.isWinner) {
                    delay(CELEBRATION_DELAY)
                    _state.update { LottoScanResultUiState.CelebrationLoading }
                    delay(CELEBRATION_DELAY)
                    _state.update { LottoScanResultUiState.Success(from, result) }

                } else {
                    delay(NOT_WINNER_DELAY)
                    _state.update { LottoScanResultUiState.NotWinner }
                }
            }
            LotteryInputType.BOTH -> {
                val lotto645ResultJob = async {
                    checkLotteryResultUseCase(LottoType.L645, myLotto).getOrThrow()
                }

                val lotto720ResultJob = async {
                    checkLotteryResultUseCase(LottoType.L720, myLotto).getOrThrow()
                }

                val lotto645Result = lotto645ResultJob.await().toUiModel(LottoType.L645, myLotto)
                val lotto720Result = lotto720ResultJob.await().toUiModel(LottoType.L720, myLotto)


                val lottoResultRows = when {
                    lotto645Result.isWinner && lotto720Result.isWinner -> lotto645Result.resultRows + lotto720Result.resultRows
                    lotto645Result.isWinner -> lotto645Result.resultRows
                    lotto720Result.isWinner -> lotto720Result.resultRows
                    else -> emptyList()
                }

                when {
                    !lotto645Result.isWinner && !lotto720Result.isWinner -> {
                        delay(NOT_WINNER_DELAY)
                        _state.update { LottoScanResultUiState.NotWinner }
                    }

                    else -> {
                        delay(CELEBRATION_DELAY)
                        _state.update { LottoScanResultUiState.CelebrationLoading }
                        delay(CELEBRATION_DELAY)
                        _state.update {
                            LottoScanResultUiState.Success(
                                from,
                                ScanResultUiModel(
                                    myLotto = myLotto,
                                    isWinner = true,
                                    resultRows = lottoResultRows,
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun sendEffect(effect: LotteryResultEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    companion object {
        private const val CELEBRATION_DELAY = 1_500L
        private const val NOT_WINNER_DELAY = 2_000L
    }
}