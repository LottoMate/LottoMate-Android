package com.lottomate.lottomate.presentation.navigation

import com.lottomate.lottomate.data.model.LottoType
import kotlinx.serialization.Serializable

sealed interface LottoMateRoute {
    // 인트로
    @Serializable data object Intro : LottoMateRoute
    // 온보딩
    @Serializable data object Onboarding : LottoMateRoute
    // 권한 확인
    @Serializable data object PermissionNotice : LottoMateRoute

    // 로또 상세
    @Serializable data class LottoDetail(val type: LottoType, val round: Int) : LottoMateRoute
    @Serializable data object LottoWinnerGuide : LottoMateRoute
    @Serializable data class NaverMap(val place: String) : LottoMateRoute

    // 인터뷰
    @Serializable data object Interview : LottoMateRoute
    // 로그인
    @Serializable data object Login : LottoMateRoute
    @Serializable data object LoginComplete : LottoMateRoute

    // 보관소
    @Serializable data object PocketStorage : LottoMateRoute
    @Serializable data object PocketDrawRandomNumbers : LottoMateRoute
    @Serializable data object RegisterLottoNumber : LottoMateRoute
    @Serializable data object LottoScan : LottoMateRoute
    @Serializable data class LottoScanResult(val data: String) : LottoMateRoute

    @Serializable data object Setting : LottoMateRoute
    @Serializable data object MyPage : LottoMateRoute
    @Serializable data object SignOut : LottoMateRoute
}

/**
 * Main Bottom Tab Route
 */
sealed interface BottomTabRoute : LottoMateRoute {
    @Serializable
    data object Home : BottomTabRoute
    @Serializable
    data object Map : BottomTabRoute
    @Serializable
    data object Pocket : BottomTabRoute
    @Serializable
    data object Lounge : BottomTabRoute
}
