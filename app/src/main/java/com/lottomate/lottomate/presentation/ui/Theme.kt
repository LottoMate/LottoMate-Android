package com.lottomate.lottomate.presentation.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// TODO : Color 선택 완료되면 다시 수정
private val LightColorScheme = lightColorScheme(
    primary = LottoMateRed50,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun LottoMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = LottoMateWhite.toArgb()
        }
    }

    CompositionLocalProvider(LocalLottoMateTypography provides Typography) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            content = content
        )
    }
}

object LottoMateTheme {
    val typography: LottoMateTypography
        @Composable get() = LocalLottoMateTypography.current
}