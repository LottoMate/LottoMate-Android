package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun LottoMateDialog(
    title: String? = null,
    body: String? = null,
    cancelText: String? = null,
    confirmText: String? = null,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            colors = CardDefaults.cardColors(
                containerColor = LottoMateWhite,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                title?.let {
                    LottoMateText(
                        text = it,
                        style = LottoMateTheme.typography.headline1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                body?.let {
                    LottoMateText(
                        text = it,
                        style = LottoMateTheme.typography.body2
                            .copy(color = LottoMateGray100),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                ) {
                    cancelText?.let {
                        LottoMateDialogAssistiveButton(
                            text = it,
                            buttonSize = LottoMateButtonProperty.Size.LARGE,
                            onClick = { onDismiss() },
                            textStyle = LottoMateTheme.typography.headline1,
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    confirmText?.let {
                        LottoMateDialogSolidButton(
                            text = it,
                            buttonSize = LottoMateButtonProperty.Size.LARGE,
                            onClick = { onConfirm() },
                            textStyle = LottoMateTheme.typography.headline1,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateDialogPreview() {
    LottoMateTheme {
        LottoMateDialog(
            title = "컨텐츠 제목",
            body = "컨텐츠 내용",
            cancelText = "취소",
            confirmText = "확인",
            onDismiss = {},
            onConfirm = {},
        )
    }
}