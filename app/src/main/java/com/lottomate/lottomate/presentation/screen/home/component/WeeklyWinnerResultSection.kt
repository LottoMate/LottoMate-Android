package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateScrollableTabRow
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.rememberTabState
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall720
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
internal fun WeeklyWinnerResultSection(
    modifier: Modifier = Modifier,
) {
    val tabRowState = rememberTabState()
    val tabs = listOf("로또", "연금복권", "스피또")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LottoMateWhite),
    ) {
        LottoMateText(
            text = "이번 주 당첨 결과",
            style = LottoMateTheme.typography.headline1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.DefaultPadding20),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
        ) {
            LottoMateScrollableTabRow(
                tabState = tabRowState,
                tabs = tabs,
                unselectedTabTextStyle = LottoMateTheme.typography.headline2,
                unselectedTabTextColor = LottoMateGray60,
                modifier = Modifier.fillMaxWidth(),
            )

            Icon(
                painter = painterResource(id = R.drawable.icon_infomation),
                contentDescription = null,
                tint = LottoMateGray60,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Dimens.DefaultPadding20)
                    .size(22.dp)
                    .noInteractionClickable { },
            )
        }

        when (tabRowState.currentTabIndex) {
            0 -> {
                // 로또 당첨 결과
                Lotto645WeeklyWinnerResult()
            }
            1 -> {
                // 연금복권 당첨 결과
                Lotto720WeeklyWinnerResult()
            }
            2 -> {
                // 스피또 당첨 결과
                SpeettoWeeklyWinnerResult()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(horizontal = Dimens.DefaultPadding20)
                .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                .padding(vertical = 16.dp, horizontal = Dimens.DefaultPadding20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "💸 2등은 당첨금이 얼마일까?",
                    style = LottoMateTheme.typography.label1,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LottoMateText(
                        text = "당첨 정보 보기",
                        style = LottoMateTheme.typography.caption1
                            .copy(color = LottoMateGray100),
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null,
                        tint = LottoMateGray100,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Lotto645WeeklyWinnerResult(
    modifier: Modifier = Modifier,
) {
    val winNumbers = listOf(4, 5, 11, 21, 37, 40)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(horizontal = Dimens.DefaultPadding20),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = null,
            tint = LottoMateGray100,
        )

        Spacer(modifier = Modifier.width(3.dp))

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "1126회 1등 당첨금",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray120),
                )

                LottoMateText(
                    text = "2024.06.29 추첨",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            LottoMateText(
                text = "총 269억원",
                style = LottoMateTheme.typography.title1,
                modifier = Modifier.padding(top = 12.dp),
            )

            LottoMateText(
                text = "당첨된 11명은 한 번에 23억을 받아요",
                style = LottoMateTheme.typography.label1,
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                winNumbers.forEach { number ->
                    LottoBall645(number = number)
                }

                Icon(
                    painter = painterResource(id = R.drawable.icon_mini_plus),
                    tint = LottoMateGray100,
                    contentDescription = "Just Separator",
                )

                LottoBall645(number = 43)
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "다음 회차 1등 예상 당첨금 약 36억",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray100),
                )

                LottoMateText(
                    text = "2024.00.00 00:00:00 기준",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray70),
                )
            }
        }

        Spacer(modifier = Modifier.width(3.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = null,
            tint = LottoMateGray100,
        )
    }
}

@Composable
private fun Lotto720WeeklyWinnerResult(
    modifier: Modifier = Modifier,
) {
    val winNumbers = listOf(5, 4, 5, 11, 21, 37, 40)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(horizontal = Dimens.DefaultPadding20),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = null,
            tint = LottoMateGray100,
        )

        Spacer(modifier = Modifier.width(3.dp))

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "1126회 1등 당첨금",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray120),
                )

                LottoMateText(
                    text = "2024.06.29 추첨",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            LottoMateText(
                text = "20년 x 월 700만원",
                style = LottoMateTheme.typography.title1,
                modifier = Modifier.padding(top = 12.dp),
            )

            LottoMateText(
                text = "당첨자는 20년 동안 매월 700만원 씩 받아요",
                style = LottoMateTheme.typography.label1,
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoBall720(
                    index = 0,
                    number = winNumbers.first(),
                    isBonusNumber = false,
                )

                Spacer(modifier = Modifier.width(7.dp))

                LottoMateText(
                    text = "조",
                    style = LottoMateTheme.typography.label2,
                )

                (1..winNumbers.lastIndex).forEach { index ->
                    LottoBall720(
                        index = index,
                        number = winNumbers[index],
                        isBonusNumber = false,
                        modifier = Modifier.padding(start = 7.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(3.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = null,
            tint = LottoMateGray100,
        )
    }
}

@Composable
private fun SpeettoWeeklyWinnerResult(
    modifier: Modifier = Modifier,
) {
    val winNumbers = listOf(5, 4, 5, 11, 21, 37, 40)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(horizontal = Dimens.DefaultPadding20),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = null,
            tint = LottoMateGray100,
        )

        Spacer(modifier = Modifier.width(3.dp))

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                    .padding(vertical = 8.dp, horizontal = Dimens.DefaultPadding20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LottoMateText(
                    text = "54회 1등 당첨금",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray120),
                )

                LottoMateText(
                    text = "2024.06.09 스피또 2000 기준",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80),
                )
            }

            LottoMateText(
                text = "10억원",
                style = LottoMateTheme.typography.title1,
                modifier = Modifier.padding(top = 12.dp),
            )

            LottoMateText(
                text = "1등 복권이 6장 남아있어요.",
                style = LottoMateTheme.typography.label1,
                modifier = Modifier.padding(top = 4.dp),
            )
            LottoMateText(
                text = "현재까지 출고율 72%",
                style = LottoMateTheme.typography.caption1
                    .copy(color = LottoMateGray100),
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "1등 : 0/6",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80),
                )

                LottoMateText(
                    text = "|",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray40),
                    modifier = Modifier.padding(start = 8.dp),
                )

                LottoMateText(
                    text = "2등 : 11/18",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(start = 8.dp),
                )

                LottoMateText(
                    text = "|",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray40),
                    modifier = Modifier.padding(start = 8.dp),
                )

                LottoMateText(
                    text = "3등 : 102/150",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

        }

        Spacer(modifier = Modifier.width(3.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = null,
            tint = LottoMateGray100,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyWinnerResultSectionPreview() {
    LottoMateTheme {
        WeeklyWinnerResultSection()
    }
}