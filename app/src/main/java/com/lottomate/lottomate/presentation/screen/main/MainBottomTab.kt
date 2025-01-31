package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.runtime.Composable
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.navigation.BottomTabRoute

enum class MainBottomTab(
    val icon: Int,
    val contentDescription: String,
    val route: BottomTabRoute,
) {
    HOME(
        icon = R.drawable.icon_clover,
        contentDescription = "홈",
        route = BottomTabRoute.Home,
    ),
    MAP(
        icon = R.drawable.icon_map,
        contentDescription = "지도",
        route = BottomTabRoute.Map,
    ),
    POCKET(
        icon = R.drawable.icon_pocket,
        contentDescription = "보관소",
        route = BottomTabRoute.Pocket,
    ),
    LOUNGE(
        icon = R.drawable.icon_person,
        contentDescription = "라운지",
        route = BottomTabRoute.Lounge,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (BottomTabRoute) -> Boolean): MainBottomTab? {
            return entries.find { predicate(it.route) }
        }
    }
}