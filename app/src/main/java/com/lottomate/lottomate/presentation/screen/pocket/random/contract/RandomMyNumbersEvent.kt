package com.lottomate.lottomate.presentation.screen.pocket.random.contract

sealed interface RandomMyNumbersEvent {
    data class CopyRandomNumbers(val numbers: List<Int>) : RandomMyNumbersEvent
    data class RemoveRandomNumbers(val id: Int) : RandomMyNumbersEvent
}