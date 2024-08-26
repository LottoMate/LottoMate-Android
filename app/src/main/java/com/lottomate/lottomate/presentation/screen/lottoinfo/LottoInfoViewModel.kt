package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LottoInfoViewModel @Inject constructor(
    private val lottoInfoRepository: LottoInfoRepository,
) : ViewModel() {
    var currentTabMenu = mutableIntStateOf(LottoType.L645.ordinal)
        private set
    var hasPreLottoRound = mutableStateOf(true)
        private set
    var hasNextLottoRound = mutableStateOf(false)
        private set

    private val _lottoInfo = MutableStateFlow<LottoInfoUiState>(LottoInfoUiState.Loading)
    val lottoInfo get() = _lottoInfo.asStateFlow()
    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow get() = _errorFlow.asSharedFlow()

    init {
        loadLatestLottoInfo()
    }

    fun changeTabMenu(index: Int) {
        currentTabMenu.intValue = index

        when (index) {
            LottoType.L645.ordinal -> {
                getLatestLottoInfoByLottoType(LottoType.L645)
            }
            LottoType.L720.ordinal -> {
                getLatestLottoInfoByLottoType(LottoType.L720)
            }
            else -> {
                // TODO : 스피또 (수정예정)
            }
        }
    }

    fun getLottoInfo(lottoRndNum: Int) {
        val lottoType = LottoType.findLottoType(currentTabMenu.intValue).num

        viewModelScope.launch {
            lottoInfoRepository.fetchLottoInfoByRound(lottoType, lottoRndNum)
                .collectLatest { lottoInfo ->
                    judgePreOrNextLottoRound(lottoInfo.lottoRound)

                    _lottoInfo.update {
                        LottoInfoUiState.Success(lottoInfo)
                    }
                }
        }
    }

    private fun getLatestLottoInfoByLottoType(lottoType: LottoType) {
        viewModelScope.launch {
            lottoInfoRepository.getLatestLottoInfoByLottoType(lottoType)
                .collectLatest { lottoInfo ->
                    judgePreOrNextLottoRound(lottoInfo.lottoRound)

                    _lottoInfo.update {
                        LottoInfoUiState.Success(lottoInfo)
                    }
                }
        }
    }

    private fun judgePreOrNextLottoRound(lottoRndNum: Int) {
        val currentLottoType = LottoType.findLottoType(currentTabMenu.intValue).num
        val latestLottoRound = lottoInfoRepository.latestLottoInfo.getValue(currentLottoType)

        hasNextLottoRound.value = lottoRndNum != latestLottoRound.lottoRound
        hasPreLottoRound.value = lottoRndNum != LOTTO_FIRST_ROUND
    }

    private fun loadLatestLottoInfo() {
        viewModelScope.launch {
            lottoInfoRepository.fetchLatestLottoInfo()
                .catch { throwable ->
                    _errorFlow.emit(throwable)
                }
                .collectLatest {
                    val info = when (currentTabMenu.intValue) {
                        LottoType.L645.ordinal -> { it.getValue(LottoType.L645.num) }
                        LottoType.L720.ordinal -> { it.getValue(LottoType.L720.num) }
                        else -> {
                            // TODO : 스피또 (수정예정)
                            it.getValue(LottoType.L645.num)
                        }
                    }

                    _lottoInfo.update {
                        LottoInfoUiState.Success(info)
                    }
                }
        }
    }

    companion object {
        private const val LOTTO_FIRST_ROUND = 1
    }
}

sealed interface LottoInfoUiState {
    data object Loading: LottoInfoUiState
    data class Success(val data: LottoInfo): LottoInfoUiState
}