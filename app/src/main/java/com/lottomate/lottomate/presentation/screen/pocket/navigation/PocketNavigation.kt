package com.lottomate.lottomate.presentation.screen.pocket.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute
import com.lottomate.lottomate.presentation.navigation.Route
import com.lottomate.lottomate.presentation.screen.pocket.PocketRoute
import com.lottomate.lottomate.presentation.screen.pocket.RandomNumbersStorageRoute

fun NavController.navigatePocket(navOptions: NavOptions) {
    navigate(route = BottomNavigationRoute.POCKET.name, navOptions)
}

fun NavController.navigateToRandomNumberStorage() {
    navigate(route = Route.POCKET_STORAGE.name)
}

fun NavGraphBuilder.pocketNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(BottomNavigationRoute.POCKET.name) {
        PocketRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickStorageOfRandomNumbers = { navController.navigateToRandomNumberStorage() }
        )
    }

    composable(Route.POCKET_STORAGE.name) {
        RandomNumbersStorageRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.popBackStack() }
        )
    }
}