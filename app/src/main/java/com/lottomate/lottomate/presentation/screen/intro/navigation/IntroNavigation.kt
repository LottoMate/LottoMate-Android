package com.lottomate.lottomate.presentation.screen.intro.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHomeTab
import com.lottomate.lottomate.presentation.screen.intro.IntroRoute
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
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
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
            moveToHome = {
                val navOptions = NavOptions.Builder().apply {
                    this.setPopUpTo<LottoMateRoute.Intro>(true)
                }.build()

                navController.navigateToHomeTab(navOptions)
            },
        )
    }
}