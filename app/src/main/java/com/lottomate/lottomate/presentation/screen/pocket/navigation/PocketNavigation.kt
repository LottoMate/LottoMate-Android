package com.lottomate.lottomate.presentation.screen.pocket.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute
import com.lottomate.lottomate.presentation.navigation.Route
import com.lottomate.lottomate.presentation.screen.pocket.random.DrawRandomNumbersRoute
import com.lottomate.lottomate.presentation.screen.pocket.PocketRoute
import com.lottomate.lottomate.presentation.screen.pocket.random.RandomNumbersStorageRoute

fun NavController.navigatePocket(navOptions: NavOptions) {
    navigate(route = BottomNavigationRoute.POCKET.name, navOptions)
}

fun NavController.navigateToRandomNumberStorage() {
    navigate(route = Route.POCKET_STORAGE.name)
}

fun NavController.navigateToDrawRandomNumbers() {
    navigate(route = Route.POCKET_DRAW_RANDOM_NUMBERS.name)
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
            onClickDrawRandomNumbers = { navController.navigateToDrawRandomNumbers() },
            onClickStorageOfRandomNumbers = { navController.navigateToRandomNumberStorage() }
        )
    }

    composable(Route.POCKET_STORAGE.name) {
        RandomNumbersStorageRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() }
        )
    }

    composable(Route.POCKET_DRAW_RANDOM_NUMBERS.name) {
        DrawRandomNumbersRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() },
        )
    }
}