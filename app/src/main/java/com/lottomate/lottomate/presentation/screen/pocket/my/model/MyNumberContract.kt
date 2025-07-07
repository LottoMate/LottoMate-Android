package com.lottomate.lottomate.presentation.screen.pocket.my.model

import com.lottomate.lottomate.data.model.LottoType

sealed interface MyNumberContract {
    sealed interface Event {
        data class DeleteMyNumber(val myNumberId: Int) : Event
        data object ChangeMode : Event
        data class ClickCheckWin(val myNumberDetail: MyNumberDetailUiModel, val numbers: List<Int>) : Event
    }

    sealed interface Effect {
        data class ShowSnackBar(val message: String) : Effect
        data class NavigateToLotteryResylt(val type: LottoType, val round: Int, val numbers: List<Int>) : Effect
    }
}