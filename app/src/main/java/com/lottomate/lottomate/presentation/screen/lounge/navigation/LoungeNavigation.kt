package com.lottomate.lottomate.presentation.screen.lounge.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToBanner
import com.lottomate.lottomate.presentation.screen.interview.navigation.navigateToInterviewDetail
import com.lottomate.lottomate.presentation.screen.lounge.LoungeRoute
import com.lottomate.lottomate.presentation.screen.setting.navigation.navigateToSetting

fun NavController.navigateToLoungeTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Lounge, navOptions)
}

fun NavGraphBuilder.loungeNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    composable<BottomTabRoute.Lounge> {
        LoungeRoute(
            padding = padding,
            moveToSetting = { navController.navigateToSetting() },
            moveToBanner = { navController.navigateToBanner(it) },
            moveToInterview = { no, place -> navController.navigateToInterviewDetail(no, place) },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}