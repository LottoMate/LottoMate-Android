package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateDim1
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LottoTypeInfoBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        modifier = modifier,
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = LottoMateWhite,
        scrimColor = LottoMateDim1,
        dragHandle = null,
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        LottoTypeInfoBottomSheetContent(
            onClickClose = onDismiss,
        )
    }
}
@Composable
fun LottoTypeInfoBottomSheetContent(
    modifier: Modifier = Modifier,
    onClickClose: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.DefaultPadding20)
            .padding(top = 32.dp, bottom = 28.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        LottoMateText(
            text = "복권 종류",
            style = LottoMateTheme.typography.headline1,
        )

        LottoMateText(
            text = "동행복권에서 판매하는 복권 중 가장 인기있는 복권을 소개해요.",
            style = LottoMateTheme.typography.label1,
            modifier = Modifier.padding(top = 4.dp),
        )

        Column {
            Row(
                modifier = Modifier.padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_lotto_type_info_645),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    LottoMateText(
                        text = "로또 6/45",
                        style = LottoMateTheme.typography.headline2,
                    )
                    LottoMateText(
                        text = "• 1개 당 1,000원",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                    LottoMateText(
                        text = "• 1 - 45개 숫자 중 6개 선택",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    LottoMateText(
                        text = "• 추첨 번호와 내 번호가 일치 시 당첨",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    LottoMateText(
                        text = "• 매주 토요일 오후 8시 45분에 추첨",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            Divider(
                color = LottoMateGray20,
                modifier = Modifier.padding(top = 16.dp),
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_lotto_type_info_720),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    LottoMateText(
                        text = "로또 720+",
                        style = LottoMateTheme.typography.headline2,
                    )
                    LottoMateText(
                        text = "• 1개 당 1,000원",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                    LottoMateText(
                        text = "• 1 - 5조 선택 / 숫자 6개 선택",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    LottoMateText(
                        text = "• 추첨 번호와 내 번호가 일치 시 당첨",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    LottoMateText(
                        text = "• 매주 목요일 오후 7시 5분 쯤 추첨",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            Divider(
                color = LottoMateGray20,
                modifier = Modifier.padding(top = 16.dp),
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.img_home_lotto_type_info_speetto),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    LottoMateText(
                        text = "스피또2000 / 1000 / 500",
                        style = LottoMateTheme.typography.headline2,
                    )
                    LottoMateText(
                        text = "• 각 2000원, 1000원, 500원",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                    LottoMateText(
                        text = "• 구매 후 긁어서 나온 모양 일치 시 당첨",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    LottoMateText(
                        text = "• 즉석에서 결과 확인 가능",
                        style = LottoMateTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }

        LottoMateSolidButton(
            text = "확인",
            buttonSize = LottoMateButtonProperty.Size.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            onClick = { onClickClose() },
        )
    }
}