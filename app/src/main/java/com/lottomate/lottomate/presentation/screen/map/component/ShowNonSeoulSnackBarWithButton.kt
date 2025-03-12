package com.lottomate.lottomate.presentation.screen.map.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarWithButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.BottomSheetPeekHeight
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow

/**
 * 서울을 벗어났을 때 보여지는 상단 스낵바 & 하단 둘러보기 버튼
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowNonSeoulSnackBarWithButton(
    topSnackBarPadding: Dp = 0.dp,
    bottomSheetState: BottomSheetState,
    onClickRequestOpen: () -> Unit,
    onClickButton: () -> Unit = {},
) {
    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp


    // BottomSheet의 높이 측정
    val bottomSheetOffset by remember {
        mutableStateOf(with(density) { bottomSheetState.requireOffset().toDp() })
    }

    // BottomSheet의 높이에 맞춰 버튼 하단 padding값 설정
    val buttonBottomPadding by remember {
        Log.d("MapScreen", "NonSeoulSnackBarWithButton: ${screenHeight - bottomSheetOffset}")
        mutableStateOf(screenHeight - bottomSheetOffset)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LottoMateSnackBarWithButton(
            message = "조건에 맞는 지점이 없어요",
            buttonText = "오픈 요청하기",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = Dimens.StatusBarHeight
                        .plus(30.dp)
                        .plus(topSnackBarPadding)
                ),
            onClick = {
                // TODO : 지도 오픈 요청 페이지 이동
                onClickRequestOpen()
            }
        )

        // BottomSheet가 확장되지 않은 상태에서만 보이도록 설정
        if (bottomSheetState.isCollapsed) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = buttonBottomPadding.plus(BottomSheetPeekHeight.dp))
                    .dropShadow(
                        shape = RoundedCornerShape(30.dp),
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        blur = 8.dp,
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .clickable { onClickButton() }
                    .background(LottoMateWhite, RoundedCornerShape(30.dp))
                    .padding(horizontal = Dimens.RadiusLarge, vertical = Dimens.RadiusSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = "로또 지도 둘러보기",
                    style = LottoMateTheme.typography.label2,
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(12.dp),
                )
            }
        }
    }
}