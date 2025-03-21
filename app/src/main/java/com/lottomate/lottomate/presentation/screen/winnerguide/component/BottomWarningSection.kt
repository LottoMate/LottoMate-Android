package com.lottomate.lottomate.presentation.screen.winnerguide.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.winnerguide.model.WinnerGuideType
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun BottomWarningSection(
    modifier: Modifier = Modifier,
    type: WinnerGuideType,
) {
    val warningList = type.warningList
    val warningListCaptions = type.warningCaptions

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        LottoMateText(
            text = "주의사항",
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            warningList.forEachIndexed { index, s ->
                GuideNoticeCard(
                    modifier = Modifier.padding(start = if (index == 0) 20.dp else 16.dp, end = if (index == warningList.lastIndex) 20.dp else 0.dp),
                    index = index.plus(1),
                    text = warningList[index],
                    color = LottoMateRed50,
                    captions = warningListCaptions[index] ?: emptyList()
                )
            }
        }
    }
}