package com.lottomate.lottomate.presentation.screen.login.navigation

import androidx.navigation.NavController
import com.lottomate.lottomate.presentation.navigation.Route

fun NavController.navigateToLogin() {
    navigate(route = Route.LOGIN.name)
}