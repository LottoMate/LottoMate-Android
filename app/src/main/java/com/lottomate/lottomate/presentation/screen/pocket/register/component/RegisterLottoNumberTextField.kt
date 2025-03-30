package com.lottomate.lottomate.presentation.screen.pocket.register.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateError
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun RegisterLottoNumberTextField(
    modifier: Modifier = Modifier,
    number: String,
    isError: Boolean,
    maxNumberLength: Int,
    visualTransformation: VisualTransformation,
    onChangeValue: (String) -> Unit,
    onClickRemove: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = modifier.padding(bottom = 24.dp),
        value = number,
        textStyle = LottoMateTheme.typography.body1,
        onValueChange = { inputNum ->
            val filtered = inputNum.filter { it.isDigit() }
            if (filtered.length <= maxNumberLength) onChangeValue(inputNum)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
        singleLine = true,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.DefaultPadding20),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (number.isEmpty()) {
                                LottoMateText(
                                    text = stringResource(id = R.string.register_lotto_number_text_placeholder),
                                    style = LottoMateTheme.typography.body1
                                        .copy(color = LottoMateGray60),
                                    modifier = Modifier.padding(bottom = 8.dp),
                                )
                            } else {
                                ProvideTextStyle(value = LottoMateTheme.typography.body1) {
                                    Box(modifier = Modifier.padding(bottom = 8.dp)) {
                                        innerTextField()
                                    }
                                }
                            }
                        }

                        Divider(
                            color = if (isError) LottoMateError else LottoMateGray60,
                        )
                    }

                    Image(
                        bitmap = ImageBitmap.imageResource(
                            id = if (number.isEmpty()) R.drawable.icon_xbox_gray20 else R.drawable.icon_xbox_gray60
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            // padding(start = 27.dp) 인데 숫자가 밑줄로 내려가서 임의로 축소함 (01 25 34 36 27 44)
                            .padding(start = 24.dp)
                            .size(20.dp)
                            .noInteractionClickable { onClickRemove() },
                    )
                }

                if (isError) {
                    LottoMateText(
                        text = stringResource(id = R.string.register_lotto_number_error),
                        style = LottoMateTheme.typography.label2
                            .copy(color = LottoMateError),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .padding(horizontal = Dimens.DefaultPadding20),
                    )
                }
            }
        }
    )
}