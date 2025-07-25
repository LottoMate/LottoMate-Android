package com.lottomate.lottomate.presentation.screen.pocket.random.contract

import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomMyNumbersGroupUiModel

sealed interface RandomMyNumbersUiState {
    data object Idle : RandomMyNumbersUiState
    data object Empty : RandomMyNumbersUiState
    data class Success(val data: List<RandomMyNumbersGroupUiModel>) : RandomMyNumbersUiState
}