package com.lottomate.lottomate.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.home.component.BottomNoticeSection
import com.lottomate.lottomate.presentation.screen.home.component.LottoTypeInfoBottomSheet
import com.lottomate.lottomate.presentation.screen.home.component.TopLottoNotice
import com.lottomate.lottomate.presentation.screen.home.component.WeeklyWinnerResultSection
import com.lottomate.lottomate.presentation.screen.home.component.WinInterviewCardsSection
import com.lottomate.lottomate.presentation.screen.home.component.WishWinCardsSection
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    vm: HomeViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLottoInfo: (LottoType, Int) -> Unit,
    moveToSetting: () -> Unit,
    moveToMap: () -> Unit,
    moveToInterviewDetail: (Int, String) -> Unit,
    moveToScan: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest { error ->
            onShowErrorSnackBar(error)
        }
    }

    val latestLotto645Round by vm.latestLotto645Round
    val latestLotto720Round by vm.latestLotto720Round

    HomeScreen(
        modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
        uiState = uiState,
        latestLotto645Round = latestLotto645Round,
        latestLotto720Round = latestLotto720Round,
        onClickPrevLottoInfo = { type, round -> vm.getLottoInfo(type, round) },
        onClickNextLottoInfo = { type, round -> vm.getLottoInfo(type, round) },
        onClickInterview = moveToInterviewDetail,
        moveToSetting = moveToSetting,
        moveToMap = moveToMap,
        moveToScan = moveToScan,
        moveToLottoInfo = { type, round -> moveToLottoInfo(type, round) },
        onClickBanner = onClickBanner,
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    latestLotto645Round: Int,
    latestLotto720Round: Int,
    onClickPrevLottoInfo: (LottoType, Int) -> Unit,
    onClickNextLottoInfo: (LottoType, Int) -> Unit,
    onClickInterview: (Int, String) -> Unit,
    moveToLottoInfo: (LottoType, Int) -> Unit,
    moveToMap: () -> Unit,
    moveToScan: () -> Unit,
    moveToSetting: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
) {
    var showLottoInfoBottomSheet by remember { mutableStateOf(false) }

    if (showLottoInfoBottomSheet) {
        LottoTypeInfoBottomSheet(
            onDismiss = { showLottoInfoBottomSheet = false }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.BaseTopPadding))

            TopLottoNotice(
                modifier = Modifier
                    .padding(top = Dimens.DefaultPadding20)
                    .padding(horizontal = Dimens.DefaultPadding20),
            )

            Spacer(modifier = Modifier.height(36.dp))

            when (uiState) {
                HomeUiState.Loading -> {

                }

                is HomeUiState.Error -> TODO()
                is HomeUiState.Success -> {
                    val lottoInfos = uiState.lottoInfos
                    val interviews = uiState.interviews

                    WeeklyWinnerResultSection(
                        lottoInfos = lottoInfos,
                        latestLotto645Round = latestLotto645Round,
                        latestLotto720Round = latestLotto720Round,
                        onClickLottoInfo = moveToLottoInfo,
                        onClickNextLottoInfo = onClickNextLottoInfo,
                        onClickPrevLottoInfo = onClickPrevLottoInfo,
                        openLottoTypeInfoBottomSheet = {
                            showLottoInfoBottomSheet = true
                        },
                    )

                    WishWinCardsSection(
                        modifier = Modifier.padding(top = 48.dp),
                        onClickMap = moveToMap,
                        onClickScan = moveToScan,
                    )

                    WinInterviewCardsSection(
                        modifier = Modifier.padding(top = 48.dp),
                        interviews = interviews,
                        onClickInterview = onClickInterview,
                    )
                }
            }
            BannerCard(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(horizontal = Dimens.DefaultPadding20),
                onClickBanner = { onClickBanner(BannerType.WINNER_GUIDE) },
            )

//            MateVoteSection(
//                modifier = Modifier.padding(top = 48.dp),
//            )

            BottomNoticeSection(
                modifier = Modifier.padding(top = 48.dp),
            )
        }

        LottoMateTopAppBar(
            titleRes = R.string.home_title,
            hasNavigation = false,
            isTitleCenter = false,
            actionButtons = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_setting),
                    contentDescription = stringResource(id = R.string.desc_setting_icon),
                    tint = LottoMateGray100,
                    modifier = Modifier.noInteractionClickable { moveToSetting() }
                )
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    LottoMateTheme {
        HomeScreen(
            uiState = HomeUiState.Loading,
            latestLotto645Round = 1152,
            latestLotto720Round = 1152,
            moveToSetting = {},
            moveToLottoInfo = { _, _ -> },
            moveToMap = {},
            moveToScan = {},
            onClickInterview = { _, _ -> },
            onClickNextLottoInfo = { _, _ -> },
            onClickPrevLottoInfo = { _, _ -> },
            onClickBanner = {},
        )
    }
}