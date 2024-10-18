package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.lottomate.lottomate.presentation.screen.home.navigation.homeNavGraph
import com.lottomate.lottomate.presentation.screen.lotto.navigation.lottoNavGraph
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
            startDestination = navigator.startDestination.name,
        ) {
            homeNavGraph(
                padding = padding,
                onClickLottoInfo = { navigator.navigateLottoInfo() },
                onClickInterview = { navigator.navigateInterview() },
                onShowErrorSnackBar = onShowErrorSnackBar,
            )

            mapNavGraph(
                padding = padding,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )

            lottoNavGraph(
                padding = padding,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )

            pocketNavGraph(
                padding = padding,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )
        }
    }
}