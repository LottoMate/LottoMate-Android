package com.lottomate.lottomate.presentation.screen.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.screen.home.HomeRoute
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute
import com.lottomate.lottomate.presentation.screen.interview.navigation.navigateInterview
import com.lottomate.lottomate.presentation.screen.login.navigation.navigateToLogin
import com.lottomate.lottomate.presentation.screen.lottoinfo.navigation.navigateLottoInfo

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(BottomNavigationRoute.HOME.name, navOptions)
}

fun NavController.navigateToHome() {
    navigate(BottomNavigationRoute.HOME.name)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(BottomNavigationRoute.HOME.name) {
        HomeRoute(
            padding = padding,
            onClickLottoInfo = { navController.navigateLottoInfo() },
            onClickInterview = { navController.navigateInterview() },
            onClickLogin = { navController.navigateToLogin() },
            onShowErrorSnackBar = onShowErrorSnackBar
        )
    }
}