package com.lottomate.lottomate.presentation.feature.main

import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.navigation.BottomNavigationRoute

enum class MainBottomTab(
    val icon: Int,
    val contentDescription: String,
    val route: BottomNavigationRoute,
) {
    HOME(
        icon = R.drawable.icon_home,
        contentDescription = "홈",
        route = BottomNavigationRoute.HOME
    ),
    MAP(
        icon = R.drawable.icon_map,
        contentDescription = "지도",
        route = BottomNavigationRoute.MAP
    ),
    MYPAGE(
        icon = R.drawable.icon_mypage,
        contentDescription = "마이페이지",
        route = BottomNavigationRoute.MYPAGE
    )
}