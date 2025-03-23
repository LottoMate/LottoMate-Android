package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.screen.home.navigation.homeNavGraph
import com.lottomate.lottomate.presentation.screen.intro.navigation.introNavGraph
import com.lottomate.lottomate.presentation.screen.login.navigation.loginNavGraph
import com.lottomate.lottomate.presentation.screen.main.model.FullScreenType
import com.lottomate.lottomate.presentation.screen.map.navigation.mapNavGraph
import com.lottomate.lottomate.presentation.screen.pocket.navigation.pocketNavGraph
import com.lottomate.lottomate.presentation.screen.setting.navigation.settingNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    padding: PaddingValues,
    onShowFullScreen: (FullScreenType) -> Unit,
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
            onShowFullScreen = onShowFullScreen,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )

        pocketNavGraph(
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