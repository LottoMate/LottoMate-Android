package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
internal fun WishWinCardsSection(
    modifier: Modifier = Modifier,
    onClickMap: () -> Unit,
    onClickScan: () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
    ) {
        LottoMateText(
            text = stringResource(id = R.string.home_wish_winner_section_title),
            style = LottoMateTheme.typography.headline1
                .copy(color = LottoMateBlack),
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            LottoMateCard(
                modifier = Modifier.weight(1f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClickMap() }
                        .padding(horizontal = Dimens.DefaultPadding20, vertical = 16.dp),
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_win_map),
                        contentDescription = null,
                    )

                    LottoMateText(
                        text = stringResource(id = R.string.home_wish_winner_map_title_sub),
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray100),
                        modifier = Modifier.padding(top = 8.dp),
                    )

                    LottoMateText(
                        text = stringResource(id = R.string.home_wish_winner_map_title),
                        style = LottoMateTheme.typography.headline2
                            .copy(color = LottoMateBlack),
                    )
                }
            }

            LottoMateCard(
                modifier = Modifier.weight(1f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClickScan() }
                        .padding(horizontal = Dimens.DefaultPadding20, vertical = 16.dp),

                    ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_win_check),
                        contentDescription = null,
                    )

                    LottoMateText(
                        text = stringResource(id = R.string.home_wish_winner_check_title_sub),
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray100),
                        modifier = Modifier.padding(top = 8.dp),
                    )

                    LottoMateText(
                        text = stringResource(id = R.string.home_wish_winner_check_title),
                        style = LottoMateTheme.typography.headline2
                            .copy(color = LottoMateBlack),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun WishWinCardsSectionPreview() {
    LottoMateTheme {
        WishWinCardsSection(
            onClickMap = {},
            onClickScan = {},
        )
    }
}