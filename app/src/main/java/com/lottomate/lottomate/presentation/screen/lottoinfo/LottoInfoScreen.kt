package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateScrollableTabRow
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.component.TabState
import com.lottomate.lottomate.presentation.component.rememberTabState
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.Lotto645WinInfoCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.Lotto720WinInfoCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoRoundWheelPicker
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoWinNumberCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.SpeettoWinInfoCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfoWithBalls
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoMockDatas
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.presentation.ui.LottoMateYellow5
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LottoInfoRoute(
    vm: LottoInfoViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBackPressed: () -> Unit,
    onClickBottomBanner: () -> Unit,
) {
    val lottoInfoUiState by vm.lottoInfo.collectAsStateWithLifecycle()
    val pickerState = rememberPickerState()
    val currentTabIndex by rememberSaveable { vm.currentTabMenu }
    val hasPreRound by rememberSaveable { vm.hasPreLottoRound }
    val hasNextRound by rememberSaveable { vm.hasNextLottoRound }

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest{ throwable -> onShowErrorSnackBar(throwable) }
    }

    LottoInfoScreen(
        modifier = Modifier,
        lottoInfoUiState = lottoInfoUiState,
        currentTabIndex = currentTabIndex,
        hasPreRound = hasPreRound,
        hasNextRound = hasNextRound,
        pickerState = pickerState,
        onBackPressed = onBackPressed,
        onChangeTabMenu = { vm.changeTabMenu(it) },
        onChangeLottoRound = { vm.getLottoInfo(pickerState.selectedItem.toInt()) },
        onClickPreRound = {
            val preLottoRound = it.minus(1)
            vm.getLottoInfo(preLottoRound)
        },
        onClickNextRound = {
            val nextLottoRound = it.plus(1)
            vm.getLottoInfo(nextLottoRound)
        },
        onClickBottomBanner = onClickBottomBanner,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LottoInfoScreen(
    modifier: Modifier = Modifier,
    lottoInfoUiState: LottoInfoUiState,
    currentTabIndex: Int,
    hasPreRound: Boolean,
    hasNextRound: Boolean,
    pickerState: PickerState,
    onBackPressed: () -> Unit,
    onChangeTabMenu: (Int) -> Unit,
    onChangeLottoRound: () -> Unit,
    onClickPreRound: (Int) -> Unit,
    onClickNextRound: (Int) -> Unit,
    onClickBottomBanner: () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    when (lottoInfoUiState) {
        LottoInfoUiState.Loading -> {
            // TODO : 로또 상세 화면 로딩
        }

        is LottoInfoUiState.Success -> {
            LottoInfoContent(
                modifier = modifier,
                currentTabIndex = currentTabIndex,
                lottoInfo = lottoInfoUiState.data,
                hasPreRound = hasPreRound,
                hasNextRound = hasNextRound,
                scaffoldState = scaffoldState,
                pickerState = pickerState,
                onBackPressed = onBackPressed,
                onChangeTabMenu = onChangeTabMenu,
                onChangeLottoRound = onChangeLottoRound,
                onClickPreRound = onClickPreRound,
                onClickNextRound = onClickNextRound,
                onClickBottomBanner = onClickBottomBanner
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LottoInfoContent(
    modifier: Modifier = Modifier,
    currentTabIndex: Int,
    lottoInfo: LottoInfo,
    hasPreRound: Boolean,
    hasNextRound: Boolean,
    scaffoldState: BottomSheetScaffoldState,
    pickerState: PickerState,
    onBackPressed: () -> Unit,
    onChangeTabMenu: (Int) -> Unit,
    onChangeLottoRound: () -> Unit,
    onClickPreRound: (Int) -> Unit,
    onClickNextRound: (Int) -> Unit,
    onClickBottomBanner: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val tabRowState = rememberTabState()
    val isDimVisible by remember {
        derivedStateOf {
            scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded
        }
    }

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LottoMateWhite,
        scaffoldState = scaffoldState,
        sheetContainerColor = LottoMateWhite,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            if (currentTabIndex != 2) {
                val info = lottoInfo as LottoInfoWithBalls

                LottoRoundWheelPicker(
                    currentLottoRound = info.lottoRound,
                    currentTabIndex = currentTabIndex,
                    scaffoldState = scaffoldState,
                    pickerState = pickerState,
                    onClickSelect = onChangeLottoRound,
                )
            }
        },
        sheetDragHandle = null,
        sheetSwipeEnabled = false,
        sheetShape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 80.dp)
            ) {
                TopToggleButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    currentIndex = currentTabIndex,
                    onClick = onChangeTabMenu,
                )

                if (currentTabIndex == LottoType.S2000.ordinal) {
                    val info = lottoInfo as SpeettoInfo
                    
                    SpeettoInfoContent(
                        lottoInfo = info,
                        currentTabIndex = currentTabIndex,
                        tabState = tabRowState,
                    )
                } else {
                    val info = lottoInfo as LottoInfoWithBalls

                    LottoInfoWithBallsContent(
                        currentTabIndex = currentTabIndex,
                        lottoInfo = info,
                        hasPreRound = hasPreRound,
                        hasNextRound = hasNextRound,
                        onClickCurrentRound = {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onClickPreRound = onClickPreRound,
                        onClickNextRound = onClickNextRound
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LottoMateText(
                    text = if (currentTabIndex == 2) {
                        stringResource(id = R.string.lotto_info_bottom_notice_speetto)
                    } else {
                        stringResource(id = R.string.lotto_info_bottom_notice)
                    },
                    style = LottoMateTheme.typography.caption
                        .copy(LottoMateGray80),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )

                Spacer(modifier = Modifier.height(32.dp))

                BottomBannerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 68.dp),
                    onClickBanner = onClickBottomBanner
                )
            }

            LottoMateTopAppBar(
                titleRes = R.string.top_app_bar_title_lotto_info,
                hasNavigation = true,
                onBackPressed = onBackPressed,
            )

            BottomSheetDimBackground(
                modifier = Modifier.fillMaxSize(),
                isDimVisible = isDimVisible,
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                }
            )
        }
    }
}

