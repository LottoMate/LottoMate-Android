package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.screen.home.navigation.homeNavGraph
import com.lottomate.lottomate.presentation.screen.interview.navigation.interviewNavGraph
import com.lottomate.lottomate.presentation.screen.intro.navigation.introNavGraph
import com.lottomate.lottomate.presentation.screen.login.navigation.loginNavGraph
import com.lottomate.lottomate.presentation.screen.lounge.navigation.loungeNavGraph
import com.lottomate.lottomate.presentation.screen.map.navigation.mapNavGraph
import com.lottomate.lottomate.presentation.screen.pocket.navigation.pocketNavGraph
import com.lottomate.lottomate.presentation.screen.setting.navigation.settingNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    padding: PaddingValues,
    onShowGlobalSnackBar: (message: String) -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
    ) {
        introNavGraph(
            navController = navigator.navController,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        homeNavGraph(
            padding = padding,
            navController = navigator.navController,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        mapNavGraph(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        pocketNavGraph(
            padding = padding,
            navController = navigator.navController,
            onShowGlobalSnackBar = onShowGlobalSnackBar,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        loungeNavGraph(
            padding = padding,
            navController = navigator.navController,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        interviewNavGraph(
            padding = padding,
            navController = navigator.navController,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        loginNavGraph(
            padding = padding,
            navController = navigator.navController,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        settingNavGraph(
            padding = padding,
            navController = navigator.navController,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}