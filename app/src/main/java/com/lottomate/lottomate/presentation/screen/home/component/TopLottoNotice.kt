package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateAnnotatedText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.home.model.LottoNoticeType
import com.lottomate.lottomate.presentation.ui.LottoMateBlue5
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.utils.AnnotatedTextUtils.createAnnotatedText
import com.lottomate.lottomate.utils.DateUtils
import java.util.Calendar

@Composable
internal fun TopLottoNotice(
    modifier: Modifier = Modifier,
) {
    // 현재 보여질 noticeType의 index를 상태로 관리
    var currentIndex by remember { mutableIntStateOf(0) }

    // 일정 시간마다 index를 변경
    LaunchedEffect(key1 = currentIndex) {
        kotlinx.coroutines.delay(3000L) // 3초 대기
        currentIndex = (currentIndex + 1) % LottoNoticeType.entries.size
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(LottoMateBlue5, RoundedCornerShape(Dimens.RadiusLarge))
            .padding(Dimens.DefaultPadding20)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(
                    id = when (currentIndex) {
                        LottoNoticeType.LOTTO_645_NOTICE.ordinal, LottoNoticeType.LOTTO_645_COUNTDOWN.ordinal -> R.drawable.icon_lotto645_rank_first
                        else -> R.drawable.icon_lotto720_rank_first
                    }
                ),
                contentDescription = null,
            )

            val luckyDay645 = DateUtils.getDaysUntilNextDay(Calendar.SATURDAY)
            val luckyDay720 = DateUtils.getDaysUntilNextDay(Calendar.THURSDAY)
            val currentNoticeType = LottoNoticeType.findByIndex(currentIndex)

            val annotatedString = when (currentNoticeType) {
                LottoNoticeType.LOTTO_645_NOTICE,
                LottoNoticeType.LOTTO_720_NOTICE -> {
                    val keyword = stringResource(id = currentNoticeType.keyword)
                    val message = stringResource(id = currentNoticeType.message)

                    createAnnotatedText(message, keyword, SpanStyle(fontWeight = FontWeight.Bold))
                }
                LottoNoticeType.LOTTO_645_COUNTDOWN -> {
                    if (luckyDay645 == 0) {
                        val keyword = stringResource(id = currentNoticeType.keyword)
                        val message = stringResource(id = currentNoticeType.message)

                        createAnnotatedText(message, keyword, SpanStyle(color = LottoMateRed50, fontWeight = FontWeight.Bold))
                    } else {
                        // 남은 일수
                        val daysLeft = luckyDay645.toString().plus("일")
                        val message = pluralStringResource(id = R.plurals.home_top_notice_lotto645_2, count = luckyDay645, daysLeft)

                        createAnnotatedText(message, daysLeft, SpanStyle(color = LottoMateBlue50, fontWeight = FontWeight.Bold))
                    }
                }
                else -> {
                    if (luckyDay720 == 0) {
                        val keyword = stringResource(id = currentNoticeType.keyword)
                        val message = stringResource(id = currentNoticeType.message)

                        createAnnotatedText(message, keyword, SpanStyle(color = LottoMateRed50, fontWeight = FontWeight.Bold))
                    } else {
                        // 남은 일수
                        val daysLeft = luckyDay720.toString().plus("일")
                        val message = pluralStringResource(id = R.plurals.home_top_notice_lotto720_2, count = luckyDay720, daysLeft)

                        createAnnotatedText(message, daysLeft, SpanStyle(color = LottoMateBlue50, fontWeight = FontWeight.Bold))
                    }
                }
            }

            LottoMateAnnotatedText(
                annotatedString = annotatedString,
                style = LottoMateTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopNoticePreview() {
    LottoMateTheme {
        TopLottoNotice(
            modifier = Modifier.padding(20.dp),
        )
    }
}