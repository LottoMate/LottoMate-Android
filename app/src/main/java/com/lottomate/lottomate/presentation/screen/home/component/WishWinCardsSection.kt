package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
internal fun WishWinCardsSection(
    modifier: Modifier = Modifier,
    onClickMap: () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
    ) {
        LottoMateText(
            text = "로또 당첨을 꿈꾼다면?",
            style = LottoMateTheme.typography.headline1,
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
                    modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20, vertical = 16.dp),

                    ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_win_check),
                        contentDescription = null,
                    )

                    LottoMateText(
                        text = "내 로또는 과연 몇 등일까?",
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray100),
                        modifier = Modifier.padding(top = 8.dp),
                    )

                    LottoMateText(
                        text = "당첨 확인하기",
                        style = LottoMateTheme.typography.headline1,
                    )
                }
            }

            LottoMateCard(
                modifier = Modifier.weight(1f),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = Dimens.DefaultPadding20, vertical = 16.dp)
                        .noInteractionClickable { onClickMap() },

                    ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_win_map),
                        contentDescription = null,
                    )

                    LottoMateText(
                        text = "로또 사러 어디로 가지?",
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray100),
                        modifier = Modifier.padding(top = 8.dp),
                    )

                    LottoMateText(
                        text = "근처 명당 보기",
                        style = LottoMateTheme.typography.headline1,
                    )
                }
            }
        }
    }
}