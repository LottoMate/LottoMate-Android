package com.lottomate.lottomate.presentation.screen.pocket.register.model

data class RegisterLottoNumber(
    val lottoNumbers: String,
    val isError: Boolean = false,
) {
    companion object {
        val EMPTY = RegisterLottoNumber("", isError = false)
    }
}
