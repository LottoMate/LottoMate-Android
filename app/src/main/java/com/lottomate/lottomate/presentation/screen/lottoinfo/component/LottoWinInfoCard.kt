package com.lottomate.lottomate.presentation.screen.lottoinfo.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.res.StringArrays.Lotto645WinConditions
import com.lottomate.lottomate.presentation.res.StringArrays.Lotto720WinConditions
import com.lottomate.lottomate.presentation.res.StringArrays.Lotto720WinPrizes
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow

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
        winnerCountContent = {
            Spacer(modifier = Modifier.height(10.dp))

            LottoWinInfoRow(
                title = "당첨 수",
                text = "${lottoInfo.drwtWinNum[rank]}매"
            )
        },
        isBonus = rank == 7
    )
}

@Composable
private fun LottoWinInfoBaseCard(
    modifier: Modifier = Modifier,
    rank: Int,
    prize: String,
    condition: String,
    isBonus: Boolean = false,
    winnerCountContent: @Composable (() -> Unit)? = null,
    totalPrizeContent: @Composable (() -> Unit)? = null,
) {
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
            Text(
                text = if (isBonus) "보너스" else "${rank}등",
                style = MaterialTheme.typography.headlineMedium,
                color = LottoMateGray100,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = prize,
                style = MaterialTheme.typography.titleSmall,
            )

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
            style = MaterialTheme.typography.headlineMedium,
            color = LottoMateGray90,
            modifier = Modifier.width(88.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
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
                rank = 2,
                prize = "48,077,032원",
                condition = "당첨번호 6개 일치\n+ 보너스 일치",
                isBonus = false,
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