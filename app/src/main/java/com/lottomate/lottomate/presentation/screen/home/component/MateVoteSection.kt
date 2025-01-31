package com.lottomate.lottomate.presentation.screen.home.component

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Preview(showBackground = true)
@Composable
internal fun MateVoteSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
    ) {
        LottoMateText(
            text = "메이트 투표",
            style = LottoMateTheme.typography.body1
                .copy(color = LottoMateGray100),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
        ) {
            LottoMateText(
                text = "Q.",
                style = LottoMateTheme.typography.title3,
            )
            LottoMateText(
                text = "나는 로또 당첨이 되면\n다니던 직장을 그만둔다.",
                style = LottoMateTheme.typography.title3,
                modifier = Modifier.weight(1f),
            )

            Icon(
                painter = painterResource(id = R.drawable.icon_sync),
                contentDescription = null,
                tint = LottoMateGray100,
            )
        }

        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_slo_cony),
            contentDescription = null,
            modifier = Modifier
                .padding(top = Dimens.DefaultPadding20)
                .align(Alignment.CenterHorizontally)
        )

        LottoMateCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            LottoMateText(
                text = "당장 그만두고 실컷 논다.",
                style = LottoMateTheme.typography.headline2,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = Dimens.DefaultPadding20)
            )
        }

        LottoMateCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            LottoMateText(
                text = "그래도 직장은 계속 다닌다.",
                style = LottoMateTheme.typography.headline2,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = Dimens.DefaultPadding20)
            )
        }
        
        LottoMateSolidButton(
            text = "답변을 선택해주세요",
            buttonSize = LottoMateButtonProperty.Size.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            onClick = {

            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            LottoMateText(
                text = "투표하고 싶은 질문이 있다면?",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray100),
            )

            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = LottoMateGray100,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(14.dp)
            )
        }
    }
}

