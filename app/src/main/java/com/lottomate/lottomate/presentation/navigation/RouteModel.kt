package com.lottomate.lottomate.presentation.navigation

import com.lottomate.lottomate.data.model.LottoType
import kotlinx.serialization.Serializable

sealed interface LottoMateRoute {
    // 로또 상세
    @Serializable data class LottoDetail(val type: LottoType, val round: Int) : LottoMateRoute
    // 인터뷰
    @Serializable data object Interview : LottoMateRoute
    // 로그인
    @Serializable data object Login : LottoMateRoute
    @Serializable data object LoginComplete : LottoMateRoute

    // 보관소
    @Serializable data object PocketStorage : LottoMateRoute
    @Serializable data object PocketDrawRandomNumbers : LottoMateRoute

    @Serializable data object Setting : LottoMateRoute
}

/**
 * Main Bottom Tab Route
 */
sealed interface BottomTabRoute : LottoMateRoute {
    @Serializable data object Home : BottomTabRoute
    @Serializable data object Map : BottomTabRoute
    @Serializable data object Pocket : BottomTabRoute
    @Serializable data object Lounge : BottomTabRoute
}