@Composable
private fun LottoInfoWithBallsContent(
    currentTabIndex: Int,
    lottoInfo: LottoInfoWithBalls,
    hasPreRound: Boolean,
    hasNextRound: Boolean,
    onClickCurrentRound: () -> Unit,
    onClickPreRound: (Int) -> Unit,
    onClickNextRound: (Int) -> Unit,
) {
    Spacer(modifier = Modifier.height(28.dp))

    LottoRoundSection(
        modifier = Modifier.fillMaxWidth(),
        currentRound = lottoInfo.lottoRound,
        currentDate = lottoInfo.lottoDate,
        hasPreRound = hasPreRound,
        hasNextRound = hasNextRound,
        onClickPreRound = { onClickPreRound(lottoInfo.lottoRound) },
        onClickNextRound = { onClickNextRound(lottoInfo.lottoRound) },
        onClickCurrentRound = onClickCurrentRound,
    )

    Spacer(modifier = Modifier.height(24.dp))

    LottoWinNumberSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        lottoInfo = lottoInfo,
        currentLottoType = LottoType.findLottoType(currentTabIndex),
        winNumbers = lottoInfo.lottoNum,
        bonusNumber = lottoInfo.lottoBonusNum,
    )

    Spacer(modifier = Modifier.height(42.dp))

    LottoWinInfoSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        lottoInfo = lottoInfo,
        lottoType = LottoType.findLottoType(currentTabIndex)
    )
}

@Composable
private fun SpeettoInfoContent(
    lottoInfo: SpeettoInfo,
    currentTabIndex: Int,
    tabState: TabState,
) {
    val tabs = listOf(
        LottoType.S2000.num.toString(),
        LottoType.S1000.num.toString(),
        LottoType.S500.num.toString()
    )
    Spacer(modifier = Modifier.height(12.dp))

    LottoMateScrollableTabRow(
        tabState = tabState,
        tabs = tabs,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(modifier = Modifier.height(36.dp))

    LottoMateText(text = "페이지네이션 수정 예정")

    Spacer(modifier = Modifier.height(24.dp))

    LottoWinInfoSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        lottoInfo = lottoInfo,
        lottoType = LottoType.findLottoType(currentTabIndex)
    )
}

