package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateRed5
import com.lottomate.lottomate.presentation.ui.LottoMateRed70
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateTransparent
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun LottoMateSolidButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = LottoMateWhite,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    pressedButtonColor: Color = LottoMateRed70,
    buttonSize: LottoMateButtonProperty.Size,
    buttonType: LottoMateButtonProperty.Type = LottoMateButtonProperty.Type.ACTIVE,
    buttonShape: LottoMateButtonProperty.Shape = LottoMateButtonProperty.Shape.NORMAL,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDisabled = buttonType == LottoMateButtonProperty.Type.DISABLED

    LottoMateBaseButton(
        modifier = modifier,
        text = text,
        textColor = textColor,
        buttonColor = if (isDisabled) LottoMateGray40 else buttonColor,
        pressedButtonColor = pressedButtonColor,
        buttonBorder = BorderStroke(
            width = 0.dp,
            color = LottoMateTransparent
        ),
        buttonSize = buttonSize,
        buttonShape = buttonShape,
        enabled = !isDisabled,
        interactionSource = interactionSource,
        onClick = { if (!isDisabled) onClick() }
    )
}


@Composable
fun LottoMateDialogSolidButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = LottoMateWhite,
    textStyle: TextStyle = LocalTextStyle.current,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    pressedButtonColor: Color = LottoMateRed70,
    buttonSize: LottoMateButtonProperty.Size,
    buttonType: LottoMateButtonProperty.Type = LottoMateButtonProperty.Type.ACTIVE,
    buttonShape: LottoMateButtonProperty.Shape = LottoMateButtonProperty.Shape.NORMAL,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDisabled = buttonType == LottoMateButtonProperty.Type.DISABLED

    LottoMateDialogButton(
        modifier = modifier,
        text = text,
        textColor = textColor,
        textStyle = textStyle,
        buttonColor = if (isPressed) pressedButtonColor
        else if (isDisabled) LottoMateGray40
        else buttonColor,
        buttonBorder = BorderStroke(
            width = 0.dp,
            color = LottoMateTransparent
        ),
        buttonSize = buttonSize,
        buttonShape = buttonShape,
        enabled = !isDisabled,
        interactionSource = interactionSource,
        onClick = { onClick() }
    )
}

@Composable
fun LottoMateOutLineButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    buttonColor: Color = LottoMateTransparent,
    buttonBorderColor: Color = MaterialTheme.colorScheme.primary,
    pressedTextColor: Color = LottoMateRed70,
    pressedButtonColor: Color = LottoMateRed5,
    pressedBorderColor: Color = LottoMateRed70,
    buttonSize: LottoMateButtonProperty.Size,
    buttonType: LottoMateButtonProperty.Type = LottoMateButtonProperty.Type.ACTIVE,
    buttonShape: LottoMateButtonProperty.Shape = LottoMateButtonProperty.Shape.NORMAL,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDisabled = buttonType == LottoMateButtonProperty.Type.DISABLED

    LottoMateBaseButton(
        modifier = modifier,
        text = text,
        textColor = if (isPressed) pressedTextColor
        else if (isDisabled) LottoMateGray40
        else textColor,
        buttonColor = buttonColor,
        pressedButtonColor = pressedButtonColor,
        buttonBorder = BorderStroke(
            width = 1.dp,
            color = if (isDisabled) LottoMateGray40
            else if (isPressed) pressedBorderColor
            else buttonBorderColor
        ),
        buttonSize = buttonSize,
        buttonShape = buttonShape,
        enabled = !isDisabled,
        interactionSource = interactionSource,
        onClick = { onClick() }
    )
}

@Composable
fun LottoMateAssistiveButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = LottoMateBlack,
    buttonColor: Color = LottoMateTransparent,
    buttonBorderColor: Color = LottoMateGray40,
    pressedTextColor: Color = LottoMateBlack,
    pressedButtonColor: Color = LottoMateGray20,
    pressedBorderColor: Color = LottoMateGray40,
    buttonSize: LottoMateButtonProperty.Size,
    buttonType: LottoMateButtonProperty.Type = LottoMateButtonProperty.Type.ACTIVE,
    buttonShape: LottoMateButtonProperty.Shape = LottoMateButtonProperty.Shape.NORMAL,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDisabled = buttonType == LottoMateButtonProperty.Type.DISABLED

    LottoMateBaseButton(
        modifier = modifier,
        text = text,
        textColor = if (isPressed) pressedTextColor
        else if (isDisabled) LottoMateGray40
        else textColor,
        buttonColor = buttonColor,
        pressedButtonColor = pressedButtonColor,
        buttonBorder = BorderStroke(
            width = 1.dp,
            color = if (isDisabled) LottoMateGray40
            else if (isPressed) pressedBorderColor
            else buttonBorderColor
        ),
        buttonSize = buttonSize,
        buttonShape = buttonShape,
        interactionSource = interactionSource,
        enabled = !isDisabled,
        onClick = { onClick() }
    )
}

