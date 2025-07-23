package com.lottomate.lottomate.presentation.screen.pocket.register

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.mapper.toDomain
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.domain.repository.MyNumberRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterLottoNumberContract
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterLottoNumberUiModel
import com.lottomate.lottomate.presentation.screen.result.model.LotteryInputType
import com.lottomate.lottomate.presentation.screen.result.model.MyLotto645Info
import com.lottomate.lottomate.presentation.screen.result.model.MyLotto720Info
import com.lottomate.lottomate.presentation.screen.result.model.MyLotto720InfoNumbers
import com.lottomate.lottomate.presentation.screen.result.model.MyLottoInfo
import com.lottomate.lottomate.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class RegisterLottoNumberViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val lottoInfoRepository: LottoInfoRepository,
    private val myNumberRepository: MyNumberRepository,
) : BaseViewModel(errorHandler) {
    private val latestLottoRoundInfo = lottoInfoRepository.latestLottoRoundInfo
    private val _currentLotto645Round = MutableStateFlow(latestLottoRoundInfo.value.getValue(LottoType.L645.num))
    private val _currentLotto720Round = MutableStateFlow(latestLottoRoundInfo.value.getValue(LottoType.L720.num))

    val state = combine(
        lottoInfoRepository.latestLottoRoundInfo,
        _currentLotto645Round,
        _currentLotto720Round,
    ) { latestLottoRoundInfo, current645Round, current720Round ->
        val latestLotto645Round = latestLottoRoundInfo.getValue(LottoType.L645.num)
        val latestLotto720Round = latestLottoRoundInfo.getValue(LottoType.L720.num)

        RegisterLottoNumberContract.State(
            currentLotto645RoundInfo = current645Round,
            currentLotto720RoundInfo = current720Round,
            hasLotto645PreRound = current645Round.round != latestLotto645Round.round - REGISTRABLE_ROUND_LIMIT,
            hasLotto645NextRound = current645Round.round != latestLotto645Round.round,
            hasLotto720PreRound = current720Round.round != latestLotto720Round.round - REGISTRABLE_ROUND_LIMIT,
            hasLotto720NextRound = current720Round.round != latestLotto720Round.round,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RegisterLottoNumberContract.State(),
        )

    private val _effect = Channel<RegisterLottoNumberContract.Effect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: RegisterLottoNumberContract.Event) {
        when (event) {
            is RegisterLottoNumberContract.Event.ClickSave -> {
                saveLottoNumbers(event.inputLotto645List, event.inputLotto720List)
            }
            is RegisterLottoNumberContract.Event.ClickPreRound -> updateLottoRoundByType(true, event.type)
            is RegisterLottoNumberContract.Event.ClickNextRound -> updateLottoRoundByType(false, event.type)
            is RegisterLottoNumberContract.Event.ChangeLottoRound -> selectLottoRound(event.lottoType, event.round)
        }
    }

    /**
     * 사용자가 입력한 번호를 저장합니다.
     *
     * @param inputLotto645List 로또645 번호 리스트 (ex. 010511121343)
     * @param inputLotto720List 연금복권720 번호 리스트 (ex. 125347)
     */
    private fun saveLottoNumbers(inputLotto645List: List<RegisterLottoNumberUiModel>, inputLotto720List: List<RegisterLottoNumberUiModel>) {
        val registerLottoNumber = listOfNotNull(
            inputLotto645List.takeIf { it.isNotEmpty() }?.map {
                it.toDomain(LottoType.L645, state.value.currentLotto645RoundInfo.round)
            },
            inputLotto720List.takeIf { it.isNotEmpty() }?.map {
                it.toDomain(LottoType.L720, state.value.currentLotto720RoundInfo.round)
            }
        ).flatten()

        viewModelScope.launch {
            val results: List<Result<Unit>> = supervisorScope {
                registerLottoNumber
                    .filter { it.numbers.isNotEmpty() }
                    .map { lotto ->
                        async { myNumberRepository.insertMyNumber(lotto) }
                    }.awaitAll()
                }

            val successResultCount = results.count { it.isSuccess }
            val failureResultCount = results.count { it.isFailure }

            when {
                // 모두 성공
                failureResultCount == 0 -> {
                    _effect.send(RegisterLottoNumberContract.Effect.ShowSuccessSnackBar)

                    navigateToLotteryResult(inputLotto645List, inputLotto720List)
                }
                // 모두 실패
                successResultCount == 0 -> {
                    handleException(IllegalArgumentException("모든 번호 저장 실패"))
                }
                // 일부만 실패
                else -> {
                    val failedItems = registerLottoNumber
                        .zip(results)
                        .filter { it.second.isFailure }
                        .map { it.first }
                }
            }
        }
    }

    /**
     * 복권 결과 화면으로 이동합니다.
     *
     * @param inputLotto645Numbers 로또645 번호 리스트 (ex. 010511121343)
     * @param inputLotto720Numbers 연금복권720 번호 리스트 (ex. 125347)
     */
    private suspend fun navigateToLotteryResult(
        inputLotto645Numbers: List<RegisterLottoNumberUiModel>,
        inputLotto720Numbers: List<RegisterLottoNumberUiModel>
    ) {
        val has645 = inputLotto645Numbers.filterNot { it.lottoNumbers.isEmpty() }.isNotEmpty()
        val has720 = inputLotto720Numbers.filterNot { it.lottoNumbers.isEmpty() }.isNotEmpty()

        val myLotto645 = if (has645) {
            MyLotto645Info(
                round = state.value.currentLotto645RoundInfo.round,
                numbers = inputLotto645Numbers
                    .map { it.lottoNumbers.chunked(2) }
                    .map { chunkedNumbers -> chunkedNumbers.map { it.toInt() } }
            )
        } else null

        val myLotto720 = if (has720) {
            MyLotto720Info(
                round = state.value.currentLotto720RoundInfo.round,
                numbers = inputLotto720Numbers
                    .map { it.lottoNumbers.chunked(1) }
                    .map { chunkedNumbers -> MyLotto720InfoNumbers(chunkedNumbers.map { it.toInt() }) }
            )
        } else null

        LotteryInputType.get(myLotto645 != null, myLotto720 != null)?.let { inputType ->
            _effect.send(RegisterLottoNumberContract.Effect.NavigateToLotteryResult(inputType, MyLottoInfo(myLotto645, myLotto720)))
        }
    }

    private fun selectLottoRound(lottoType: LottoType, selectedRound: LatestRoundInfo) {
        if (lottoType == LottoType.L645) {
            _currentLotto645Round.update { selectedRound }
        } else {
            _currentLotto720Round.update { selectedRound }
        }
    }

    private fun updateLottoRoundByType(isPrev: Boolean, lottoType: LottoType) {
        val currentLotto645RoundInfo = state.value.currentLotto645RoundInfo
        val currentLotto720RoundInfo = state.value.currentLotto720RoundInfo

        if (lottoType == LottoType.L645) {
            val newRoundInfo = LatestRoundInfo(
                round = if (isPrev) currentLotto645RoundInfo.round - 1 else currentLotto645RoundInfo.round + 1,
                drawDate = DateUtils.calLottoRoundDate(currentLotto645RoundInfo.drawDate.replace("-", "."), 1, isFuture = !isPrev)
            )

            _currentLotto645Round.update { newRoundInfo }
        } else {
            val newRoundInfo = LatestRoundInfo(
                round = if (isPrev) currentLotto720RoundInfo.round - 1 else currentLotto720RoundInfo.round + 1,
                drawDate = DateUtils.calLottoRoundDate(currentLotto720RoundInfo.drawDate.replace("-", "."), 1, isFuture = !isPrev)
            )

            _currentLotto720Round.update { newRoundInfo }
        }
    }

    companion object {
        private const val REGISTRABLE_ROUND_LIMIT = 48
    }
}