package com.lottomate.lottomate.presentation.screen.lottoinfo.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.res.StringArrays.Lotto645WinConditions
import com.lottomate.lottomate.presentation.res.StringArrays.Lotto720WinConditions
import com.lottomate.lottomate.presentation.res.StringArrays.Lotto720WinPrizes
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue40
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateGreen50
import com.lottomate.lottomate.presentation.ui.LottoMateRed30
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.presentation.ui.LottoMateYellow60
import com.lottomate.lottomate.utils.dropShadow

enum class LottoRank(val rank: Int) {
    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5), SIXTH(6), SEVENTH(7), BONUS(8);

    companion object {
        fun getLottoRankLabel(rank: Int): String {
            return when (val lottoRank = entries.find { it.rank == rank }) {
                BONUS -> "보너스"
                null -> "ERROR"
                else -> "${lottoRank.rank}등"
            }
        }
    }
}

@Composable
fun Lotto645WinInfoCard(
    modifier: Modifier = Modifier,
    rank: Int,
    lottoInfo: Lotto645Info,
) {
    LottoWinInfoBaseCard(
        modifier = modifier,
        rank = rank.plus(1),
        prize = "${lottoInfo.prizeMoney[rank]}원",
        condition = Lotto645WinConditions[rank],
        lottoType = LottoType.L645,
        winnerCountContent = {
            Spacer(modifier = Modifier.height(10.dp))

            LottoWinInfoRow(
                title = "당첨자 수",
                text = "${lottoInfo.drwtWinNum[rank]}명"
            )
        },
        totalPrizeContent = {
            Spacer(modifier = Modifier.height(10.dp))

            LottoWinInfoRow(
                title = "총 당첨금",
                text = "${lottoInfo.prizeMoney[rank]}원"
            )
        }
    )
}

@Composable
fun Lotto720WinInfoCard(
    modifier: Modifier = Modifier,
    rank: Int,
    lottoInfo: Lotto720Info,
) {
    LottoWinInfoBaseCard(
        modifier = modifier,
        rank = rank.plus(1),
        prize = Lotto720WinPrizes[rank],
        condition = Lotto720WinConditions[rank],
        lottoType = LottoType.L720,
        winnerCountContent = {
            Spacer(modifier = Modifier.height(10.dp))

            LottoWinInfoRow(
                title = "당첨 수",
                text = "${lottoInfo.drwtWinNum[rank]}매"
            )
        },
    )
}

@Composable
private fun LottoWinInfoBaseCard(
    modifier: Modifier = Modifier,
    rank: Int,
    prize: String,
    condition: String,
    lottoType: LottoType,
    winnerCountContent: @Composable (() -> Unit)? = null,
    totalPrizeContent: @Composable (() -> Unit)? = null,
) {
    val rankColor = when (rank) {
        LottoRank.FIRST.rank -> LottoMateRed50
        LottoRank.SECOND.rank -> LottoMateRed30
        LottoRank.THIRD.rank -> LottoMateYellow60
        LottoRank.FOURTH.rank -> LottoMateGreen50
        LottoRank.FIFTH.rank -> LottoMateBlue40
        else -> LottoMateGray100
    }

    Card(
        modifier = modifier.dropShadow(
            shape = RoundedCornerShape(Dimens.RadiusLarge),
            color = LottoMateBlack.copy(alpha = 0.16f),
            blur = 8.dp,
            offsetX = 0.dp,
            offsetY = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = LottoMateWhite,
        ),
        shape = RoundedCornerShape(Dimens.RadiusLarge),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (rank == LottoRank.FIRST.rank) {
                    Image(
                        painter = painterResource(
                            id = when (lottoType) {
                                LottoType.L645 -> R.drawable.icon_lotto645_rank_first
                                LottoType.L720 -> R.drawable.icon_lotto720_rank_first
                                else -> R.drawable.icon_speetto_rank_first
                            }
                        ),
                        contentDescription = "Lotto Rank First Icon"
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Text(
                    text = LottoRank.getLottoRankLabel(rank),
                    style = LottoMateTheme.typography.headline2
                        .copy(color = rankColor),
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = prize,
                    style = LottoMateTheme.typography.title3,
                )

                if (lottoType == LottoType.L645) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = R.string.lotto_info_per_person),
                        style = LottoMateTheme.typography.label2
                            .copy(color = LottoMateGray80)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LottoMateGray10,
                        shape = RoundedCornerShape(Dimens.RadiusLarge)
                    )
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                LottoWinInfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "당첨 조건",
                    text = condition
                )

                if (winnerCountContent != null) {
                    winnerCountContent()
                }
                if (totalPrizeContent != null) {
                    totalPrizeContent()
                }
            }
        }
    }
}

@Composable
private fun LottoWinInfoRow(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = LottoMateTheme.typography.body1
                .copy(LottoMateGray90),
            modifier = Modifier.width(88.dp)
        )
        Text(
            text = text,
            style = LottoMateTheme.typography.headline2,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoWinnerInfoCardPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LottoWinInfoBaseCard(
                modifier = Modifier.fillMaxWidth(),
                rank = 1,
                lottoType = LottoType.L645,
                prize = "48,077,032원",
                condition = "당첨번호 6개 일치\n+ 보너스 일치",
                winnerCountContent = {
                    Spacer(modifier = Modifier.height(10.dp))

                    LottoWinInfoRow(
                        title = "당첨자 수",
                        text = "91명"
                    )
                },
                totalPrizeContent = {
                    Spacer(modifier = Modifier.height(10.dp))

                    LottoWinInfoRow(
                        title = "총 당첨금",
                        text = "48,077,032원"
                    )
                }
            )
        }


    }
}