@Composable
fun LottoMateDialogAssistiveButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = LottoMateBlack,
    textStyle: TextStyle = LocalTextStyle.current,
    buttonColor: Color = LottoMateTransparent,
    buttonBorderColor: Color = LottoMateGray40,
    pressedTextColor: Color = LottoMateBlack,
    pressedButtonColor: Color = LottoMateGray20,
    pressedBorderColor: Color = LottoMateGray40,
    buttonSize: LottoMateButtonProperty.Size,
    buttonType: LottoMateButtonProperty.Type = LottoMateButtonProperty.Type.ACTIVE,
    buttonShape: LottoMateButtonProperty.Shape = LottoMateButtonProperty.Shape.NORMAL,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDisabled = buttonType == LottoMateButtonProperty.Type.DISABLED

    LottoMateDialogButton(
        modifier = modifier,
        text = text,
        textColor = if (isPressed) pressedTextColor
        else if (isDisabled) LottoMateGray40
        else textColor,
        textStyle = textStyle,
        buttonColor = if (isPressed) pressedButtonColor else buttonColor,
        buttonBorder = BorderStroke(
            width = 1.dp,
            color = if (isDisabled) LottoMateGray40
            else if (isPressed) pressedBorderColor
            else buttonBorderColor
        ),
        buttonSize = buttonSize,
        buttonShape = buttonShape,
        enabled = !isDisabled,
        interactionSource = interactionSource,
        onClick = { onClick() }
    )
}

@Composable
fun LottoMateTextButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    buttonColor: Color = LottoMateTransparent,
    pressedTextColor: Color = LottoMateRed70,
    pressedButtonColor: Color = LottoMateRed5,
    buttonSize: LottoMateButtonProperty.Size,
    buttonType: LottoMateButtonProperty.Type = LottoMateButtonProperty.Type.ACTIVE,
    textAlign: TextAlign = TextAlign.Center,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDisabled = buttonType == LottoMateButtonProperty.Type.DISABLED

    LottoMateText(
        text = buttonText,
        style = when (buttonSize) {
            LottoMateButtonProperty.Size.LARGE -> LottoMateTheme.typography.label1
            else -> LottoMateTheme.typography.label2
        },
        color = if (isPressed) pressedTextColor
        else if (isDisabled) LottoMateGray40
        else textColor,
        textAlign = textAlign,
        modifier = Modifier.noInteractionClickable { onClick() }
    )
}

@Composable
private fun LottoMateBaseButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    buttonColor: Color,
    pressedButtonColor: Color,
    buttonBorder: BorderStroke?,
    buttonSize: LottoMateButtonProperty.Size,
    buttonShape: LottoMateButtonProperty.Shape,
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = when (buttonShape) {
        LottoMateButtonProperty.Shape.NORMAL -> RoundedCornerShape(Dimens.RadiusSmall)
        LottoMateButtonProperty.Shape.NORMAL_XSMALL -> RoundedCornerShape(Dimens.RadiusExtraSmall)
        LottoMateButtonProperty.Shape.ROUND -> RoundedCornerShape(60.dp)
        LottoMateButtonProperty.Shape.ROUND_SMALL -> RoundedCornerShape(Dimens.RadiusExtraSmall)
    }

    Surface(
        modifier = modifier
            .clip(shape)
            .then(
                if (enabled) Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(
                            bounded = true,
                            color = pressedButtonColor,
                        ),
                        role = Role.Button,
                        onClick = onClick
                    ) else Modifier
            )
            .semantics {
                role = Role.Button
                if (!enabled) this.disabled()
            },
        color = buttonColor,
        shape = shape,
        border = buttonBorder,
    ) {
        LottoMateText(
            text = text,
            color = textColor,
            style = when (buttonSize) {
                LottoMateButtonProperty.Size.XSMALL -> LottoMateTheme.typography.caption
                LottoMateButtonProperty.Size.SMALL -> LottoMateTheme.typography.label2
                LottoMateButtonProperty.Size.LARGE -> LottoMateTheme.typography.headline1
                else -> LottoMateTheme.typography.label1
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .semantics { role = Role.Button }
                .padding(
                    horizontal = when (buttonSize) {
                        LottoMateButtonProperty.Size.XSMALL -> 8.dp
                        LottoMateButtonProperty.Size.SMALL -> 16.dp
                        LottoMateButtonProperty.Size.MEDIUM -> 22.dp
                        LottoMateButtonProperty.Size.LARGE -> 40.dp
                    },
                    vertical = when (buttonSize) {
                        LottoMateButtonProperty.Size.XSMALL -> 2.dp
                        LottoMateButtonProperty.Size.SMALL -> 6.dp
                        LottoMateButtonProperty.Size.MEDIUM -> 8.dp
                        LottoMateButtonProperty.Size.LARGE -> 12.dp
                    }
                ),
        )
    }
}

