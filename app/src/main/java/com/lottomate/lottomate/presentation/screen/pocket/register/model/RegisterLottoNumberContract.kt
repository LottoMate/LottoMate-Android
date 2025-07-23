package com.lottomate.lottomate.presentation.screen.pocket.register.model

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.result.model.LotteryInputType
import com.lottomate.lottomate.presentation.screen.result.model.MyLottoInfo

sealed interface RegisterLottoNumberContract {
    data class State(
        val currentLotto645RoundInfo: LatestRoundInfo = LatestRoundInfo.EMPTY,
        val currentLotto720RoundInfo: LatestRoundInfo = LatestRoundInfo.EMPTY,
        val hasLotto645PreRound: Boolean = true,
        val hasLotto645NextRound: Boolean = false,
        val hasLotto720PreRound: Boolean = true,
        val hasLotto720NextRound: Boolean = false,
    ): RegisterLottoNumberContract

    sealed interface Event : RegisterLottoNumberContract {
        data class ClickPreRound(val type: LottoType) : Event
        data class ClickNextRound(val type: LottoType) : Event
        data class ChangeLottoRound(val lottoType: LottoType, val round: LatestRoundInfo) : Event
        data class ClickSave(val inputLotto645List: List<RegisterLottoNumberUiModel>, val inputLotto720List: List<RegisterLottoNumberUiModel>) : Event
    }

    sealed interface Effect: RegisterLottoNumberContract {
        data object ShowSuccessSnackBar : Effect
        data class NavigateToLotteryResult(val inputType: LotteryInputType, val myLottoInfo: MyLottoInfo) : Effect
    }
}