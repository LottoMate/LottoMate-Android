package com.lottomate.lottomate.presentation.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.feature.home.HomeRoute
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(BottomNavigationRoute.HOME.name, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    onClickLottoInfo: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(BottomNavigationRoute.HOME.name) {
        HomeRoute(
            padding = padding,
            onClickLottoInfo = onClickLottoInfo,
            onShowErrorSnackBar = onShowErrorSnackBar
        )
    }
}