@Composable
private fun LottoMateDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    textStyle: TextStyle = LocalTextStyle.current,
    buttonColor: Color,
    buttonBorder: BorderStroke?,
    buttonSize: LottoMateButtonProperty.Size,
    buttonShape: LottoMateButtonProperty.Shape,
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val buttonHeight = when (buttonSize) {
        LottoMateButtonProperty.Size.LARGE -> 48.dp
        LottoMateButtonProperty.Size.MEDIUM -> 40.dp
        LottoMateButtonProperty.Size.SMALL -> 34.dp
        LottoMateButtonProperty.Size.XSMALL -> 22.dp
    }

    Button(
        modifier = modifier,
        contentPadding = PaddingValues(
            vertical = when (buttonSize) {
                LottoMateButtonProperty.Size.LARGE -> 12.dp
                LottoMateButtonProperty.Size.MEDIUM -> 8.dp
                LottoMateButtonProperty.Size.SMALL -> 6.dp
                LottoMateButtonProperty.Size.XSMALL -> 2.dp
            }
        ),
        border = buttonBorder,
        shape = when (buttonShape) {
            LottoMateButtonProperty.Shape.NORMAL -> RoundedCornerShape(Dimens.RadiusSmall)
            LottoMateButtonProperty.Shape.NORMAL_XSMALL -> RoundedCornerShape(Dimens.RadiusExtraSmall)
            LottoMateButtonProperty.Shape.ROUND_SMALL -> RoundedCornerShape(Dimens.RadiusExtraSmall)
            LottoMateButtonProperty.Shape.ROUND -> RoundedCornerShape(60.dp)
        },
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = buttonColor,
            containerColor = buttonColor,
        ),
        onClick = { onClick() },
    ) {
        LottoMateText(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}

sealed interface LottoMateButtonProperty {
    enum class Shape {
        NORMAL, NORMAL_XSMALL, ROUND, ROUND_SMALL,
    }

    enum class Size {
        XSMALL, SMALL, MEDIUM, LARGE,
    }

    enum class Type {
        ACTIVE, DISABLED,
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateSolidButtonPreview() {
    LottoMateTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LottoMateSolidButton(
                text = "Small Solid Button",
                buttonSize = LottoMateButtonProperty.Size.SMALL,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateSolidButton(
                text = "Medium Disabled Solid Button",
                buttonSize = LottoMateButtonProperty.Size.MEDIUM,
                buttonType = LottoMateButtonProperty.Type.DISABLED,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateSolidButton(
                text = "Large Solid Button",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateSolidButton(
                text = "Large Rounded Solid Button",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.ROUND,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateOutlineButtonPreview() {
    LottoMateTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LottoMateOutLineButton(
                text = "확인",
                buttonSize = LottoMateButtonProperty.Size.XSMALL,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL_XSMALL,
                onClick = {}
            )

            LottoMateOutLineButton(
                text = "Small Outline Button",
                buttonSize = LottoMateButtonProperty.Size.SMALL,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateOutLineButton(
                text = "Medium Outline Button",
                buttonSize = LottoMateButtonProperty.Size.MEDIUM,
                buttonType = LottoMateButtonProperty.Type.DISABLED,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateOutLineButton(
                text = "Large Outline Button",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateOutLineButton(
                text = "Large Rounded Outline Button",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.ROUND,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateTextButtonPreview() {
    LottoMateTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LottoMateTextButton(
                buttonText = "Large Text Button",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateTextButton(
                buttonText = "Large Disabled Text Button",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.DISABLED,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateTextButton(
                buttonText = "Small Text Button",
                buttonSize = LottoMateButtonProperty.Size.SMALL,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateAssistiveButtonPreview() {
    LottoMateTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LottoMateAssistiveButton(
                text = "취소",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateAssistiveButton(
                text = "Large Disabled Assistive Button",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonType = LottoMateButtonProperty.Type.DISABLED,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateAssistiveButton(
                text = "Small Assistive Button",
                buttonSize = LottoMateButtonProperty.Size.SMALL,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LottoMateAssistiveButton(
                text = "Small Rounded Assistive Button",
                buttonSize = LottoMateButtonProperty.Size.SMALL,
                buttonType = LottoMateButtonProperty.Type.ACTIVE,
                buttonShape = LottoMateButtonProperty.Shape.ROUND,
                onClick = {}
            )
        }
    }
}