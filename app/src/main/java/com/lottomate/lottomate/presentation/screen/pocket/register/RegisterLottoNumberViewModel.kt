package com.lottomate.lottomate.presentation.screen.pocket.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLottoNumberViewModel @Inject constructor(
    private val lottoInfoRepository: LottoInfoRepository,
) : ViewModel() {
    val latestLottoRoundInfo: StateFlow<Map<Int, LatestRoundInfo>>
        get() = lottoInfoRepository.latestLottoRoundInfo
    var hasLotto645PreRound = mutableStateOf(true)
        private set
    var hasLotto645NextRound = mutableStateOf(false)
        private set
    var hasLotto720PreRound = mutableStateOf(true)
        private set
    var hasLotto720NextRound = mutableStateOf(false)
        private set

    private var _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow: SharedFlow<String> get() = _snackBarFlow.asSharedFlow()

    fun judgePreOrNextLottoRound(lotteryType: LottoType, lottoRndNum: Int) {
        val latestLottoRound = latestLottoRoundInfo.value.getValue(lotteryType.num)

        when (lotteryType) {
            LottoType.L645 -> {
                hasLotto645NextRound.value = lottoRndNum != latestLottoRound.round
                hasLotto645PreRound.value = lottoRndNum != latestLottoRound.round - REGISTRABLE_ROUND_LIMIT
            }
            LottoType.L720 -> {
                hasLotto720NextRound.value = lottoRndNum != latestLottoRound.round
                hasLotto720PreRound.value = lottoRndNum != latestLottoRound.round - REGISTRABLE_ROUND_LIMIT
            }
            else -> {}
        }
    }

    fun sendSnackBarMsg(message: String) {
        viewModelScope.launch {
            _snackBarFlow.emit(message)
        }
    }

    companion object {
        private const val REGISTRABLE_ROUND_LIMIT = 48
    }
}

sealed interface LottoRoundUiState {
    data object Loading: LottoRoundUiState
    data class Success(val round: String, val date: String): LottoRoundUiState
    data class Failed(val message: String, val throwable: Throwable? = null): LottoRoundUiState
}