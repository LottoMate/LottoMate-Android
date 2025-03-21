package com.lottomate.lottomate.presentation.screen.winnerguide.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun TopNoticeSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = Dimens.DefaultPadding20)
            .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusLarge))
            .padding(Dimens.DefaultPadding20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.img_rounge_cony2),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            LottoMateText(
                text = stringResource(id = R.string.guide_top_notice_content01),
                style = LottoMateTheme.typography.label1,
            )

            LottoMateText(
                text = stringResource(id = R.string.guide_top_notice_content02),
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateRed50),
                modifier = Modifier.padding(start = 2.dp),
            )

            LottoMateText(
                text = stringResource(id = R.string.guide_top_notice_content03),
                style = LottoMateTheme.typography.label1,
                modifier = Modifier.padding(start = 2.dp),
            )
        }

        LottoMateText(
            text = stringResource(id = R.string.guide_top_notice_content04),
            style = LottoMateTheme.typography.label1,
        )

        LottoMateText(
            text = stringResource(id = R.string.guide_top_notice_sub_content),
            style = LottoMateTheme.typography.caption
                .copy(color = LottoMateGray100),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
        )
    }
}