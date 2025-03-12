package com.lottomate.lottomate.presentation.screen.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.login.LoginRoute

fun NavController.navigateToLogin() {
    navigate(LottoMateRoute.Login)
}

fun NavController.navigateToLoginSuccess() {
    navigate(LottoMateRoute.LoginComplete)
}

fun NavGraphBuilder.loginNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<LottoMateRoute.Login> {
        LoginRoute(
            padding = padding,
            moveToLoginSuccess = { navController.navigateToLoginSuccess() },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}