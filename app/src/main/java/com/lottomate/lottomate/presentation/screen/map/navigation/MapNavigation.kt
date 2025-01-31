package com.lottomate.lottomate.presentation.screen.map.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute
import com.lottomate.lottomate.presentation.screen.map.MapRoute

fun NavController.navigateToMapTab(navOptions: NavOptions) {
    navigate(BottomTabRoute.Map, navOptions)
}

fun NavController.navigateToMap() {
    navigate(BottomTabRoute.Map)
}

fun NavGraphBuilder.mapNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<BottomTabRoute.Map> {
        MapRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}