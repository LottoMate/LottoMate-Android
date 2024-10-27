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
import com.lottomate.lottomate.presentation.screen.home.navigation.navigateToHome
import com.lottomate.lottomate.presentation.screen.interview.navigation.navigateInterview
import com.lottomate.lottomate.presentation.screen.login.navigation.navigateToLogin
import com.lottomate.lottomate.presentation.screen.lottoinfo.navigation.navigateLottoInfo
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateMap
import com.lottomate.lottomate.presentation.screen.map.navigation.navigateToMap
import com.lottomate.lottomate.presentation.screen.pocket.navigation.navigatePocket

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
            MainBottomTab.POCKET -> navController.navigatePocket(navOptions)
            MainBottomTab.LOUNGE -> {}
        }
    }

    fun navigateToHome() {
        navController.navigateToHome()
    }

    fun navigateToMap() {
        navController.navigateToMap()
    }

    fun navigateLottoInfo() {
        navController.navigateLottoInfo()
    }

    fun navigateInterview() {
        navController.navigateInterview()
    }

    fun navigateToLogin() {
        navController.navigateToLogin()
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}