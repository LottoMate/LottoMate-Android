package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateHome
import com.lottomate.lottomate.presentation.screen.lottoinfo.navigation.navigateLottoInfo
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateMap

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = MainBottomTab.HOME.route

    val currentTab: MainBottomTab?
        @Composable get() = MainBottomTab.find {
            currentDestination?.route == it.name
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
            MainBottomTab.HOME -> navController.navigateHome(navOptions)
            MainBottomTab.MAP -> navController.navigateMap(navOptions)
            MainBottomTab.POCKET -> {}
            MainBottomTab.LOUNGE -> {}
        }
    }

    fun navigateLottoInfo() {
        navController.navigateLottoInfo()
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}