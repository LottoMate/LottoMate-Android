package com.lottomate.lottomate.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.model.LoginTypeUiModel

@Composable
fun LoginIconButton(
    @DrawableRes iconRes: Int,
    @StringRes descRes: Int,
    type: LoginTypeUiModel,
    latest: LoginTypeUiModel?,
    onPositioned: (LayoutCoordinates) -> Unit,
    onClick: () -> Unit = {},
) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = stringResource(descRes),
        modifier = Modifier
            .size(48.dp)
            .onGloballyPositioned {
                if (type == latest) onPositioned(it)
            }
            .clip(CircleShape)
            .clickable { onClick() }
    )
}