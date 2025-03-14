package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.BottomSheetPeekHeight
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 지도에 보여지는 상/하단 버튼 컴포저블 함수
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapButtons(
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    currentCameraPosition: LatLng,
    bottomSheetState: BottomSheetState,
    cameraPositionState: CameraPositionState,
    onSizeBottomSheetHeight: (Int) -> Unit,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
    onClickRefresh: () -> Unit,
    onClickStoreList: () -> Unit,
    onClickLocationFocus: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TopFilterButtons(
            modifier = Modifier.fillMaxWidth(),
            lottoTypeState = lottoTypeState,
            winStoreState = winStoreState,
            favoriteStoreState = favoriteStoreState,
            onSizeBottomSheetHeight = onSizeBottomSheetHeight,
            onClickLottoType = onClickLottoType,
            onClickWinLottoStore = onClickWinLottoStore,
            onClickFavoriteStore = onClickFavoriteStore,
        )

        BottomButtons(
            modifier = Modifier.align(Alignment.BottomCenter),
            bottomSheetState = bottomSheetState,
            currentCameraPosition = currentCameraPosition,
            cameraPositionState = cameraPositionState,
            onClickRefresh = onClickRefresh,
            onClickStoreList = onClickStoreList,
            onClickLocationFocus = onClickLocationFocus,
        )
    }
}

@Composable
private fun TopFilterButtons(
    modifier: Modifier = Modifier,
    lottoTypeState: String,
    winStoreState: Boolean,
    favoriteStoreState: Boolean,
    onSizeBottomSheetHeight: (Int) -> Unit,
    onClickLottoType: () -> Unit,
    onClickWinLottoStore: () -> Unit,
    onClickFavoriteStore: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(top = Dimens.StatusBarHeight.plus(8.dp))
            .onSizeChanged { onSizeBottomSheetHeight(it.height) },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FilterButton(
            text = lottoTypeState,
            iconRes = R.drawable.icon_filter_12,
            iconDescription = stringResource(id = R.string.desc_filter_icon_map),
            isSelected = true,
            onClick = onClickLottoType,
        )

        Row {
            FilterButton(
                text = stringResource(id = R.string.map_win_store_filter_button),
                isSelected = winStoreState,
                onClick = onClickWinLottoStore,
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilterButton(
                text = stringResource(id = R.string.map_favorite_store_filter_button),
                isSelected = favoriteStoreState,
                onClick = onClickFavoriteStore,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    currentCameraPosition: LatLng,
    bottomSheetState: BottomSheetState,
    cameraPositionState: CameraPositionState,
    onClickRefresh: () -> Unit,
    onClickStoreList: () -> Unit,
    onClickLocationFocus: () -> Unit,
) {
    fun isApproximatelyEqual(coord1: LatLng, coord2: LatLng): Boolean {
        val roundedLat1 = BigDecimal(coord1.latitude).setScale(6, RoundingMode.HALF_UP).toDouble()
        val roundedLon1 = BigDecimal(coord1.longitude).setScale(6, RoundingMode.HALF_UP).toDouble()

        val roundedLat2 = BigDecimal(coord2.latitude).setScale(6, RoundingMode.HALF_UP).toDouble()
        val roundedLon2 = BigDecimal(coord2.longitude).setScale(6, RoundingMode.HALF_UP).toDouble()

        return roundedLat1 == roundedLat2 && roundedLon1 == roundedLon2
    }
    val isCurrentMoving = cameraPositionState.isMoving || isApproximatelyEqual(currentCameraPosition, cameraPositionState.position.target)

    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // BottomSheet의 높이를 실시간 관찰
    val bottomSheetOffset by remember {
        derivedStateOf {
            with(density) { bottomSheetState.requireOffset().toDp() }
        }
    }

    // BottomSheet의 높이에 맞춰 버튼 하단 padding값 설정
    val buttonBottomPadding by remember {
        derivedStateOf { screenHeight - bottomSheetOffset }
    }

    // BottomSheet의 높이가 화면의 60%이상을 차지하면 버튼 숨기기
    val shouldShowButtons by remember {
        derivedStateOf { bottomSheetOffset > screenHeight * 0.4f }
    }

    if (shouldShowButtons) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(bottom = buttonBottomPadding.plus(BottomSheetPeekHeight.dp))
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            IconButton(
                onClick = onClickLocationFocus,
                modifier = Modifier
                    .dropShadow(
                        shape = CircleShape,
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        blur = 8.dp,
                    )
                    .background(
                        color = LottoMateWhite,
                        shape = CircleShape,
                    )
                    .size(40.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_location),
                    contentDescription = stringResource(id = R.string.desc_refresh_icon_map),
                    tint = LottoMateGray100,
                )
            }

            Column {
                IconButton(
                    onClick = onClickStoreList,
                    modifier = Modifier
                        .dropShadow(
                            shape = CircleShape,
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            blur = 8.dp,
                        )
                        .background(
                            color = LottoMateWhite,
                            shape = CircleShape,
                        )
                        .size(40.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_list),
                        contentDescription = stringResource(id = R.string.desc_lotto_store_list_icon_map),
                        tint = LottoMateGray100,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                IconButton(
                    onClick = onClickRefresh,
                    modifier = Modifier
                        .dropShadow(
                            shape = CircleShape,
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            blur = if (isCurrentMoving) 16.dp else 8.dp,
                            color = if (isCurrentMoving) LottoMateBlue50.copy(
                                alpha = 0.8f
                            ) else LottoMateBlack.copy(alpha = 0.16f),
                        )
                        .background(
                            color = LottoMateWhite,
                            shape = CircleShape,
                        )
                        .size(40.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_refresh),
                        contentDescription = stringResource(id = R.string.desc_location_focus_icon_map),
                        tint = if (isCurrentMoving) LottoMateBlue50 else LottoMateGray100,
                    )
                }
            }
        }
    }
}