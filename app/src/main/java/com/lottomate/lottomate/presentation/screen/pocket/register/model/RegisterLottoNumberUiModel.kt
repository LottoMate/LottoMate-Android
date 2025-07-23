package com.lottomate.lottomate.presentation.screen.pocket.register.model

data class RegisterLottoNumberUiModel(
    val lottoNumbers: String,
    val isError: Boolean = false,
) {
    companion object {
        val EMPTY = RegisterLottoNumberUiModel("", isError = false)
    }
}
