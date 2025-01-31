package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
internal fun BottomNoticeSection(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(LottoMateGray20)
            .padding(horizontal = Dimens.DefaultPadding20)
            .padding(top = 32.dp, bottom = 44.dp),
    ) {
        Column {
            LottoMateText(
                text = "주의해주세요.",
                style = LottoMateTheme.typography.headline2
                    .copy(color = LottoMateGray100),
            )

            LottoMateText(
                text = "• 19세 미만은 로또를 구매할 수 없어요.",
                style = LottoMateTheme.typography.label1
                    .copy(color = LottoMateGray100),
                modifier = Modifier.padding(top = 10.dp),
            )

            LottoMateText(
                text = "• 1인당 1회 10만원만 구매할 수 있어요.",
                style = LottoMateTheme.typography.label1
                    .copy(color = LottoMateGray100),
                modifier = Modifier.padding(top = 4.dp),
            )

            LottoMateText(
                text = "• 모든 복권은 종류에 상관 없이 현금으로만 구매할 수 있어요.",
                style = LottoMateTheme.typography.label1
                    .copy(color = LottoMateGray100),
                modifier = Modifier.padding(top = 4.dp),
            )

            LottoMateText(
                text = "• 동행복권 및 스포츠토토에서만 살 수 있어요.",
                style = LottoMateTheme.typography.label1
                    .copy(color = LottoMateGray100),
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}