package com.lottomate.lottomate.presentation.screen.setting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHomeTab
import com.lottomate.lottomate.presentation.screen.setting.SettingRoute
import com.lottomate.lottomate.presentation.screen.setting.page.MyPageRoute

fun NavController.navigateToSetting() {
    navigate(LottoMateRoute.Setting)
}

fun NavController.navigateToMyPage() {
    navigate(LottoMateRoute.MyPage)
}

fun NavGraphBuilder.settingNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    // 설정 화면
    composable<LottoMateRoute.Setting> {
        SettingRoute(
            padding = padding,
            moveToMyPage = { navController.navigateToMyPage() },
            onBackPressed = { navController.navigateUp() },
        )
    }

    // 내 계정 관리 화면
    composable<LottoMateRoute.MyPage> {
        MyPageRoute(
            padding = padding,
            moveToHome = {
                val navOptions = NavOptions.Builder().apply {
                    setPopUpTo<BottomTabRoute.Home>(true)
                }.build()

                navController.navigateToHomeTab(navOptions)
            },
            moveToSignOut = {},
            onBackPressed = { navController.navigateUp() },
        )
    }
}