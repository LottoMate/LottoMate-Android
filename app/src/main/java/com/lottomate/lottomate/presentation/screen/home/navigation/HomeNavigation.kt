package com.lottomate.lottomate.presentation.screen.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.HomeRoute
import com.lottomate.lottomate.presentation.screen.home.SettingPage
import com.lottomate.lottomate.presentation.screen.interview.InterviewRoute
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoInfoRoute
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateToMap

fun NavController.navigateToHomeTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Home, navOptions)
}

fun NavController.navigateToSetting() {
    navigate(LottoMateRoute.Setting)
}

fun NavController.navigateToInterview() {
    navigate(LottoMateRoute.Interview)
}

fun NavController.navigateToLottoDetail(type: LottoType, round: Int) {
    navigate(LottoMateRoute.LottoDetail(type, round))
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<BottomTabRoute.Home> {
        HomeRoute(
            padding = padding,
            moveToLottoInfo = { type, round -> navController.navigateToLottoDetail(type, round) },
            onClickInterview = { navController.navigateToInterview() },
            moveToSetting = { navController.navigateToSetting() },
            moveToMap = { navController.navigateToMap() },
            onShowErrorSnackBar = onShowErrorSnackBar
        )
    }

    // 로또 상세 화면
    composable<LottoMateRoute.LottoDetail> { navBackStackEntry ->
        val type = navBackStackEntry.toRoute<LottoMateRoute.LottoDetail>().type
        val round = navBackStackEntry.toRoute<LottoMateRoute.LottoDetail>().round

        LottoInfoRoute(
            onClickBottomBanner = {},
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() },
        )
    }

    // 인터뷰 화면
    composable<LottoMateRoute.Interview> {
        InterviewRoute(
            onClickBanner = {},
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() },
        )
    }

    // 설정 화면
    composable<LottoMateRoute.Setting> {
        SettingPage(
            padding = padding,
            onBackPressed = { navController.navigateUp() },
        )
    }
}