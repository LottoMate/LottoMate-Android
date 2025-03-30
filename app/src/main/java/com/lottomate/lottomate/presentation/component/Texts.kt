package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateRed10
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun LottoMateText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    val size = with(LocalDensity.current) { style.fontSize.value.dp.toSp() }

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontFamily?.let { fontSize } ?: run { size },
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style.copy(fontSize = size),
    )
}

@Composable
fun LottoMateAnnotatedText(
    annotatedString: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    val size = with(LocalDensity.current) { style.fontSize.value.dp.toSp() }

    Text(
        text = annotatedString,
        modifier = modifier,
        color = annotatedString?.let { Color.Unspecified } ?: color,
        fontSize = fontFamily?.let { fontSize } ?: size,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style.copy(fontSize = size),
    )
}

@Preview(showBackground = true)
@Composable
private fun LottoMateTextPreview() {
    LottoMateTheme {
        val text = "로또 당첨으로 인생 역전\n로또 낙첨으로 인생 여전"
        Column(modifier = Modifier.fillMaxSize()) {
            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.display1,
                modifier = Modifier.border(1.dp, LottoMateBlack)
            )

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.display2,
                modifier = Modifier.background(LottoMateRed10)
            )

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.title1,
                modifier = Modifier.background(LottoMateRed10)
            )

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.title2,
                modifier = Modifier.background(LottoMateRed10)
            )

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.title3,
                modifier = Modifier.background(LottoMateRed10)
            )

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.display2,
                modifier = Modifier.background(LottoMateRed10)
            )

            LottoMateText(
                text = text,
                style = LottoMateTheme.typography.display2,
                modifier = Modifier.background(LottoMateRed10)
            )
        }
    }
}

@Composable
private fun lottoMateTextStyle(style: TextStyle = LocalTextStyle.current): LottoMateTextStyle {
    val equals: ((TextStyle, TextStyle) -> Boolean) = { style1, style2 ->
        style1.fontFamily == style2.fontFamily &&
                style1.fontSize == style2.fontSize &&
                style1.fontWeight == style2.fontWeight &&
                style1.lineHeight == style2.lineHeight &&
                style1.letterSpacing == style2.letterSpacing
    }

    return when {
        equals(LottoMateTheme.typography.display1, style) -> LottoMateTextStyle.DISPLAY1
        equals(LottoMateTheme.typography.display2, style) -> LottoMateTextStyle.DISPLAY2
        equals(LottoMateTheme.typography.title1, style) -> LottoMateTextStyle.TITLE1
        equals(LottoMateTheme.typography.title2, style) -> LottoMateTextStyle.TITLE2
        equals(LottoMateTheme.typography.title3, style) -> LottoMateTextStyle.TITLE3
        equals(LottoMateTheme.typography.headline1, style) -> LottoMateTextStyle.HEADLINE1
        equals(LottoMateTheme.typography.headline2, style) -> LottoMateTextStyle.HEADLINE2
        equals(LottoMateTheme.typography.body1, style) -> LottoMateTextStyle.BODY1
        equals(LottoMateTheme.typography.body2, style) -> LottoMateTextStyle.BODY2
        equals(LottoMateTheme.typography.label1, style) -> LottoMateTextStyle.LABEL1
        equals(LottoMateTheme.typography.label2, style) -> LottoMateTextStyle.LABEL2
        equals(LottoMateTheme.typography.caption, style) -> LottoMateTextStyle.CAPTION1
        equals(LottoMateTheme.typography.caption2, style) -> LottoMateTextStyle.CAPTION2
        else -> LottoMateTextStyle.UNSPECIFIED
    }
}

enum class LottoMateTextStyle(
    val firstBaseLineToTop: TextUnit = TextUnit.Unspecified,
    val lastBaseLineToBottom: TextUnit = TextUnit.Unspecified,
) {
    UNSPECIFIED,
    DISPLAY1(48.sp, 14.sp),
    DISPLAY2(41.sp, 13.sp),
    TITLE1(37.sp, 11.sp),
    TITLE2(30.sp, 10.sp),
    TITLE3(26.sp, 8.sp),
    HEADLINE1(20.sp, 8.sp),
    HEADLINE2(18.sp, 6.sp),
    BODY1(18.sp, 6.sp),
    BODY2(18.sp, 6.sp),
    LABEL1(18.sp, 6.sp),
    LABEL2(16.sp, 6.sp),
    CAPTION1(13.sp, 5.sp),
    CAPTION2(12.sp, 4.sp),
}