package com.lottomate.lottomate.presentation.screen.interview.navigation

import androidx.navigation.NavController
import com.lottomate.lottomate.presentation.navigation.LottoRoute

fun NavController.navigateInterview() {
    navigate(LottoRoute.Interview.name)
}