package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.lottomate.lottomate.presentation.screen.home.navigation.homeNavGraph
import com.lottomate.lottomate.presentation.screen.login.navigation.loginNavGraph
import com.lottomate.lottomate.presentation.screen.login.navigation.navigateToLogin
import com.lottomate.lottomate.presentation.screen.map.navigation.mapNavGraph
import com.lottomate.lottomate.presentation.screen.pocket.navigation.pocketNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            homeNavGraph(
                padding = padding,
                navController = navigator.navController,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )

            mapNavGraph(
                padding = padding,
                moveToLogin = { navigator.navController.navigateToLogin() },
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
        }
    }
}