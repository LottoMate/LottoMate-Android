package com.lottomate.lottomate.presentation.res

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.lottomate.lottomate.R

object Dimens {
    val RadiusExtraSmall: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_extra_small)

    val RadiusSmall: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_small)

    val RadiusMedium: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_medium)

    val RadiusLarge: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_large)

    val RadiusExtraLarge: Dp
        @Composable get() = dimensionResource(id = R.dimen.radius_extra_large)
    
    val TopAppBarHeight: Dp
        @Composable get() = dimensionResource(id = R.dimen.top_app_bar_height)
}