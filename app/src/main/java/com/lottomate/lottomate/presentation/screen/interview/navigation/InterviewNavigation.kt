package com.lottomate.lottomate.presentation.screen.interview.navigation

import androidx.navigation.NavController
import com.lottomate.lottomate.presentation.navigation.Route

fun NavController.navigateInterview() {
    navigate(Route.INTERVIEW.name)
}