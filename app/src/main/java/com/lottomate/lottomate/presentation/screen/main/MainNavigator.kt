package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute
import com.lottomate.lottomate.presentation.screen.lottoinfo.navigation.navigateLottoInfo
import com.lottomate.lottomate.presentation.screen.review.navigation.navigateInterview

class MainNavigator(
    val navController: NavHostController,
) {
    val startDestination = BottomNavigationRoute.HOME.name

//    fun navigate(bottomTab: MainBottomTab) {
//        val navOptions = navOptions {
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
//
//        when (bottomTab) {
//            MainBottomTab.HOME -> navController.navigateHome(navOptions)
//            MainBottomTab.MAP -> navController.navigateMap(navOptions)
//            MainBottomTab.MYPAGE -> navController.navigateMypage(navOptions)
//        }
//    }

    fun navigateLottoInfo() {
        navController.navigateLottoInfo()
    }

    fun navigateInterview() {
        navController.navigateInterview()
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}