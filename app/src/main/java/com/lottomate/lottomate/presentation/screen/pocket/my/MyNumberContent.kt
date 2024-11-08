package com.lottomate.lottomate.presentation.screen.pocket.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun MyNumberContent(
    modifier: Modifier = Modifier,
    onClickQRScan: () -> Unit,
    onClickSaveNumbers: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            LottoMateCard(
                modifier = Modifier,
                onClick = {},
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_left),
                        textAlign = TextAlign.Center,
                        style = LottoMateTheme.typography.headline2,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.pochi_6),
                        contentDescription = stringResource(id = R.string.pocket_desc_my_number_top_left_image),
                        modifier = Modifier.size(76.dp),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LottoMateAssistiveButton(
                        text = stringResource(id = R.string.pocket_text_my_number_scan),
                        buttonSize = LottoMateButtonProperty.Size.MEDIUM,
                        onClick = onClickQRScan,
                    )
                }
            }

            LottoMateCard(
                modifier = Modifier,
                onClick = {},
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_right),
                        textAlign = TextAlign.Center,
                        style = LottoMateTheme.typography.headline2,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.pochi_6),
                        contentDescription = stringResource(id = R.string.pocket_desc_my_number_top_right_image),
                        modifier = Modifier.size(76.dp),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LottoMateAssistiveButton(
                        text = stringResource(id = R.string.pocket_text_my_number_save_number),
                        buttonSize = LottoMateButtonProperty.Size.MEDIUM,
                        onClick = onClickSaveNumbers,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyNumberContentPreview() {
    LottoMateTheme {
        MyNumberContent(
            modifier = Modifier.fillMaxSize(),
            onClickQRScan = {},
            onClickSaveNumbers = {},
        )
    }
}