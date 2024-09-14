package com.lottomate.lottomate.presentation.screen.lotto.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.LottoRoute
import com.lottomate.lottomate.presentation.screen.lottoinfo.LottoInfoRoute
import com.lottomate.lottomate.presentation.screen.review.InterviewRoute

fun NavGraphBuilder.lottoNavGraph(
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(LottoRoute.INFO.name) {
        LottoInfoRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = {},
            onClickBottomBanner = {}
        )
    }

    composable(LottoRoute.Interview.name) {
        InterviewRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = {},
        )
    }
}