package com.lottomate.lottomate.presentation.screen.lotto.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.Route
import com.lottomate.lottomate.presentation.screen.interview.InterviewRoute
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoInfoRoute

fun NavGraphBuilder.lottoNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(Route.INFO.name) {
        LottoInfoRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() },
            onClickBottomBanner = {}
        )
    }

    composable(Route.INTERVIEW.name) {
        InterviewRoute(
            onClickBanner = {},
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = {},
        )
    }
}