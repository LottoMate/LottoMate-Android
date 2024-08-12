package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _lottoInfo = MutableStateFlow<LottoInfoUiState>(LottoInfoUiState.Loading)
    val lottoInfo get() = _lottoInfo.asStateFlow()

    init {
        loadLatestLottoInfo()
    }

    fun getLottoInfoByRound(round: Int) {

    }

    fun changeTabMenu(index: Int) {
        currentTabMenu.intValue = index

        loadLatestLottoInfo()
    }

    private fun loadLatestLottoInfo() {
        viewModelScope.launch {
            lottoInfoRepository.getLatestLottoInfo()
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
}

sealed interface LottoInfoUiState {
    data object Loading: LottoInfoUiState
    data class Success(val data: LottoInfo): LottoInfoUiState
}