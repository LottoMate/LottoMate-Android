package com.lottomate.lottomate.presentation.screen.map.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute
import com.lottomate.lottomate.presentation.screen.map.MapRoute

fun NavController.navigateMap(navOptions: NavOptions) {
    navigate(route = BottomNavigationRoute.MAP.name, navOptions)
}

fun NavGraphBuilder.mapNavGraph(
    padding: PaddingValues,
    navController: NavController,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(BottomNavigationRoute.MAP.name) {
        MapRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}