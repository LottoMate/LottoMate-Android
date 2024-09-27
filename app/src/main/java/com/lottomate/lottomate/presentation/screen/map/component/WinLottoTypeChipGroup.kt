package com.lottomate.lottomate.presentation.screen.map.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun WinLottoTypeChipGroup(
    modifier: Modifier = Modifier,
    store: StoreInfo,
) {
    Row(modifier = modifier) {
        if (store.hasWinLotto645) {
            WinLottoTypeChip(
                icon = R.drawable.icon_lotto645_rank_first,
                winLottoType = LottoTypeFilter.Lotto645.kr,
                winCount = store.getCountLotto645()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (store.hasWinLotto720) {
            WinLottoTypeChip(
                icon = R.drawable.icon_lotto720_rank_first,
                winLottoType = LottoTypeFilter.Lotto720.kr,
                winCount = store.getCountLotto720(),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (store.hasWinSpeetto) {
            WinLottoTypeChip(
                icon = R.drawable.icon_speetto_rank_first,
                winLottoType = LottoTypeFilter.Speetto.kr,
                winCount = store.getCountSpeetto(),
            )
        }
    }
}

@Composable
private fun WinLottoTypeChip(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    winLottoType: String,
    winCount: Int,
) {
    Box(modifier = modifier.background(LottoMateGray20, RoundedCornerShape(Dimens.RadiusSmall))) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "The Chip Icon for LottoType on the Map",
                modifier = Modifier.size(12.dp),
            )

            Spacer(modifier = Modifier.width(2.dp))

            LottoMateText(
                text = winLottoType,
                style = LottoMateTheme.typography.caption1,
            )

            Spacer(modifier = Modifier.width(2.dp))

            LottoMateText(
                text = "${winCount}회",
                style = LottoMateTheme.typography.caption1
                    .copy(color = LottoMateRed50),
            )
        }
    }
}