package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.ui.LottoMateDim1
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MapInitPopupBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onClickRequestOpen: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
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
        MapInitPopupBottomSheetContent(
            onClickClose = {
                coroutineScope.launch { 
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    onDismiss()
                }
            },
            onClickRequestOpen = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    onClickRequestOpen()
                }
            }
        )
    }
}

@Composable
private fun MapInitPopupBottomSheetContent(
    modifier: Modifier = Modifier,
    onClickClose: () -> Unit,
    onClickRequestOpen: () -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 28.dp, horizontal = 20.dp),
    ) {
        LottoMateText(
            text = "오픈 안내",
            style = LottoMateTheme.typography.headline1,
        )

        LottoMateText(
            text = "현재는 서울 지역의 로또 매장 정보만 확인할 수 있어요\n다른 지역은 준비 중이니 조금만 기다려주세요",
            style = LottoMateTheme.typography.body2,
            modifier = Modifier.padding(top = 4.dp),
        )

        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.img_map_init_popup),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 24.dp)
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
        )

        LottoMateSolidButton(
            text = "확인",
            buttonSize = LottoMateButtonProperty.Size.LARGE,
            onClick = onClickClose,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .noInteractionClickable { onClickRequestOpen() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LottoMateText(
                text = "지도 오픈 요청하기",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray100),
            )

            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = LottoMateGray100,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(14.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapInitPopupBottomSheetContentPreview() {
    LottoMateTheme {
        MapInitPopupBottomSheetContent(
            onClickClose = {},
            onClickRequestOpen = {},
        )
    }
}