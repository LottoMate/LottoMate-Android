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
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun MiddlePrizeClaimSection(
    modifier: Modifier = Modifier,
    type: WinnerGuideType,
) {
    val prizeClaimList = type.prizeClaimList
    val prizeClaimListCaptions = type.prizeClaimListCaptions
    val prizeClaimSubList = type.prizeClaimSubList

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        LottoMateText(
            text = "당첨금 받는 방법",
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
        )

        LottoMateText(
            text = "소득세, 주민세 등 원천징수를 적용한 금액을 당첨금으로 받게 됩니다.",
            style = LottoMateTheme.typography.caption
                .copy(color = LottoMateGray80),
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            prizeClaimList.forEachIndexed { index, s ->
                GuideNoticeCard(
                    modifier = Modifier.padding(start = if (index == 0) 20.dp else 16.dp, end = if (index == prizeClaimList.lastIndex) 20.dp else 0.dp),
                    index = index.plus(1),
                    text = prizeClaimList[index],
                    captions = prizeClaimListCaptions[index] ?: emptyList(),
                    subContents = prizeClaimSubList[index] ?: emptyList(),
                    color = LottoMateBlue50,
                )
            }
        }
    }
}