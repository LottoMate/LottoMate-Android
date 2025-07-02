package com.lottomate.lottomate.presentation.screen.map.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.screen.login.navigation.navigateToLogin
import com.lottomate.lottomate.presentation.screen.map.MapRoute

fun NavController.navigateToMapTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Map, navOptions)
}

fun NavController.navigateToMap() {
    navigate(BottomTabRoute.Map)
}

fun NavGraphBuilder.mapNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    composable<BottomTabRoute.Map> {
        MapRoute(
            padding = padding,
            moveToLogin = {
                val navOptions = NavOptions.Builder().apply {
                    setPopUpTo(
                        route = BottomTabRoute.Map,
                        inclusive = false,
                        saveState = true,
                    )

                    setLaunchSingleTop(true)
                    setRestoreState(true)
                }.build()

                navController.navigateToLogin(navOptions)
            },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}