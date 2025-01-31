package com.lottomate.lottomate.presentation.screen.lottoinfo.navigation

import androidx.navigation.NavController
import com.lottomate.lottomate.presentation.navigation.LottoMateRoute

fun NavController.navigateLottoInfo() {
    navigate(LottoMateRoute.LottoDetail)
}