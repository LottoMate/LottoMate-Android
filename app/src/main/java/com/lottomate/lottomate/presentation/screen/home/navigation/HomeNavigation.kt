package com.lottomate.lottomate.presentation.screen.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.HomeRoute
import com.lottomate.lottomate.presentation.screen.home.SettingPage
import com.lottomate.lottomate.presentation.screen.interview.InterviewRoute
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoInfoRoute
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateToMap
import com.lottomate.lottomate.presentation.screen.scan.LottoScanRoute
import com.lottomate.lottomate.presentation.screen.scanResult.LottoScanResultRoute
import com.lottomate.lottomate.presentation.screen.winnerguide.WinnerGuideRoute
import com.lottomate.lottomate.presentation.screen.winnerguide.navigation.navigateToWinnerGuide

fun NavController.navigateToHomeTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Home, navOptions)
}

fun NavController.navigateToSetting() {
    navigate(LottoMateRoute.Setting)
}

fun NavController.navigateToInterviewDetail(no: Int) {
    navigate(LottoMateRoute.InterviewDetail(no))
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

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<BottomTabRoute.Home> {
        HomeRoute(
            padding = padding,
            moveToLottoInfo = { type, round -> navController.navigateToLottoDetail(type, round) },
            moveToSetting = { navController.navigateToSetting() },
            moveToMap = { navController.navigateToMap() },
            moveToScan = { navController.navigateToLottoScan() },
            moveToInterviewDetail = { navController.navigateToInterviewDetail(it) },
            onClickBanner = { navController.navigateToBanner(it) },
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

    // 인터뷰 상세 화면
    composable<LottoMateRoute.InterviewDetail> {navBackStackEntry ->
        val no = navBackStackEntry.toRoute<LottoMateRoute.InterviewDetail>().no

        InterviewRoute(
            no = no,
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
        )
    }

    // 당첨자 가이드 화면 (배너)
    composable<LottoMateRoute.LottoWinnerGuide> {
        WinnerGuideRoute(
            onClickBanner = {
                // TODO : 지도로 이동
            },
            onBackPressed = { navController.popBackStack() },
        )
    }
}