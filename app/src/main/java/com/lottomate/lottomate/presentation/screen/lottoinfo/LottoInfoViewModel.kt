package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoMockDatas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
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
        when (index) {
            LottoType.L645.ordinal -> getLatestLottoInfoByLottoType(LottoType.L645)
            LottoType.L720.ordinal -> getLatestLottoInfoByLottoType(LottoType.L720)
            else -> getLatestLottoInfoByLottoType(LottoType.S2000)
        }

        currentTabMenu.intValue = index
    }

    fun getLottoInfoByRoundOrPage(lottoRndOrPageNum: Int) {
        val lottoType = LottoType.findLottoType(currentTabMenu.intValue)

        when (lottoType) {
            LottoType.L645, LottoType.L720 -> {
                viewModelScope.launch {
                    lottoInfoRepository.fetchLottoInfo(lottoType.num, lottoRndOrPageNum)
                        .collectLatest { lottoInfo ->
                            lottoInfo.lottoRound?.let { round ->
                                judgePreOrNextLottoRound(round)
                            }

                            _lottoInfo.update {
                                LottoInfoUiState.Success(lottoInfo)
                            }
                        }
                }
            }
            else -> {
                val updateSpeettoInfo = SpeettoMockDatas.copy(
                    currentPage = lottoRndOrPageNum
                )
                _lottoInfo.update {
                    LottoInfoUiState.Success(updateSpeettoInfo)
                }

                hasPreLottoRound.value = updateSpeettoInfo.currentPage != 1
                hasNextLottoRound.value = updateSpeettoInfo.currentPage != updateSpeettoInfo.lastPage
            }
        }
    }

    private fun getLatestLottoInfoByLottoType(lottoType: LottoType) {
        viewModelScope.launch {
            lottoInfoRepository.fetchLottoInfo(lottoType.num)
                .onStart { _lottoInfo.update { LottoInfoUiState.Loading } }
                .catch { throwable -> _errorFlow.emit(throwable) }
                .collectLatest { lottoInfo ->
                    lottoInfo.lottoRound?.let { round ->
                        judgePreOrNextLottoRound(round)
                    } ?: run {
                        val speettoInfo = lottoInfo as SpeettoInfo

                        hasPreLottoRound.value = speettoInfo.currentPage != 1
                        hasNextLottoRound.value = speettoInfo.currentPage != speettoInfo.lastPage
                    }

                    _lottoInfo.update {
                        LottoInfoUiState.Success(lottoInfo)
                    }
                }
        }
    }

    private fun judgePreOrNextLottoRound(lottoRndNum: Int) {
        val currentLottoType = LottoType.findLottoType(currentTabMenu.intValue).num
        val latestLottoRound = lottoInfoRepository.allLatestLottoRound.getValue(currentLottoType)

        hasNextLottoRound.value = lottoRndNum != latestLottoRound
        hasPreLottoRound.value = lottoRndNum != LOTTO_FIRST_ROUND
    }

    private fun loadLatestLottoInfo() {
        viewModelScope.launch {
            lottoInfoRepository.fetchAllLatestLottoInfo()

            val currentLottoType = LottoType.findLottoType(currentTabMenu.value)

            if (lottoInfoRepository.allLatestLottoRound.isNotEmpty()) {
                lottoInfoRepository.fetchLottoInfo(
                    lottoType = currentLottoType.num,
                    lottoRndNum = lottoInfoRepository.allLatestLottoRound.getValue(currentLottoType.num)
                )
                    .onStart { _lottoInfo.update { LottoInfoUiState.Loading } }
                    .catch { throwable -> _errorFlow.emit(throwable) }
                    .collectLatest { info ->
                        _lottoInfo.update { LottoInfoUiState.Success(info) }
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