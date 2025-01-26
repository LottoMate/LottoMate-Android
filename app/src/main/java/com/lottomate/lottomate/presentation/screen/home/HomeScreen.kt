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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.home.component.BottomNoticeSection
import com.lottomate.lottomate.presentation.screen.home.component.LottoTypeInfoBottomSheet
import com.lottomate.lottomate.presentation.screen.home.component.MateVoteSection
import com.lottomate.lottomate.presentation.screen.home.component.TopLottoNotice
import com.lottomate.lottomate.presentation.screen.home.component.WeeklyWinnerResultSection
import com.lottomate.lottomate.presentation.screen.home.component.WinInterviewCardsSection
import com.lottomate.lottomate.presentation.screen.home.component.WishWinCardsSection
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    padding: PaddingValues,
    onClickLottoInfo: () -> Unit,
    onClickInterview: () -> Unit,
    onClickLogin: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    HomeScreen(
        modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
        onClickLogin = onClickLogin,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onClickLogin: () -> Unit,
) {
    val bottomSheetScaffoldState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        modifier = Modifier,
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Box(modifier = modifier) {
                LottoTypeInfoBottomSheet(
                    onClickClose = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.hide()
                        }
                    }
                )
            }
        },
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
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

                WeeklyWinnerResultSection(
                    openLottoTypeInfoBottomSheet = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.show()
                        }
                    }
                )

                WishWinCardsSection(
                    modifier = Modifier.padding(top = 36.dp),
                )

                WinInterviewCardsSection(
                    modifier = Modifier.padding(top = 48.dp),
                    onClickInterview = {

                    },
                )

                BannerCard(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .padding(horizontal = Dimens.DefaultPadding20),
                    onClickBanner = {

                    },
                )

                MateVoteSection(
                    modifier = Modifier.padding(top = 48.dp),
                )

                BottomNoticeSection(
                    modifier = Modifier.padding(top = 56.dp),
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
                        modifier = Modifier.noInteractionClickable {  }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    LottoMateTheme {
        HomeScreen(
            onClickLogin = {}
        )
    }
}