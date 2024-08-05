package com.lottomate.lottomate.presentation.feature.lottoinfo.navigation

import androidx.navigation.NavController
import com.lottomate.lottomate.presentation.navigation.LottoRoute

fun NavController.navigateLottoInfo() {
    navigate(LottoRoute.INFO.name)
}