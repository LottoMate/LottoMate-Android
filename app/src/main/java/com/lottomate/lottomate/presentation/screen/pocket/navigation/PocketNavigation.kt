package com.lottomate.lottomate.presentation.screen.pocket.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHomeTab
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToLottoScan
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToLottoScanResult
import com.lottomate.lottomate.presentation.screen.pocket.PocketRoute
import com.lottomate.lottomate.presentation.screen.pocket.random.DrawRandomNumbersRoute
import com.lottomate.lottomate.presentation.screen.pocket.random.RandomNumbersStorageRoute
import com.lottomate.lottomate.presentation.screen.scan.LottoScanRoute
import com.lottomate.lottomate.presentation.screen.scanResult.LottoScanResultRoute
import com.lottomate.lottomate.presentation.screen.winnerguide.navigation.navigateToWinnerGuide

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
            moveToLottoScan = { navController.navigateToLottoScan() },
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

    // 복권 스캔 화면
    composable<LottoMateRoute.LottoScan> {
        LottoScanRoute(
            padding = padding,
            moveToLottoScanResult = { navController.navigateToLottoScanResult(it) },
            onBackPressed = { navController.popBackStack() },
        )
    }

    // 복권 스캔 결과 화면
    composable<LottoMateRoute.LottoScanResult> { navBackStackEntry ->
        val data = navBackStackEntry.toRoute<LottoMateRoute.LottoScanResult>().data

        LottoScanResultRoute(
            padding = padding,
            data = data,
            moveToHome = {
                val navOptions = NavOptions.Builder().apply {
                    setPopUpTo<BottomTabRoute.Home>(true)
                }.build()

                navController.navigateToHomeTab(navOptions)
            },
            moveToWinningGuide = { navController.navigateToWinnerGuide() },
            onBackPressed = { navController.popBackStack() },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}