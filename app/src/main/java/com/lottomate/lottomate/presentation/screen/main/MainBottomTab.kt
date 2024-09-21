package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.runtime.Composable
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute

enum class MainBottomTab(
    val icon: Int,
    val contentDescription: String,
    val route: BottomNavigationRoute,
) {
    HOME(
        icon = R.drawable.icon_clover,
        contentDescription = "홈",
        route = BottomNavigationRoute.HOME,
    ),
    MAP(
        icon = R.drawable.icon_map,
        contentDescription = "지도",
        route = BottomNavigationRoute.MAP,
    ),
    POCKET(
        icon = R.drawable.icon_pocket,
        contentDescription = "보관소",
        route = BottomNavigationRoute.POCKET,
    ),
    LOUNGE(
        icon = R.drawable.icon_person,
        contentDescription = "라운지",
        route = BottomNavigationRoute.LOUNGE,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (BottomNavigationRoute) -> Boolean): MainBottomTab? {
            return entries.find { predicate(it.route) }
        }
    }
}