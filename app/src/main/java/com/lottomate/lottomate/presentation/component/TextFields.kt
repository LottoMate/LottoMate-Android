package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateError
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMatePositive
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LottoMateMultiTextField(
    text: String,
    placeHolder: String? = null,
    hasLimitTextLength: Boolean = false,
    textLengthLimit: Int = 200,
    textFieldHeight: Dp = 140.dp,
    textStyle: TextStyle = LottoMateTheme.typography.body1.copy(textDecoration = TextDecoration.None),
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    var isFocused by remember { mutableStateOf(false) }
    var isLimit by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            },
        enabled = enabled,
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions.copy(
            autoCorrectEnabled = false,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        value = text,
        onValueChange = when (hasLimitTextLength) {
            true -> {
                { newText ->
                    // 글자수 제한
                    if (hasLimitTextLength) {
                        isLimit = newText.length >= textLengthLimit

                        if (newText.length <= textLengthLimit) onTextChange(newText)
                    } else onTextChange(newText)
                }
            }
            false -> onTextChange
        },
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(textFieldHeight)
                .border(
                    1.dp,
                    color = if (isLimit) LottoMateError else LottoMateGray60,
                    RoundedCornerShape(Dimens.RadiusSmall)
                )
                .background(LottoMateWhite)
                .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (text.isEmpty()) {
                            placeHolder?.let { holder ->
                                LottoMateText(
                                    text = holder,
                                    style = LottoMateTheme.typography.body1
                                        .copy(color = LottoMateGray60),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }

                        innerTextField()
                    }

                    if (hasLimitTextLength) {
                        LottoMateText(
                            text = "${text.length}/$textLengthLimit",
                            style = LottoMateTheme.typography.label1
                                .copy(color = if (isLimit) LottoMateError else LottoMateGray100),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun LottoMateTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeHolder: String,
    supportText: String? = null,
    errorText: String? = null,
    limitTextLength: Int? = null,
    onChangeValue: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val isError = errorText != null

    BasicTextField(
        modifier = modifier.padding(bottom = 24.dp),
        value = text,
        textStyle = LottoMateTheme.typography.body1,
        onValueChange = { input ->
            limitTextLength?.let { limitLength ->
                if (input.length <= limitLength) onChangeValue(input)
            } ?: run { onChangeValue(input) }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        if (text.isEmpty()) {
                            LottoMateText(
                                text = placeHolder,
                                style = LottoMateTheme.typography.body1
                                    .copy(color = LottoMateGray60),
                                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
                            )
                        } else {
                            Box(modifier = Modifier.padding(bottom = 8.dp)) {
                                innerTextField()
                            }
                        }
                    }

                    limitTextLength?.let {
                        LottoMateText(
                            text = "${text.length}/$it",
                            style = LottoMateTheme.typography.label1
                                .copy(color = if (isError) LottoMateError else LottoMateGray100),
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = 2.dp),
                        )
                    }
                }

                HorizontalDivider(
                    color = if (isError) LottoMateError else LottoMateGray60,
                )


                supportText?.let { supportText ->
                    LottoMateText(
                        text = supportText,
                        style = LottoMateTheme.typography.label2
                            .copy(color = LottoMatePositive),
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }

                errorText?.let { errorText ->
                    LottoMateText(
                        text = errorText,
                        style = LottoMateTheme.typography.label2
                            .copy(color = LottoMateError),
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun LottoMateTextFieldPreview() {
    var text1 by remember { mutableStateOf("") }
    Column {
        LottoMateTextField(
            text = text1,
            onChangeValue = { text1 = it },
            placeHolder = "2글자 이상 입력",
            limitTextLength = 10,
            errorText = "사용할 수 있는 닉네임이에요"
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun LottoMateMultiTextFieldPreview() {
    LottoMateTheme {
        var text by remember { mutableStateOf("") }
        var text1 by remember { mutableStateOf("") }
        val goodByeFocusRequester = remember { FocusRequester() }

        Column {
            LottoMateMultiTextField(
                modifier = Modifier.padding(20.dp),
                text = text,
                placeHolder = "텍스트를 입력하세요.",
                textLengthLimit = 20,
                onTextChange = { text = it },
                cursorBrush = SolidColor(Color.Transparent),
                textStyle = LottoMateTheme.typography.body1,
            )

            LottoMateMultiTextField(
                modifier = Modifier.padding(20.dp),
                text = text1,
                placeHolder = "텍스트를 입력하세요.",
                hasLimitTextLength = true,
                textLengthLimit = 20,
                onTextChange = { text1 = it },
                cursorBrush = SolidColor(Color.Transparent),
                textStyle = LottoMateTheme.typography.body1,
            )
        }
    }
}