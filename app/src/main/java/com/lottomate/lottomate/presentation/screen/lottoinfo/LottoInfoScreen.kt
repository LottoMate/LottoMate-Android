package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.Lotto645WinInfoCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.Lotto720WinInfoCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoRoundWheelPicker
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoWinNumberCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue5
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
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

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "당첨 번호 상세",
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        containerColor = LottoMateWhite,
        scaffoldState = scaffoldState,
        sheetContainerColor = LottoMateWhite,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            LottoRoundWheelPicker(
                currentLottoRound = lottoInfo.lottoRndNum,
                currentTabIndex = currentTabIndex,
                scaffoldState = scaffoldState,
                pickerState = pickerState,
                onClickSelect = onChangeLottoRound,
            )
        },
        snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.height(24.dp))

            TopToggleButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                currentIndex = currentTabIndex,
                onClick = onChangeTabMenu,
            )

            Spacer(modifier = Modifier.height(28.dp))

            LottoRoundSection(
                modifier = Modifier.fillMaxWidth(),
                currentRound = lottoInfo.lottoRndNum,
                currentDate = lottoInfo.drwtDate,
                hasPreRound = hasPreRound,
                hasNextRound = hasNextRound,
                onClickPreRound = { onClickPreRound(lottoInfo.lottoRndNum) },
                onClickNextRound = { onClickNextRound(lottoInfo.lottoRndNum) },
                onClickCurrentRound = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LottoWinNumberSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                currentLottoType = LottoType.findLottoType(currentTabIndex),
                winNumbers = lottoInfo.drwtNum,
                bonusNumber = lottoInfo.drwtBonusNum,
            )

            Spacer(modifier = Modifier.height(42.dp))

            LottoWinInfoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                lottoInfo = lottoInfo,
                lottoType = LottoType.findLottoType(currentTabIndex)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(id = R.string.lotto_info_bottom_notice),
                style = MaterialTheme.typography.labelSmall,
                color = LottoMateGray70,
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
    }
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    if (hasPreRound) {
                        onClickPreRound()
                    }
                },
            imageVector = Icons.Rounded.KeyboardArrowLeft,
            tint = if (hasPreRound) LottoMateBlack else LottoMateGray40,
            contentDescription = "Previous Lotto Round Click",
        )
        Spacer(modifier = Modifier.width(80.dp))

        Row(
            modifier = Modifier.clickable {
                onClickCurrentRound()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = currentRound.toString().plus("회"),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = currentDate,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = LottoMateGray70,
                )
            )
        }

        Spacer(modifier = Modifier.width(80.dp))
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    if (hasNextRound) {
                        onClickNextRound()
                    }
                },
            imageVector = Icons.Rounded.KeyboardArrowRight,
            tint = if (hasNextRound) LottoMateBlack else LottoMateGray40,
            contentDescription = "Next Lotto Round Click",
        )
    }
}

@Composable
private fun LottoWinNumberSection(
    modifier: Modifier = Modifier,
    currentLottoType: LottoType,
    winNumbers: List<Int>,
    bonusNumber: List<Int>,
) {
    Column(modifier = modifier) {
        Text(
            text = "당첨 번호 보기",
            style = MaterialTheme.typography.headlineLarge
        )

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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "등수별 당첨 정보",
                style = MaterialTheme.typography.headlineLarge
            )

            if (lottoType == LottoType.L645) {
                val info = lottoInfo as Lotto645Info

                Text(
                    text = "총 판매 금액 : ${info.drwtSaleMoney}원",
                    style = MaterialTheme.typography.labelSmall,
                    color = LottoMateGray70,
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
                // TODO : 스피또 (수정예정)
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
            .background(
                color = LottoMateBlue5,
                shape = RoundedCornerShape(Dimens.RadiusLarge)
            )
            .clip(RoundedCornerShape(Dimens.RadiusLarge))
            .clickable { onClickBanner() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.banner_lotto_info_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.banner_lotto_info_sub_title),
                style = MaterialTheme.typography.labelSmall,
                color = LottoMateGray120
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoInfoScreenPreview() {
    LottoMateTheme {
        LottoInfoScreen(
            onBackPressed = {}
        )
    }
}

class PickerState {
    var selectedItem by mutableStateOf("")
}

@Composable
fun rememberPickerState() = remember { PickerState() }