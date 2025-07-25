package com.lottomate.lottomate.presentation.screen.pocket.random.contract

import androidx.annotation.StringRes

sealed interface RandomMyNumbersEffect {
    data class ShowSnackBar(@StringRes val messageRes: Int) : RandomMyNumbersEffect
}