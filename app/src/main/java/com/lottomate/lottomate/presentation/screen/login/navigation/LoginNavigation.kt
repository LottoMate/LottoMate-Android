package com.lottomate.lottomate.presentation.screen.login.navigation

import androidx.navigation.NavController
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute

fun NavController.navigateToLogin() {
    navigate(LottoMateRoute.Login)
}

fun NavController.navigateToLoginSuccess() {
    navigate(LottoMateRoute.LoginComplete)
}