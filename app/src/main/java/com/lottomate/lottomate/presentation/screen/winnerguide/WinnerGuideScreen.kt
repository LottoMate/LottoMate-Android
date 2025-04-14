package com.lottomate.lottomate.presentation.screen.winnerguide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.winnerguide.component.BottomWarningSection
import com.lottomate.lottomate.presentation.screen.winnerguide.component.MiddlePrizeClaimSection
import com.lottomate.lottomate.presentation.screen.winnerguide.component.TopNoticeSection
import com.lottomate.lottomate.presentation.screen.winnerguide.component.TopPrizeClaimLocation
import com.lottomate.lottomate.presentation.screen.winnerguide.model.WinnerGuideType

@Composable
fun WinnerGuideRoute(
    moveToMap: () -> Unit,
    moveToNaverMap: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    WinnerGuideScreen(
        onClickClaimAddress = { place ->
            if (place.isEmpty()) moveToMap()
            else moveToNaverMap(place)
        },
        onClickBanner = { moveToMap() },
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun WinnerGuideScreen(
    modifier: Modifier = Modifier,
    onClickClaimAddress: (String) -> Unit,
    onClickBanner: (BannerType) -> Unit,
    onBackPressed: () -> Unit,
) {
    var currentLottoType by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.BaseTopPadding))

            TopNoticeSection()

            TopLottoTypeToggleButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.DefaultPadding20)
                    .padding(top = 36.dp),
                currentIndex = currentLottoType,
                onClick = { currentLottoType = it },
            )

            TopPrizeClaimLocation(
                modifier = Modifier.padding(top = 24.dp),
                type = WinnerGuideType.findWinnerGuide(LottoType.findLottoType(currentLottoType)),
                onClickClaimAddress = onClickClaimAddress,
            )

            MiddlePrizeClaimSection(
                modifier = Modifier.padding(top = 48.dp),
                type = WinnerGuideType.findWinnerGuide(LottoType.findLottoType(currentLottoType)),
            )

            BottomWarningSection(
                modifier = Modifier.padding(top = 48.dp),
                type = WinnerGuideType.findWinnerGuide(LottoType.findLottoType(currentLottoType)),
            )

            BannerCard(
                modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20, vertical = 36.dp),
                type = BannerType.MAP,
                onClickBanner = { onClickBanner(BannerType.MAP) },
            )
        }

        LottoMateTopAppBar(
            titleRes = R.string.guide_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )
    }
}

@Composable
private fun TopLottoTypeToggleButtons(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    onClick: (Int) -> Unit,
) {
    val toggleButtons = stringArrayResource(id = R.array.lotto_info_tab_menu)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        toggleButtons.forEachIndexed { index, button ->
            ToggleButtonItem(
                item = button,
                isSelected = currentIndex == index,
                onClick = { onClick(index) }
            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
private fun ToggleButtonItem(
    item: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    if (isSelected) {
        LottoMateSolidButton(
            text = item,
            buttonSize = LottoMateButtonProperty.Size.SMALL,
            buttonShape = LottoMateButtonProperty.Shape.ROUND,
            onClick = onClick,
        )
    } else {
        LottoMateAssistiveButton(
            text = item,
            buttonSize = LottoMateButtonProperty.Size.SMALL,
            buttonShape = LottoMateButtonProperty.Shape.ROUND,
            onClick = onClick,
        )
    }
}