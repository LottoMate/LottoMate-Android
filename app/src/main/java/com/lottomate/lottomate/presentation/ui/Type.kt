package com.lottomate.lottomate.presentation.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
)

private val display1 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 48.sp,
    lineHeight = 62.sp,
)

private val display2 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 40.sp,
    lineHeight = 54.sp,
)

private val title1 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    lineHeight = 48.sp,
)

private val title2 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 40.sp,
)

private val title3 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 34.sp,
)

private val headline1 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 28.sp,
)

private val headline2 = pretendardStyle.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 26.sp,
)

private val body1 = pretendardStyle.copy(
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

private val body2 = pretendardStyle.copy(
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

private val label1 = pretendardStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

private val label2 = pretendardStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 22.sp,
)

private val label3 = pretendardStyle.copy(
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 18.sp,
)

val Typography = Typography(
    displayLarge = display1,
    displayMedium = display2,

    titleLarge = title1,
    titleMedium = title2,
    titleSmall = title3,

    headlineLarge = headline1,
    headlineMedium = headline2,

    bodyLarge = body1,
    bodyMedium = body2,

    labelLarge = label1,
    labelMedium = label2,
    labelSmall = label3,
)