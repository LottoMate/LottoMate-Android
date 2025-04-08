package com.lottomate.lottomate.presentation.screen.pocket.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToLottoScan
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToLottoScanResult
import com.lottomate.lottomate.presentation.screen.pocket.PocketRoute
import com.lottomate.lottomate.presentation.screen.pocket.random.DrawRandomNumbersRoute
import com.lottomate.lottomate.presentation.screen.pocket.random.RandomNumbersStorageRoute
import com.lottomate.lottomate.presentation.screen.pocket.register.RegisterLottoNumbersRoute
import com.lottomate.lottomate.presentation.screen.setting.navigation.navigateToSetting

fun NavController.navigateToPocketTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Pocket, navOptions)
}

fun NavController.navigateToRandomNumberStorage() {
    navigate(LottoMateRoute.PocketStorage)
}

fun NavController.navigateToDrawRandomNumbers() {
    navigate(LottoMateRoute.PocketDrawRandomNumbers)
}

fun NavController.navigateToSaveNumbers() {
    navigate(route = LottoMateRoute.RegisterLottoNumber)
}

fun NavGraphBuilder.pocketNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowGlobalSnackBar: (message: String) -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    composable<BottomTabRoute.Pocket> {
        PocketRoute(
            padding = padding,
            moveToLottoScan = { navController.navigateToLottoScan() },
            moveToSetting = { navController.navigateToSetting() },
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickDrawRandomNumbers = { navController.navigateToDrawRandomNumbers() },
            onClickStorageOfRandomNumbers = { navController.navigateToRandomNumberStorage() },
            moveToSaveNumberScreen = { navController.navigateToSaveNumbers() },
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

    composable<LottoMateRoute.RegisterLottoNumber> {
        RegisterLottoNumbersRoute(
            padding = padding,
            moveToLottoResult = { navController.navigateToLottoScanResult(it) },
            onShowGlobalSnackBar = onShowGlobalSnackBar,
            onBackPressed = { navController.navigateUp() },
        )
    }
}