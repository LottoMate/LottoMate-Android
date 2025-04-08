package com.lottomate.lottomate.presentation.screen.pocket.register.component

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class Lotto645Transformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 12) text.text.substring(0..11) else text.text
        var numberWithDash = ""
        val dash = "    -    "

        for (i in trimmed.indices) {
            numberWithDash += trimmed[i]
            if (i % 2 == 1 && i != 11) numberWithDash += dash
        }

        val lottoNumberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    offset <= 3 -> offset + dash.length
                    offset <= 5 -> offset + (dash.length * 2)
                    offset <= 7 -> offset + (dash.length * 3)
                    offset <= 9 -> offset + (dash.length * 4)
                    offset <= 12 -> offset + (dash.length * 5)
                    else -> 57
                }

            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    offset in 2..11 -> 2
                    offset == 12 -> 3
                    offset in 13..22 -> 4
                    offset == 23 -> 5
                    offset in 24..33 -> 6
                    offset == 34 -> 7
                    offset in 35..44 -> 8
                    offset == 45 -> 9
                    offset in 46..55 -> 10
                    offset == 56 -> 11
                    offset == 57 -> 12
                    else -> 12
                }
            }
        }

        return TransformedText(AnnotatedString(numberWithDash), lottoNumberOffsetTranslator)
    }
}

class Lotto720Transformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
        var numberWithDash = ""
        val dash = "    -    "

        for (i in trimmed.indices) {
            numberWithDash += trimmed[i]
            if (i != 5) numberWithDash += dash
        }

        val lottoNumberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 5 -> offset + (9 * offset)
                    else -> 51
                }

            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset == 0 -> offset
                    offset in 1..10 -> 1
                    offset in 11..20 -> 2
                    offset in 21..30 -> 3
                    offset in 31..40 -> 4
                    offset in 41..50 -> 5
                    else -> 6
                }
            }
        }

        return TransformedText(AnnotatedString(numberWithDash), lottoNumberOffsetTranslator)
    }
}