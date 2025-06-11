package com.lottomate.lottomate.presentation.screen.scanResult

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.InvalidLottoQRFormatException
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.mapper.toUiModel
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.di.DispatcherModule
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.domain.usecase.CheckLotteryResultUseCase
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto645Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto720Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.presentation.screen.scanResult.model.ScanResultUiModel
import com.lottomate.lottomate.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LottoScanResultViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    @DispatcherModule.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val lottoInfoRepository: LottoInfoRepository,
    private val checkLotteryResultUseCase: CheckLotteryResultUseCase,
) : BaseViewModel(errorHandler) {
    private var _state = MutableStateFlow<LottoScanResultUiState>(LottoScanResultUiState.Loading)
    val state: StateFlow<LottoScanResultUiState> get() = _state.asStateFlow()

    fun getLottoResult(data: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val parseData = data.substringAfter("?v=")

                val (lottoType, myLotto) = parseMyLotto(parseData)
                val isAnnouncement = checkAnnouncement(lottoType, myLotto.round)

                if (!isAnnouncement) {
                    val week = if (lottoType == LottoType.L645) Calendar.SATURDAY else Calendar.THURSDAY
                    val remainDays = DateUtils.getDaysUntilNextDay(week)

                    delay(2_000)
                    _state.update { LottoScanResultUiState.NotYet(remainDays) }
                    return@launch
                }

                val result = checkLotteryResultUseCase(lottoType, myLotto).getOrThrow()

                delay(1_500)
                _state.update { LottoScanResultUiState.CelebrationLoading }
                delay(1_500)
                _state.update { LottoScanResultUiState.Success(result.toUiModel(myLotto)) }
            } catch (e: Exception) {
                handleException(InvalidLottoQRFormatException())
            }
        }
    }

    private suspend fun checkAnnouncement(type: LottoType, round: Int): Boolean {
        val latestRound = lottoInfoRepository.latestLottoRoundInfo.first().getValue(type.num)
        return latestRound.round >= round
    }

    private fun parseMyLotto(data: String): Pair<LottoType, MyLottoInfo> {
        return when {
            data.contains("pd") -> {
                LottoType.L720 to parseLotto720(data.substringAfter("pd"))
            }
            else -> {
                LottoType.L645 to parseLotto645(data)
            }
        }
    }

    /**
     * 실물 복권 용지(연금복권720)를 스캔한 데이터를 회차, 번호로 파싱합니다.
     *
     * @param data 실물 복권 용지를 스캔했을 때 나오는 데이터
     * @return MyLottoInfo 파싱된 데이터
     */
    private fun parseLotto720(data: String): MyLottoInfo {
        val (roundPart, numbers) = data.split("s")

        val round = roundPart.substring(3, 6)  // "256"
        val jo = roundPart.substring(6, 7)     // "1"

        return MyLotto720Info(
            round = round.toInt(),
            firstNumber = jo.toInt(),
            numbers = numbers.map { it.toInt() },
        )
    }

    /**
     * 실물 복권 용지(로또645)를 스캔한 데이터를 회차, 번호로 파싱합니다.
     *
     * @param data 실물 복권 용지를 스캔했을 때 나오는 데이터
     * @return MyLottoInfo 파싱된 데이터
     */
    private fun parseLotto645(data: String): MyLottoInfo {
        val round = Regex("^\\d+").find(data)?.value

        val regex = "(q|n|m)(\\d+)".toRegex()
        val numbers = regex.findAll(data)
            .map { matchResult ->
                matchResult.groupValues[2].take(12)
            }.toList()

        return MyLotto645Info(
            round = round?.toInt() ?: 0,
            numbers = numbers.map { raw -> raw.chunked(2).map { it.toInt() } },
        )
    }
}


sealed interface LottoScanResultUiState {
    data object Loading : LottoScanResultUiState
    // 당첨되어 결과를 기다릴때 보여주는 로딩 화면
    data object CelebrationLoading : LottoScanResultUiState
    data class NotYet(val data: Int) : LottoScanResultUiState
    data class Success(val data: ScanResultUiModel) : LottoScanResultUiState
}

data class LottoResultInfo(
    val rank: Int,
    val price: String,
)