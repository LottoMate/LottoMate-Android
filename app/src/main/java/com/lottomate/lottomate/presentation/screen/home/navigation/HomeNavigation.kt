package com.lottomate.lottomate.presentation.screen.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.HomeRoute
import com.lottomate.lottomate.presentation.screen.interview.navigation.navigateToInterviewDetail
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoInfoRoute
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateToMap
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateToMapTab
import com.lottomate.lottomate.presentation.screen.pocket.navigation.navigateToPocketTab
import com.lottomate.lottomate.presentation.screen.scan.LottoScanRoute
import com.lottomate.lottomate.presentation.screen.scanResult.LottoScanResultRoute
import com.lottomate.lottomate.presentation.screen.setting.navigation.navigateToSetting
import com.lottomate.lottomate.presentation.screen.winnerguide.WinnerGuideNaverMapWebView
import com.lottomate.lottomate.presentation.screen.winnerguide.WinnerGuideRoute
import com.lottomate.lottomate.presentation.screen.winnerguide.navigation.navigateToWinnerGuide

fun NavController.navigateToHomeTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Home, navOptions)
}

fun NavController.navigateToLottoDetail(type: LottoType, round: Int) {
    navigate(LottoMateRoute.LottoDetail(type, round))
}

fun NavController.navigateToLottoScan() {
    navigate(LottoMateRoute.LottoScan)
}

fun NavController.navigateToLottoScanResult(data: String) {
    navigate(LottoMateRoute.LottoScanResult(data))
}

fun NavController.navigateToBanner(bannerType: BannerType) {
    navigate(bannerType.route)
}

fun NavController.navigateToNaverMap(url: String) {
    navigate(LottoMateRoute.NaverMap(url))
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    composable<BottomTabRoute.Home> {
        HomeRoute(
            padding = padding,
            moveToLottoInfo = { type, round -> navController.navigateToLottoDetail(type, round) },
            moveToSetting = { navController.navigateToSetting() },
            moveToMap = { navController.navigateToMap() },
            moveToScan = { navController.navigateToLottoScan() },
            moveToInterviewDetail = { no, place -> navController.navigateToInterviewDetail(no, place) },
            onClickBanner = { navController.navigateToBanner(it) },
            onShowErrorSnackBar = onShowErrorSnackBar
        )
    }

    // 로또 상세 화면
    composable<LottoMateRoute.LottoDetail> { navBackStackEntry ->
        val type = navBackStackEntry.toRoute<LottoMateRoute.LottoDetail>().type
        val round = navBackStackEntry.toRoute<LottoMateRoute.LottoDetail>().round

        LottoInfoRoute(
            type = type,
            round = round,
            onClickBottomBanner = {},
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
            moveToMap = { navController.navigateToMap() },
            moveToInterview = { no, place -> navController.navigateToInterviewDetail(no, place)},
            moveToPocket = {
                val navOptions = NavOptions.Builder().apply {
                    setPopUpTo<BottomTabRoute.Pocket>(true)
                }.build()

                navController.navigateToPocketTab(navOptions)
            },
            onBackPressed = { navController.popBackStack(route = LottoMateRoute.LottoScan, inclusive = true) },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }

    // 당첨자 가이드 화면 (배너)
    composable<LottoMateRoute.LottoWinnerGuide> {
        WinnerGuideRoute(
            moveToMap = {
                val navOptions = NavOptions.Builder().apply {
                    setPopUpTo<LottoMateRoute.LottoWinnerGuide>(inclusive = true)
                }.build()

                navController.navigateToMapTab(navOptions)
            },
            moveToNaverMap = { navController.navigateToNaverMap(it) },
            onBackPressed = { navController.popBackStack() },
        )
    }

    // 당첨자 가이드 네이버 지도
    composable<LottoMateRoute.NaverMap> { navBackStackEntry ->
        val place = navBackStackEntry.toRoute<LottoMateRoute.NaverMap>().place

        WinnerGuideNaverMapWebView(
            place = place,
            onBackPressed = { navController.navigateUp() },
        )
    }
}