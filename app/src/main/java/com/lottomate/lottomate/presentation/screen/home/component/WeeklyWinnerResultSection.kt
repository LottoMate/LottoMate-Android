package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateScrollableTabRow
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.rememberTabState
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.home.model.HomeLotto645Info
import com.lottomate.lottomate.presentation.screen.home.model.HomeLotto720Info
import com.lottomate.lottomate.presentation.screen.home.model.HomeLottoInfo
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
    lottoInfos: Map<Int, HomeLottoInfo>,
    latestLotto645Round: Int,
    latestLotto720Round: Int,
    openLottoTypeInfoBottomSheet: () -> Unit,
    onClickPrevLottoInfo: (LottoType, Int) -> Unit,
    onClickNextLottoInfo: (LottoType, Int) -> Unit,
    onClickLottoInfo: (LottoType, Int) -> Unit,
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
                    .clickable { openLottoTypeInfoBottomSheet() },
            )
        }

        when (tabRowState.currentTabIndex) {
            0 -> {
                val lottoInfo = lottoInfos[LottoType.L645.num] as HomeLotto645Info

                // 로또 당첨 결과
                Lotto645WeeklyWinnerResult(
                    latestLottoRound = latestLotto645Round,
                    lottoInfo = lottoInfo,
                    onClickPrevLottoInfo = { onClickPrevLottoInfo(LottoType.L645, it) },
                    onClickNextLottoInfo = { onClickNextLottoInfo(LottoType.L645, it) },
                    onClickLottoInfo = { onClickLottoInfo(LottoType.L645, lottoInfo.round) },
                )
            }
            1 -> {
                val lottoInfo = lottoInfos[LottoType.L720.num] as HomeLotto720Info

                // 연금복권 당첨 결과
                Lotto720WeeklyWinnerResult(
                    latestLottoRound = latestLotto720Round,
                    lottoInfo = lottoInfos[LottoType.L720.num] as HomeLotto720Info,
                    onClickPrevLottoInfo = { onClickPrevLottoInfo(LottoType.L720, it) },
                    onClickNextLottoInfo = { onClickNextLottoInfo(LottoType.L720, it) },
                    onClickLottoInfo = { onClickLottoInfo(LottoType.L720, lottoInfo.round) },
                )
            }
            2 -> {
                // 스피또 당첨 결과
                SpeettoWeeklyWinnerResult(
                    onClickLottoInfo = {
                        // TODO : 스피또 선택 시, 해당 회차로 이동
                    },
                )
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
    latestLottoRound: Int,
    lottoInfo: HomeLotto645Info,
    onClickPrevLottoInfo: (Int) -> Unit,
    onClickNextLottoInfo: (Int) -> Unit,
    onClickLottoInfo: () -> Unit,
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
        val isLatestRound = lottoInfo.round == latestLottoRound

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = null,
            tint = LottoMateGray100,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClickPrevLottoInfo(lottoInfo.round.minus(1)) }
        )

        Spacer(modifier = Modifier.width(3.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .noInteractionClickable { onClickLottoInfo() },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "${lottoInfo.round}회 1등 당첨금",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray120),
                )

                LottoMateText(
                    text = "${lottoInfo.date} 추첨",
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            LottoMateText(
                text = "총 ${lottoInfo.winnerPrice.replace(",", "").toLong()/100_000_000}억원",
                style = LottoMateTheme.typography.title1,
                modifier = Modifier.padding(top = 12.dp),
            )

            LottoMateText(
                text = "당첨된 ${lottoInfo.winnerCount}명은 한 번에 ${lottoInfo.winnerPrice.replace(",", "").toLong().div(lottoInfo.winnerCount.toInt())/100_000_000}억을 받아요",
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
                lottoInfo.winnerNumbers.forEach { number ->
                    LottoBall645(number = number)
                }

                Icon(
                    painter = painterResource(id = R.drawable.icon_mini_plus),
                    tint = LottoMateGray100,
                    contentDescription = "Just Separator",
                )

                LottoBall645(number = lottoInfo.winnerBonusNumber)
            }
        }

        Spacer(modifier = Modifier.width(3.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = null,
            tint = if (isLatestRound) LottoMateWhite else LottoMateGray100,
            modifier = Modifier
                .noInteractionClickable { if (!isLatestRound) onClickNextLottoInfo(lottoInfo.round.plus(1)) }
        )
    }
}

@Composable
private fun Lotto720WeeklyWinnerResult(
    modifier: Modifier = Modifier,
    lottoInfo: HomeLotto720Info,
    latestLottoRound: Int,
    onClickPrevLottoInfo: (Int) -> Unit,
    onClickNextLottoInfo: (Int) -> Unit,
    onClickLottoInfo: () -> Unit,
) {
    val isLatestRound = lottoInfo.round == latestLottoRound

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
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClickPrevLottoInfo(lottoInfo.round.minus(1)) },
        )

        Spacer(modifier = Modifier.width(3.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .noInteractionClickable { onClickLottoInfo() },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "${lottoInfo.round}회 1등 당첨금",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray120),
                )

                LottoMateText(
                    text = "${lottoInfo.date} 추첨",
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
                    number = lottoInfo.winnerNumbers.first().toInt(),
                    isBonusNumber = false,
                )

                Spacer(modifier = Modifier.width(7.dp))

                LottoMateText(
                    text = "조",
                    style = LottoMateTheme.typography.label2,
                )

                (1..lottoInfo.winnerNumbers.lastIndex).forEach { index ->
                    LottoBall720(
                        index = index,
                        number = lottoInfo.winnerNumbers[index],
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
            tint = if (isLatestRound) LottoMateWhite else LottoMateGray100,
            modifier = Modifier
                .noInteractionClickable { if (!isLatestRound) onClickPrevLottoInfo(lottoInfo.round.plus(1)) },
        )
    }
}

@Composable
private fun SpeettoWeeklyWinnerResult(
    modifier: Modifier = Modifier,
    onClickLottoInfo: () -> Unit,
) {
    val winNumbers = listOf(5, 4, 5, 11, 21, 37, 40)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(horizontal = Dimens.DefaultPadding20)
            .clickable { onClickLottoInfo() },
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