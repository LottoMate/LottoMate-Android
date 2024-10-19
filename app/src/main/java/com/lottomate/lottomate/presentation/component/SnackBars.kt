package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateRed40
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun LottoMateSnackBarWithButton(
    modifier: Modifier = Modifier,
    message: String,
    buttonText: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.background(LottoMateBlack.copy(alpha = 0.68f), RoundedCornerShape(Dimens.RadiusSmall)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            LottoMateText(
                text = message,
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateWhite),
                textAlign = TextAlign.Left,
            )

            Spacer(modifier = Modifier.width(24.dp))

            LottoMateTextButton(
                buttonText = buttonText,
                buttonSize = LottoMateButtonProperty.Size.SMALL,
                onClick = onClick,
                textColor = LottoMateRed40,
            )
        }
    }
}
@Composable
fun LottoMateSnackBarWithIcon(
    modifier: Modifier = Modifier,
    message: String,
    icon: String,
) {
    Box(
        modifier = modifier
            .background(LottoMateBlack.copy(alpha = 0.68f), RoundedCornerShape(Dimens.RadiusSmall)),
        contentAlignment = Alignment.Center,
    ) {
        Row(modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)) {
            LottoMateText(
                text = icon,
                textAlign = TextAlign.Center,
            )
            
            Spacer(modifier = Modifier.width(8.dp))

            LottoMateText(
                text = message,
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateWhite),
                textAlign = TextAlign.Left,
            )
        }
    }
}
@Composable
fun LottoMateSnackBar(
    modifier: Modifier = Modifier,
    message: String,
) {
    Box(
        modifier = modifier
            .background(LottoMateBlack.copy(alpha = 0.68f), RoundedCornerShape(Dimens.RadiusSmall)),
        contentAlignment = Alignment.Center,
    ) {
        LottoMateText(
            text = message,
            style = LottoMateTheme.typography.label2
                .copy(color = LottoMateWhite),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateSnackBarsPreview() {
    LottoMateTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            LottoMateSnackBar(
                message = "Toast Content"
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            LottoMateSnackBar(
                message = "Enter your contents\n" +
                        "Up to 2 lines are allow",
            )

            Spacer(modifier = Modifier.height(8.dp))

            LottoMateSnackBarWithIcon(
                message = "Toast Contents with Icon",
                icon = "🙏"
            )

            Spacer(modifier = Modifier.height(8.dp))

            LottoMateSnackBarWithIcon(
                message = "Enter your contents\n" +
                        "Up to 2 lines are allow",
                icon = "🙏"
            )

            Spacer(modifier = Modifier.height(8.dp))

            LottoMateSnackBarWithButton(
                message = "Toast Contents with Icon",
                buttonText = "Button",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(8.dp))

            LottoMateSnackBarWithButton(
                message = "Enter your contents\n" +
                        "Up to 2 lines are allow",
                buttonText = "Button",
                onClick = {}
            )
        }
    }
}