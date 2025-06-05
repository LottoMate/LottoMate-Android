package com.lottomate.lottomate.presentation.screen.interview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.component.LottoMateBasicWebView
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToBanner
import com.lottomate.lottomate.presentation.screen.interview.InterviewRoute

fun NavController.navigateToInterviewDetail(no: Int, place: String) {
    navigate(LottoMateRoute.InterviewDetail(no, place))
}

fun NavController.navigateToOriginalInterview(no: Int) {
    navigate(LottoMateRoute.OriginalInterview(no))
}

fun NavGraphBuilder.interviewNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    // 인터뷰 상세 화면
    composable<LottoMateRoute.InterviewDetail> { navBackStackEntry ->
        val no = navBackStackEntry.toRoute<LottoMateRoute.InterviewDetail>().no
        val place = navBackStackEntry.toRoute<LottoMateRoute.InterviewDetail>().place

        InterviewRoute(
            no = no,
            place = place,
            moveToOriginalInterview = { navController.navigateToOriginalInterview(it) },
            onClickBanner = { navController.navigateToBanner(BannerType.MAP) },
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackPressed = { navController.navigateUp() },
        )
    }

    // 동행복권 당첨자 후기 화면
    composable<LottoMateRoute.OriginalInterview> {navBackStackEntry ->
        val no = navBackStackEntry.toRoute<LottoMateRoute.OriginalInterview>().no

        LottoMateBasicWebView(
            title = R.string.top_app_bar_empty_title,
            url = "https://m.dhlottery.co.kr/gameResult.do?method=highWinView&txtNo=$no",
            onBackPressed = { navController.navigateUp() },
        )
    }
}