@Composable
private fun TopToggleButtons(
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

@Composable
private fun LottoRoundSection(
    modifier: Modifier = Modifier,
    currentRound: Int,
    currentDate: String,
    hasPreRound: Boolean = true,
    hasNextRound: Boolean = false,
    onClickPreRound: () -> Unit,
    onClickNextRound: () -> Unit,
    onClickCurrentRound: () -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    if (hasPreRound) {
                        onClickPreRound()
                    }
                },
            painter = painterResource(id = R.drawable.icon_arrow_small_left),
            tint = if (hasPreRound) LottoMateBlack else LottoMateGray40,
            contentDescription = "Previous Lotto Round Click",
        )

        Row(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClickCurrentRound,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            LottoMateText(
                text = currentRound.toString().plus("회"),
                style = LottoMateTheme.typography.headline1,
            )

            Spacer(modifier = Modifier.width(8.dp))

            LottoMateText(
                text = currentDate.replace("-", "."),
                style = LottoMateTheme.typography.label2
                    .copy(LottoMateGray70),
            )
        }

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    if (hasNextRound) {
                        onClickNextRound()
                    }
                },
            painter = painterResource(id = R.drawable.icon_arrow_small_right),
            tint = if (hasNextRound) LottoMateBlack else LottoMateGray40,
            contentDescription = "Next Lotto Round Click",
        )
    }
}

@Composable
private fun LottoWinNumberSection(
    modifier: Modifier = Modifier,
    lottoInfo: LottoInfo,
    currentLottoType: LottoType,
    winNumbers: List<Int>,
    bonusNumber: List<Int>,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            LottoMateText(
                text = "당첨 번호 보기",
                style = LottoMateTheme.typography.headline1,
            )

            if (currentLottoType == LottoType.L645) {
                val info = lottoInfo as Lotto645Info

                LottoMateText(
                    text = "총 판매 금액 : ${info.totalSalesPrice}원",
                    style = LottoMateTheme.typography.caption
                        .copy(LottoMateGray80),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LottoWinNumberCard(
            lottoType = currentLottoType,
            winNumbers = winNumbers,
            bonusNumbers = bonusNumber,
        )
    }
}

@Composable
private fun LottoWinInfoSection(
    modifier: Modifier = Modifier,
    lottoInfo: LottoInfo,
    lottoType: LottoType,
) {
    Column (
        modifier = modifier,
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
        ) {
            LottoMateText(
                text = "등수별 당첨 정보",
                style = LottoMateTheme.typography.headline1,
            )

            if (lottoType == LottoType.L645) {
                LottoMateText(
                    text = "1인당 당첨 수령 금액",
                    style = LottoMateTheme.typography.caption
                        .copy(LottoMateGray80),
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        when (lottoType) {
            LottoType.L645 -> {
                val info = lottoInfo as Lotto645Info

                repeat(5) { rank ->
                    Lotto645WinInfoCard(
                        rank = rank,
                        lottoInfo = info,
                    )

                    if (rank != 4) Spacer(modifier = Modifier.height(20.dp))
                }
            }
            LottoType.L720 -> {
                val info = lottoInfo as Lotto720Info

                repeat(8) { rank ->
                    Lotto720WinInfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        rank = rank,
                        lottoInfo = info
                    )

                    if (rank != 7) Spacer(modifier = Modifier.height(20.dp))
                }
            }
            else -> {
                val info = lottoInfo as SpeettoInfo

                SpeettoWinInfoCard(
                    speettoWinStoreInfo = info.details,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun BottomBannerSection(
    modifier: Modifier = Modifier,
    onClickBanner: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(
                color = LottoMateYellow5,
                shape = RoundedCornerShape(Dimens.RadiusLarge)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClickBanner
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 20.dp, end = 18.dp),
        ) {
            Column {
                LottoMateText(
                    text = stringResource(id = R.string.banner_lotto_info_title),
                    style = LottoMateTheme.typography.headline2,
                )

                Spacer(modifier = Modifier.height(4.dp))

                LottoMateText(
                    text = stringResource(id = R.string.banner_lotto_info_sub_title),
                    style = LottoMateTheme.typography.caption
                        .copy(LottoMateGray90),
                )
            }

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.bn_pochi_lotto_info),
                contentDescription = "Lotto Info Bottom Banner Image",
                modifier = Modifier.padding(bottom = 7.dp),
            )
        }
    }
}

@Composable
private fun BottomSheetDimBackground(
    modifier: Modifier = Modifier,
    isDimVisible: Boolean,
    onClick: () -> Unit,
) {
    if (isDimVisible) {
        Box(
            modifier = modifier
                .background(LottoMateBlack.copy(0.4f))
                .clickable { onClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoWinInfoSectionPreview() {
    LottoMateTheme {
        LottoWinInfoSection(
            lottoInfo = SpeettoMockDatas[0],
            lottoType = LottoType.S2000
        )
    }
}
class PickerState {
    var selectedItem by mutableStateOf("")
}

@Composable
fun rememberPickerState() = remember { PickerState() }