package com.lottomate.lottomate.presentation.screen.pocket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.pocket.my.MyNumberScreen
import com.lottomate.lottomate.presentation.screen.pocket.random.RandomNumberContent
import com.lottomate.lottomate.presentation.screen.result.model.MyLottoInfo
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PocketRoute(
    vm: PocketViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLottoScan: () -> Unit,
    moveToSetting: () -> Unit,
    onShowGlobalSnackBar: (String) -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
    onClickStorageOfRandomNumbers: () -> Unit,
    onClickDrawRandomNumbers: () -> Unit,
    moveToSaveNumberScreen: () -> Unit,
    moveToLogin: () -> Unit,
    moveToLotteryResult: (LottoType, MyLottoInfo) -> Unit,
) {
    val userProfile by vm.userProfile.collectAsState()
    var currentTabIndex by vm.currentTabIndex
    val drewRandomNumbers by vm.drewRandomNumbers.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.resetDrewRandomLottoNumbers()
    }

    LaunchedEffect(true) {
        vm.snackBarFlow.collectLatest { message ->
            snackBarHostState.showSnackbar(
                message = message,
            )
        }

        vm.errorFlow.collectLatest { error -> onShowErrorSnackBar(error) }
    }

    PocketScreen(
        modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
        currentTabIndex = currentTabIndex,
        drewRandomNumbers = drewRandomNumbers,
        snackBarHostState = snackBarHostState,
        onClickTabMenu = { currentTabIndex = it },
        onClickDrawRandomNumbers = onClickDrawRandomNumbers,
        onClickStorageOfRandomNumbers = onClickStorageOfRandomNumbers,
        onClickCopyRandomNumbers = { vm.copyLottoNumbers(it) },
        onClickSaveRandomNumbers = { vm.saveDrewRandomNumber(it) },
        onClickQRScan = moveToLottoScan,
        onClickSetting = moveToSetting,
        onShowGlobalSnackBar = onShowGlobalSnackBar,
        moveToSaveNumberScreen = {
            userProfile?.let { moveToSaveNumberScreen() }
                ?: run { moveToLogin() }
        },
        moveToLotteryResult = moveToLotteryResult,
    )
}

@Composable
private fun PocketScreen(
    modifier: Modifier = Modifier,
    currentTabIndex: Int,
    drewRandomNumbers: List<List<Int>>,
    snackBarHostState: SnackbarHostState,
    onClickTabMenu: (Int) -> Unit,
    onClickDrawRandomNumbers: () -> Unit,
    onClickStorageOfRandomNumbers: () -> Unit,
    onClickCopyRandomNumbers: (List<Int>) -> Unit,
    onClickSaveRandomNumbers: (List<Int>) -> Unit,
    onClickQRScan: () -> Unit,
    onClickSetting: () -> Unit,
    onShowGlobalSnackBar: (String) -> Unit,
    moveToSaveNumberScreen: () -> Unit,
    moveToLotteryResult: (LottoType, MyLottoInfo) -> Unit,
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
                .padding(top = Dimens.BaseTopPadding),
        ) {
            TopTabMenus(
                modifier = Modifier.padding(top = 24.dp),
                currentTabIndex = currentTabIndex,
                onClickTabMenu = onClickTabMenu,
            )

            when (currentTabIndex) {
                0 -> {
                    MyNumberScreen(
                        onClickQRScan = onClickQRScan,
                        onClickLottoInfo = {},
                        onClickBanner = {},
                        onClickSaveNumbers = { moveToSaveNumberScreen() },
                        onShowGlobalSnackBar = onShowGlobalSnackBar,
                        moveToLotteryResult = moveToLotteryResult,
                    )
                }
                1 -> {
                    RandomNumberContent(
                        modifier = Modifier.padding(top = 32.dp),
                        drewRandomNumbers = drewRandomNumbers,
                        onClickDrawRandomNumbers = onClickDrawRandomNumbers,
                        onClickStorageOfRandomNumbers = onClickStorageOfRandomNumbers,
                        onClickCopyRandomNumbers = onClickCopyRandomNumbers,
                        onClickSaveRandomNumbers = onClickSaveRandomNumbers,
                    )
                }
            }
        }

        snackBarHostState.currentSnackbarData?.let {
            LottoMateSnackBarHost(
                modifier = Modifier.align(Alignment.TopCenter),
                snackBarHostState = snackBarHostState
            ) {
                LottoMateSnackBar(
                    modifier = Modifier
                        .padding(top = Dimens.BaseTopPadding.plus(12.dp)),
                    message = it.visuals.message
                )
            }
        }

        LottoMateTopAppBar(
            titleRes = R.string.pocket_top_app_bar_title,
            hasNavigation = false,
            actionButtons = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_setting),
                    contentDescription = stringResource(id = R.string.desc_setting_icon),
                    modifier = Modifier.noInteractionClickable { onClickSetting() }
                )
            }
        )
    }
}

@Composable
private fun TopTabMenus(
    modifier: Modifier = Modifier,
    currentTabIndex: Int,
    onClickTabMenu: (Int) -> Unit,
) {
    val tabMenus = stringArrayResource(id = R.array.pocket_tab_menu)

    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabMenus.forEachIndexed { index, button ->
            ToggleButtonItem(
                item = button,
                isSelected = currentTabIndex == index,
                onClick = { onClickTabMenu(index) }
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

@Preview(showBackground = true)
@Composable
private fun PocketScreenPreview() {
    LottoMateTheme {
        PocketRoute(
            padding = PaddingValues(0.dp),
            moveToLottoScan = {},
            moveToSetting = {},
            onShowErrorSnackBar = {},
            onClickDrawRandomNumbers = {},
            onClickStorageOfRandomNumbers = {},
            moveToSaveNumberScreen = {},
            moveToLogin = {},
            onShowGlobalSnackBar = {},
            moveToLotteryResult = { _, _ -> },
        )
    }
}