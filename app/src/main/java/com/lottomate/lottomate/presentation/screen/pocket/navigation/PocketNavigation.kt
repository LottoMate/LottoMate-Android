package com.lottomate.lottomate.presentation.screen.pocket.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.pocket.PocketRoute
import com.lottomate.lottomate.presentation.screen.pocket.random.DrawRandomNumbersRoute
import com.lottomate.lottomate.presentation.screen.pocket.random.RandomNumbersStorageRoute

fun NavController.navigateToPocketTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Pocket, navOptions)
}

fun NavController.navigateToRandomNumberStorage() {
    navigate(LottoMateRoute.PocketStorage)
}

fun NavController.navigateToDrawRandomNumbers() {
    navigate(LottoMateRoute.PocketDrawRandomNumbers)
}

fun NavGraphBuilder.pocketNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    composable<BottomTabRoute.Pocket> {
        PocketRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickDrawRandomNumbers = { navController.navigateToDrawRandomNumbers() },
            onClickStorageOfRandomNumbers = { navController.navigateToRandomNumberStorage() }
        )
    }

    composable<LottoMateRoute.PocketStorage> {
        RandomNumbersStorageRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() }
        )
    }

    composable<LottoMateRoute.PocketDrawRandomNumbers> {
        DrawRandomNumbersRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() },
        )
    }
}