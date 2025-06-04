package com.lottomate.lottomate.presentation.res

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.pixelsToDp

object Dimens {
    /** 4dp */
    val RadiusExtraSmall: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_extra_small)

    /** 8dp */
    val RadiusSmall: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_small)

    /** 12dp */
    val RadiusMedium: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_medium)

    /** 16dp */
    val RadiusLarge: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_large)

    /** 20dp */
    val RadiusExtraLarge: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_extra_large)

    /** 56dp */
    val TopAppBarHeight: Dp
        @Composable get() = dimensionResource(id = R.dimen.top_app_bar_height)
    
    val BottomTabHeight: Dp
        @Composable get() = dimensionResource(id = R.dimen.bottom_tab_height)

    val StatusBarHeight
        @Composable get() = LocalContext.current.resources.getDimensionPixelSize(
        LocalContext.current.resources.getIdentifier("status_bar_height", "dimen", "android")
    ).run {
        pixelsToDp(pixels = this)
    }

    val NavigationBarHeight
        @Composable get() = LocalContext.current.resources.getDimensionPixelSize(
            LocalContext.current.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        ).run {
            pixelsToDp(pixels = this)
        }

    val BaseTopPadding
        @Composable get() = TopAppBarHeight.plus(StatusBarHeight)

    val DefaultPadding20: Dp = 20.dp
}