package com.lottomate.lottomate.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

/**
 * 유틸리티 클래스로, 텍스트 내 특정 키워드에 스타일을 적용한 AnnotatedString을 생성합니다.
 */
object AnnotatedTextUtils {

    /**
     * 주어진 메시지에서 특정 키워드를 찾아 지정한 스타일을 적용한 AnnotatedString을 반환합니다.
     *
     * @param message 전체 텍스트 문자열
     * @param keyword 강조할 키워드 (message 내에 포함되어 있어야 함)
     * @param style 적용할 SpanStyle (예: 굵게, 색상 등)
     *
     * @return 스타일이 적용된 AnnotatedString. 만약 키워드가 존재하지 않으면 스타일 없이 전체 메시지를 반환합니다.
     *
     * @throws IndexOutOfBoundsException 키워드가 message 내에 없을 경우 예외가 발생할 수 있으므로 사용 시 주의가 필요합니다.
     */
    fun createAnnotatedText(
        message: String,
        keyword: String,
        style: SpanStyle,
    ): AnnotatedString {
        val startIndex = message.indexOf(keyword)
        if (startIndex == -1) return AnnotatedString(message)

        val endIndex = startIndex + keyword.length

        return AnnotatedString.Builder(message).apply {
            addStyle(style, startIndex, endIndex)
        }.toAnnotatedString()
    }
}
