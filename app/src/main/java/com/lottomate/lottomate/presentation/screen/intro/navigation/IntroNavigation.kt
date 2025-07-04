package com.lottomate.lottomate.presentation.screen.intro.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHomeTab
import com.lottomate.lottomate.presentation.screen.intro.IntroRoute
import com.lottomate.lottomate.presentation.screen.login.navigation.navigateToLogin
import com.lottomate.lottomate.presentation.screen.onboarding.OnboardingRoute
import com.lottomate.lottomate.presentation.screen.onboarding.PermissionNoticeRoute

fun NavController.navigateToOnboarding() {
    navigate(LottoMateRoute.Onboarding)
}

fun NavController.navigateToPermissionNotice() {
    navigate(LottoMateRoute.PermissionNotice)
}

fun NavGraphBuilder.introNavGraph(
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    // 인트로 화면
    composable<LottoMateRoute.Intro> {
        IntroRoute(
            moveToHome = {
                val navOptions = NavOptions.Builder().apply {
                    this.setPopUpTo<LottoMateRoute.Intro>(true)
                }.build()

                navController.navigateToHomeTab(navOptions)
            },
            moveToOnboarding = { navController.navigateToOnboarding() },
            moveToLogin = {
                val navOption = NavOptions.Builder().apply {
                    setPopUpTo(LottoMateRoute.Intro, true)
                }.build()

                navController.navigateToLogin(navOption)
            },
        )
    }

    // 온보딩 화면
    composable<LottoMateRoute.Onboarding> {
        OnboardingRoute(
            moveToPermissionNotice = { navController.navigateToPermissionNotice() },
        )
    }

    // 권한 안내 화면
    composable<LottoMateRoute.PermissionNotice> {
        PermissionNoticeRoute(
            moveToLogin = {
                val navOptions = NavOptions.Builder().apply {
                    this.setPopUpTo<LottoMateRoute.Intro>(true)
                }.build()

                navController.navigateToLogin(navOptions)
            },
        )
    }
}