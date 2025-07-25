package com.lottomate.lottomate.presentation.screen.pocket.random

import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.local.RandomLottoRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawRandomNumbersViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val randomLottoRepository: RandomLottoRepository,
) : BaseViewModel(errorHandler) {
    private var _randomNumbers = MutableStateFlow<DrawRandomNumbersUiState>(DrawRandomNumbersUiState.Loading)
    val randomNumbers: StateFlow<DrawRandomNumbersUiState> get() = _randomNumbers.asStateFlow()
    private var _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow: SharedFlow<String> get() = _snackBarFlow.asSharedFlow()

    init {
        playLuckyDraw()
    }

    private fun playLuckyDraw() {
        val numbers = mutableListOf<Int>()
        while(numbers.size < LOTTO_TOTAL_COUNT) {
            val number = (LOTTO_FIRST_NUMBER..LOTTO_LAST_NUMBER).random()
            if (!numbers.contains(number)) numbers.add(number)
        }
        val sortedNumbers = numbers.sorted()

        viewModelScope.launch {
            delay(5_000)

            saveRandomLotto(sortedNumbers)
            _randomNumbers.update {
                DrawRandomNumbersUiState.Success(sortedNumbers)
            }

            _snackBarFlow.emit(SNACKBAR_MESSAGE)
        }
    }

    private fun saveRandomLotto(numbers: List<Int>) {
        viewModelScope.launch {
            runCatching {
                randomLottoRepository.insertRandomLotto(numbers)
            }.onFailure { handleException(it) }
        }
    }

    companion object {
        private const val SNACKBAR_MESSAGE = "뽑은 번호는 오늘 뽑은 번호에서 확인하세요"
        private const val LOTTO_TOTAL_COUNT = 6
        private const val LOTTO_FIRST_NUMBER = 1
        private const val LOTTO_LAST_NUMBER = 45
    }
}

sealed interface DrawRandomNumbersUiState {
    data object Loading : DrawRandomNumbersUiState
    data class Success(val randomNumbers: List<Int>) : DrawRandomNumbersUiState
}