package com.lottomate.lottomate.presentation.screen.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHomeTab
import com.lottomate.lottomate.presentation.screen.interview.navigation.navigateToInterviewDetail
import com.lottomate.lottomate.presentation.screen.login.LoginRoute
import com.lottomate.lottomate.presentation.screen.login.LoginSuccessRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LottoMateRoute.Login, navOptions)
}

fun NavController.navigateToLoginSuccess() {
    val navOptions = NavOptions.Builder().apply {
        setPopUpTo(LottoMateRoute.Login, true)
    }.build()

    navigate(LottoMateRoute.LoginComplete, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    composable<LottoMateRoute.Login> {
        LoginRoute(
            padding = padding,
            moveToLoginSuccess = { navController.navigateToLoginSuccess() },
            onCloseClicked = {
                val homeRoute = BottomTabRoute.Home

                // Home이 백스택에 있으면 그 자리로 pop
                val popped = navController.popBackStack(homeRoute, false)

                if (!popped) {
                    val navOptions = NavOptions.Builder().apply {
                        setPopUpTo(LottoMateRoute.Login, inclusive = true)
                        setLaunchSingleTop(true)
                    }.build()

                    navController.navigateToHomeTab(navOptions)
                }
            },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }

    composable<LottoMateRoute.LoginComplete> {
        LoginSuccessRoute(
            padding = padding,
            moveToHome = {
                val navOptions = NavOptions.Builder().apply {
                    setPopUpTo(LottoMateRoute.LoginComplete, true)
                    setLaunchSingleTop(true)
                }.build()

                navController.navigateToHomeTab(navOptions)
            },
            moveToInterviewDetail = { no, place -> navController.navigateToInterviewDetail(no, place) },
            onErrorSnackBar = {},
        )
    }
}