package com.lottomate.lottomate.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
fun LottoMateCheckBox(
    isChecked: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    Box(modifier = Modifier
        .size(20.dp)
        .background(
            color = when (isChecked) {
                true -> LottoMateRed50
                else -> LottoMateGray40
            },
            shape = CircleShape
        )
        .noInteractionClickable { onClick(!isChecked) }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_check),
            contentDescription = null,
            tint = LottoMateWhite,
            modifier = Modifier
                .align(Alignment.Center)
                .size(14.dp),
        )
    }
}

@Composable
fun LottoMateCheckBoxWithText(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier
            .size(20.dp)
            .background(
                color = when (isChecked) {
                    true -> LottoMateRed50
                    else -> LottoMateGray40
                },
                shape = CircleShape
            )
            .noInteractionClickable { onClick(!isChecked) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_check),
                contentDescription = null,
                tint = LottoMateWhite,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(14.dp),
            )
        }

        LottoMateText(
            text = text,
            style = LottoMateTheme.typography.body1,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}