package com.lottomate.lottomate.presentation.screen.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.Route
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHome
import com.lottomate.lottomate.presentation.screen.login.LoginRoute
import com.lottomate.lottomate.presentation.screen.login.LoginSuccessRoute
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateToMap

fun NavController.navigateToLogin() {
    navigate(route = Route.LOGIN.name)
}

fun NavController.navigateToLoginSuccess() {
    navigate(route = Route.LOGIN_SUCCESS.name)
}

fun NavGraphBuilder.loginNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(Route.LOGIN.name) {
        LoginRoute(
            padding = padding,
            moveToLoginSuccess = { navController.navigateToLoginSuccess() },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }

    composable(Route.LOGIN_SUCCESS.name) {
        LoginSuccessRoute(
            padding = padding,
            moveToHome = { navController.navigateToHome() },
            moveToMap = { navController.navigateToMap() },
            onErrorSnackBar = onShowErrorSnackBar,
        )
    }
}