package com.lottomate.lottomate.presentation.screen.pocket.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.BottomSheetDimBackground
import com.lottomate.lottomate.presentation.screen.lottoinfo.PickerState
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoRoundWheelPicker
import com.lottomate.lottomate.presentation.screen.lottoinfo.rememberPickerState
import com.lottomate.lottomate.presentation.screen.pocket.register.component.LottoNumberSection
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterLottoNumberContract
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterLottoNumberUiModel
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryInputType
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.launch

@Composable
fun RegisterLottoNumbersRoute(
    vm: RegisterLottoNumberViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToLottoResult: (LotteryInputType, MyLottoInfo) -> Unit,
    onBackPressed: () -> Unit,
    onShowGlobalSnackBar: (String) -> Unit,
) {
    val context = LocalContext.current
    val pickerState = rememberPickerState()

    val uiState by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                RegisterLottoNumberContract.Effect.ShowSuccessSnackBar -> {
                    val message = context.getString(R.string.register_lotto_number_text_complete)
                    onShowGlobalSnackBar(message)
                }
                is RegisterLottoNumberContract.Effect.NavigateToLotteryResult -> moveToLottoResult(effect.inputType, effect.myLottoInfo)
            }
        }
    }

    RegisterLottoNumbersScreen(
        uiState = uiState,
        pickerState = pickerState,
        onClickRegister = { inputLotto645, inputLotto720 ->
            vm.handleEvent(RegisterLottoNumberContract.Event.ClickSave(inputLotto645, inputLotto720))
        },
        onChangeLottoRound = { lottoType -> vm.handleEvent(RegisterLottoNumberContract.Event.ChangeLottoRound(lottoType, pickerState.selectedItem)) },
        onClickPreLottoRound = { type -> vm.handleEvent(RegisterLottoNumberContract.Event.ClickPreRound(type)) },
        onClickNextLottoRound = { type -> vm.handleEvent(RegisterLottoNumberContract.Event.ClickNextRound(type)) },
        onBackPressed = onBackPressed,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterLottoNumbersScreen(
    modifier: Modifier = Modifier,
    uiState: RegisterLottoNumberContract.State,
    pickerState: PickerState,
    onBackPressed: () -> Unit,
    onChangeLottoRound: (LottoType) -> Unit,
    onClickPreLottoRound: (LottoType) -> Unit,
    onClickNextLottoRound: (LottoType) -> Unit,
    onClickRegister: (List<RegisterLottoNumberUiModel>, List<RegisterLottoNumberUiModel>) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val inputLotto645Numbers = remember { mutableStateListOf(RegisterLottoNumberUiModel.EMPTY) }
    val inputLotto720Numbers = remember { mutableStateListOf(RegisterLottoNumberUiModel.EMPTY) }
    var selectLottoType by remember { mutableStateOf(LottoType.L645) }

    val isDimVisible by remember {
        derivedStateOf {
            scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded
        }
    }

    BottomSheetScaffold(
        modifier = modifier,
        containerColor = LottoMateWhite,
        scaffoldState = scaffoldState,
        sheetContainerColor = LottoMateWhite,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            LottoRoundWheelPicker(
                currentLottoRound = if (selectLottoType == LottoType.L645) uiState.currentLotto645RoundInfo.round else uiState.currentLotto720RoundInfo.round,
                lotteryType = selectLottoType,
                scaffoldState = scaffoldState,
                pickerState = pickerState,
                isRegisterScreen = true,
                onClickSelect = { onChangeLottoRound(selectLottoType) },
            )
        },
        sheetDragHandle = null,
        sheetSwipeEnabled = false,
        sheetShape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(LottoMateWhite),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(Dimens.BaseTopPadding))

                    LottoMateText(
                        text = stringResource(id = R.string.register_lotto_number_top_title),
                        style = LottoMateTheme.typography.title3
                            .copy(color = LottoMateBlack),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = Dimens.DefaultPadding20),
                    )

                    LottoMateText(
                        text = stringResource(id = R.string.register_lotto_number_top_sub_title),
                        style = LottoMateTheme.typography.body1
                            .copy(color = LottoMateGray80),
                        modifier = Modifier
                            .padding(horizontal = Dimens.DefaultPadding20),
                    )

                    LottoNumberSection(
                        modifier = Modifier.padding(top = 40.dp),
                        lotteryType = LottoType.L645,
                        round = uiState.currentLotto645RoundInfo.round,
                        date = uiState.currentLotto645RoundInfo.drawDate,
                        inputNumbers = inputLotto645Numbers,
                        hasPreRound = uiState.hasLotto645PreRound,
                        hasNextRound = uiState.hasLotto645NextRound,
                        onAddNewInputNumber = { inputLotto645Numbers.add(0, RegisterLottoNumberUiModel.EMPTY) },
                        onRemoveInputNumber = { index -> inputLotto645Numbers.removeAt(index) },
                        onChangeInputNumber = { index, number -> inputLotto645Numbers[index] = inputLotto645Numbers[index].copy(lottoNumbers = number) },
                        onChangeReset = { index -> inputLotto645Numbers[index] = RegisterLottoNumberUiModel.EMPTY },
                        onClickLottoRoundPicker = {
                            selectLottoType = it

                            coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                        },
                        onClickNextRound = { onClickNextLottoRound(LottoType.L645) },
                        onClickPreRound = { onClickPreLottoRound(LottoType.L645) },
                    )

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp)
                        .background(color = LottoMateGray20)
                        .height(10.dp)
                    )

                    LottoNumberSection(
                        modifier = Modifier.padding(top = 40.dp, bottom = 91.dp),
                        lotteryType = LottoType.L720,
                        round = uiState.currentLotto720RoundInfo.round,
                        date = uiState.currentLotto720RoundInfo.drawDate,
                        inputNumbers = inputLotto720Numbers,
                        hasPreRound = uiState.hasLotto720PreRound,
                        hasNextRound = uiState.hasLotto720NextRound,
                        onAddNewInputNumber = { inputLotto720Numbers.add(0, RegisterLottoNumberUiModel.EMPTY) },
                        onRemoveInputNumber = { index -> inputLotto720Numbers.removeAt(index) },
                        onChangeInputNumber = { index, number -> inputLotto720Numbers[index] = inputLotto720Numbers[index].copy(lottoNumbers = number) },
                        onChangeReset = { index -> inputLotto720Numbers[index] = RegisterLottoNumberUiModel.EMPTY },
                        onClickLottoRoundPicker = {
                            selectLottoType = it

                            coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                        },
                        onClickPreRound = { onClickPreLottoRound(LottoType.L720) },
                        onClickNextRound = { onClickNextLottoRound(LottoType.L720) },
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.bg_btn_register_lotto_number),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                )

                LottoMateSolidButton(
                    text = stringResource(id = R.string.common_save),
                    buttonSize = LottoMateButtonProperty.Size.LARGE,
                    buttonType = if (inputLotto645Numbers.first().lottoNumbers.length >= 12 || inputLotto720Numbers.first().lottoNumbers.length >= 6 || inputLotto645Numbers.size > 1 || inputLotto720Numbers.size > 1) LottoMateButtonProperty.Type.ACTIVE else LottoMateButtonProperty.Type.DISABLED,
                    onClick = {
                        focusManager.clearFocus()

                        // 유효성 검사 (로또)
                        inputLotto645Numbers.forEachIndexed { index, lotto ->
                            // 첫번째 리스트일 경우 (가장 최상단) + 비어있으면 검사 X
                            if (index == 0 && lotto.lottoNumbers.isEmpty()) return@forEachIndexed

                            // 12자리 이하이면 오류
                            if (lotto.lottoNumbers.length < 12) {
                                inputLotto645Numbers[index] = lotto.copy(isError = true)
                                return@forEachIndexed
                            }
                            // 2자리씩 나눠서 숫자로 변환 후 모두 1~45 사이인지 확인
                            val valid = lotto.lottoNumbers.chunked(2).all { it.toInt() in 1..45 }
                            inputLotto645Numbers[index] = lotto.copy(isError = !valid)
                        }

                        // 유효성 검사 (연금복권)
                        inputLotto720Numbers.forEachIndexed { index, lotto ->
                            // 첫번째 리스트일 경우 (가장 최상단) + 비어있으면 검사 X
                            if (index == 0 && lotto.lottoNumbers.isEmpty()) return@forEachIndexed

                            if (lotto.lottoNumbers.length < 6) {
                                inputLotto720Numbers[index] = lotto.copy(isError = true)
                                return@forEachIndexed
                            }

                            // 2자리씩 나눠서 숫자로 변환 후 모두 1~45 사이인지 확인
                            val valid = lotto.lottoNumbers.all {number ->
                                number.digitToIntOrNull()?.let {
                                    it in 0..9
                                } ?: false
                            }
                            inputLotto720Numbers[index] = lotto.copy(isError = !valid)
                        }

                        // 모든 로또 번호가 유효한지 검사 후 등록 실행
                        val vaildLotto645Number = inputLotto645Numbers.all {
                            it.lottoNumbers.chunked(2).all { numbers -> numbers.toInt() in 1..45 }
                        }

                        val vaildLotto720Number = inputLotto720Numbers.all {
                            it.lottoNumbers.all { number ->
                                number.digitToIntOrNull()?.let {
                                    it in 0..9
                                } ?: false
                            }
                        }

                        if (vaildLotto645Number && vaildLotto720Number) {
                            onClickRegister(inputLotto645Numbers, inputLotto720Numbers)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = Dimens.DefaultPadding20),
                )
            }

            LottoMateTopAppBar(
                titleRes = R.string.register_lotto_number_title,
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

@Preview(showBackground = true)
@Composable
private fun RegisterLottoNumberScreenPreview() {
    LottoMateTheme {
        val pickerState = rememberPickerState()

        RegisterLottoNumbersScreen(
            uiState = RegisterLottoNumberContract.State(),
            pickerState = pickerState,
            onClickRegister = { _, _ -> },
            onChangeLottoRound = {},
            onClickPreLottoRound = {},
            onClickNextLottoRound = {},
            onBackPressed = {},
        )
    }
}
