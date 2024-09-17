package com.lottomate.lottomate.presentation.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.lottomate.lottomate.R

private val pretendardFontFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold),
)

private val pretendardStyle = TextStyle(
    fontFamily = pretendardFontFamily,
    letterSpacing = (-0.6).sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

private val display1 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 52.sp,
    lineHeight = 62.sp,
)

private val display2 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 42.sp,
    lineHeight = 54.sp,
)

private val title1 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 40.sp,
    lineHeight = 48.sp,
)

private val title2 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 31.sp,
    lineHeight = 40.sp,
)

private val title3 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 27.sp,
    lineHeight = 34.sp,
)

private val headline1 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
)

private val headline2 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 26.sp,
)

private val body1 = pretendardStyle.copy(
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    lineHeight = 24.sp,
)

private val body2 = pretendardStyle.copy(
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    lineHeight = 24.sp,
)

private val label1 = pretendardStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
)

private val label2 = pretendardStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 15.sp,
    lineHeight = 22.sp,
)

private val caption1 = pretendardStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
)

private val caption2 = pretendardStyle.copy(
    fontWeight = FontWeight.Medium,
    fontSize = 10.sp,
    lineHeight = 16.sp,
)

@Immutable
data class LottoMateTypography(
    val display1: TextStyle,
    val display2: TextStyle,
    val title1: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val headline1: TextStyle,
    val headline2: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val label1: TextStyle,
    val label2: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle,
)

val Typography = LottoMateTypography(
    display1 = display1,
    display2 = display2,
    title1 = title1,
    title2 = title2,
    title3 = title3,
    headline1 = headline1,
    headline2 = headline2,
    body1 = body1,
    body2 = body2,
    label1 = label1,
    label2 = label2,
    caption1 = caption1,
    caption2 = caption2,
)

val LocalLottoMateTypography = staticCompositionLocalOf {
    LottoMateTypography(
        display1 = display1,
        display2 = display2,
        title1 = title1,
        title2 = title2,
        title3 = title3,
        headline1 = headline1,
        headline2 = headline2,
        body1 = body1,
        body2 = body2,
        label1 = label1,
        label2 = label2,
        caption1 = caption1,
        caption2 = caption2,
    )
}