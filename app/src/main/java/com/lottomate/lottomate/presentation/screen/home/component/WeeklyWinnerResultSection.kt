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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateAnnotatedText
import com.lottomate.lottomate.presentation.component.LottoMateScrollableTabRow
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.rememberTabState
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.home.model.HomeLotto645Info
import com.lottomate.lottomate.presentation.screen.home.model.HomeLotto720Info
import com.lottomate.lottomate.presentation.screen.home.model.HomeLottoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall720
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
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
            style = LottoMateTheme.typography.headline1
                .copy(color = LottoMateBlack),
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
                    .noInteractionClickable { openLottoTypeInfoBottomSheet() },
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
                    onClickLottoInfo = { round -> onClickLottoInfo(LottoType.L645, round) },
                )
            }
            1 -> {
                val lottoInfo = lottoInfos[LottoType.L720.num] as HomeLotto720Info

                // 연금복권 당첨 결과
                Lotto720WeeklyWinnerResult(
                    latestLottoRound = latestLotto720Round,
                    lottoInfo = lottoInfo,
                    onClickPrevLottoInfo = { onClickPrevLottoInfo(LottoType.L720, it) },
                    onClickNextLottoInfo = { onClickNextLottoInfo(LottoType.L720, it) },
                    onClickLottoInfo = { round -> onClickLottoInfo(LottoType.L720, round) },
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
                        style = LottoMateTheme.typography.caption
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
    onClickLottoInfo: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(horizontal = Dimens.DefaultPadding20),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val isLatestRound = lottoInfo.round == latestLottoRound
        val hasPrevRound = lottoInfo.round - 1 > latestLottoRound - 3

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = null,
            tint = if (hasPrevRound) LottoMateGray100 else LottoMateWhite,
            modifier = Modifier
                .padding(top = 111.dp)
                .noInteractionClickable { if (hasPrevRound) onClickPrevLottoInfo(lottoInfo.round.minus(1)) }
        )

        Spacer(modifier = Modifier.width(13.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .noInteractionClickable { onClickLottoInfo(lottoInfo.round) },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "${lottoInfo.round}회 1등 당첨금",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray120),
                )

                LottoMateText(
                    text = "${lottoInfo.date} 추첨",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                )
            }

            LottoMateText(
                text = "총 ${lottoInfo.winnerPrice.replace(",", "").toLong()/100_000_000}억원",
                style = LottoMateTheme.typography.title1
                    .copy(color = LottoMateBlack),
                modifier = Modifier.padding(top = 12.dp),
            )

            LottoMateText(
                text = "당첨된 ${lottoInfo.winnerCount}명은 한 번에 ${lottoInfo.winnerPrice.replace(",", "").toLong().div(lottoInfo.winnerCount.toInt())/100_000_000}억을 받아요",
                style = LottoMateTheme.typography.label1
                    .copy(color = LottoMateBlack),
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                lottoInfo.winnerNumbers.forEach { number ->
                    LottoBall645(
                        number = number,
                        size = 28.dp,
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.icon_plus_10),
                    tint = LottoMateGray100,
                    contentDescription = "Just Separator",
                )

                LottoBall645(
                    number = lottoInfo.winnerBonusNumber,
                    size = 28.dp,
                )
            }
        }

        Spacer(modifier = Modifier.width(13.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = null,
            tint = if (isLatestRound) LottoMateWhite else LottoMateGray100,
            modifier = Modifier
                .padding(top = 111.dp)
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
    onClickLottoInfo: (Int) -> Unit,
) {
    val isLatestRound = lottoInfo.round == latestLottoRound
    val hasPrevRound = lottoInfo.round - 1 > latestLottoRound - 3

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(horizontal = Dimens.DefaultPadding20),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = null,
            tint = if (hasPrevRound) LottoMateGray100 else LottoMateWhite,
            modifier = Modifier
                .padding(top = 68.dp)
                .noInteractionClickable { if (hasPrevRound) onClickPrevLottoInfo(lottoInfo.round.minus(1)) },
        )

        Spacer(modifier = Modifier.width(13.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .noInteractionClickable { onClickLottoInfo(lottoInfo.round) },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .background(LottoMateGray10, RoundedCornerShape(Dimens.RadiusSmall))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "${lottoInfo.round}회 1등 당첨금",
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray120),
                )

                LottoMateText(
                    text = "${lottoInfo.date} 추첨",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                )
            }

            LottoMateText(
                text = "20년 x 월 700만원",
                style = LottoMateTheme.typography.title2,
                modifier = Modifier.padding(top = 12.dp),
            )

            val message = pluralStringResource(id = R.plurals.home_720_prize_per_person, 1)
            val keyword1 = stringResource(id = R.string.home_720_prize_per_person_keyword1)
            val keyword2 = stringResource(id = R.string.home_720_prize_per_person_keyword2)
            val years = message.indexOf(keyword1)
            val prizeOfMonth = message.indexOf(keyword2)

            val annotatedMessage = AnnotatedString.Builder(message).apply {
                addStyle(SpanStyle(fontWeight = FontWeight.SemiBold), years, years + (keyword1.length))
                addStyle(SpanStyle(fontWeight = FontWeight.SemiBold), prizeOfMonth, prizeOfMonth + (keyword2.length))
            }.toAnnotatedString()

            LottoMateAnnotatedText(
                annotatedString = annotatedMessage,
                style = LottoMateTheme.typography.label1
                    .copy(color = LottoMateBlack),
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoBall720(
                    index = 0,
                    number = lottoInfo.winnerNumbers.first().toInt(),
                    isBonusNumber = false,
                    size = 28.dp,
                )

                LottoMateText(
                    text = "조",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateBlack),
                )

                (1..lottoInfo.winnerNumbers.lastIndex).forEach { index ->
                    LottoBall720(
                        index = index,
                        number = lottoInfo.winnerNumbers[index],
                        isBonusNumber = false,
                        size = 28.dp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(13.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = null,
            tint = if (isLatestRound) LottoMateWhite else LottoMateGray100,
            modifier = Modifier
                .padding(top = 68.dp)
                .noInteractionClickable { if (!isLatestRound) onClickNextLottoInfo(lottoInfo.round.plus(1)) },
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
                    style = LottoMateTheme.typography.caption
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
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray100),
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "1등 : 0/6",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                )

                LottoMateText(
                    text = "|",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray40),
                    modifier = Modifier.padding(start = 8.dp),
                )

                LottoMateText(
                    text = "2등 : 11/18",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.padding(start = 8.dp),
                )

                LottoMateText(
                    text = "|",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray40),
                    modifier = Modifier.padding(start = 8.dp),
                )

                LottoMateText(
                    text = "3등 : 102/150",
                    style = LottoMateTheme.typography.caption
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