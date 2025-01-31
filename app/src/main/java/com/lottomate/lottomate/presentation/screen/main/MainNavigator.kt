package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHomeTab
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateToMapTab
import com.lottomate.lottomate.presentation.screen.pocket.navigation.navigateToPocketTab

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = MainBottomTab.HOME.route

    val isInMainBottomTab
        @Composable get() = currentTab != null

    val currentTab: MainBottomTab?
        @Composable get() = MainBottomTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigate(bottomTab: MainBottomTab) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (bottomTab) {
            MainBottomTab.HOME -> navController.navigateToHomeTab(navOptions)
            MainBottomTab.MAP -> navController.navigateToMapTab(navOptions)
            MainBottomTab.POCKET -> navController.navigateToPocketTab(navOptions)
            MainBottomTab.LOUNGE -> {}
        }
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}