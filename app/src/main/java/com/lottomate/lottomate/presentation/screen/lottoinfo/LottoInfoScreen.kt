package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoRoundWheelPicker
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfoItem
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateGray90
import com.lottomate.lottomate.presentation.ui.LottoMateGreen50
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateTransparent
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.presentation.ui.LottoMateYellow50
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LottoInfoScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val pickerState = rememberPickerState()
    val coroutineScope = rememberCoroutineScope()
    val lastRound by remember { mutableIntStateOf(1131) }
    var currentRound by remember() { mutableIntStateOf(1131) }

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
        LottoInfoContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            lottoWinInfo = LottoInfoItem.lottoInfoMock.first {
                it.roundNum == currentRound
            },
            hasPreRound = currentRound > 1,
            hasNextRound = currentRound < lastRound,
            onClickPreRound = { currentRound = currentRound.minus(1) },
            onClickNextRound = { currentRound = currentRound.plus(1) },
            onClickCurrentRound = {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            }
        )
    }
}

@Composable
private fun LottoInfoContent(
    modifier: Modifier = Modifier,
    lottoWinInfo: LottoInfoItem,
    hasPreRound: Boolean,
    hasNextRound: Boolean,
    onClickPreRound: () -> Unit,
    onClickNextRound: () -> Unit,
    onClickCurrentRound: () -> Unit,
) {
    var currentIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))

        TopToggleButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            currentIndex = currentIndex,
            onClick = { index ->
                currentIndex = index
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        LottoRound(
            modifier = Modifier.fillMaxWidth(),
            currentRound = lottoWinInfo.roundNum,
            currentDate = lottoWinInfo.drwtDate.replace("-", "."),
            hasPreRound = hasPreRound,
            hasNextRound = hasNextRound,
            onClickPreRound = onClickPreRound,
            onClickNextRound = onClickNextRound,
            onClickCurrentRound = onClickCurrentRound
        )

        Spacer(modifier = Modifier.height(24.dp))

        LottoWinNumbers(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            winNumbers = lottoWinInfo.drwtNums,
            bonusNumber = lottoWinInfo.drwtBonusNum,
        )
    }
}

@Composable
private fun TopToggleButtons(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    onClick: (Int) -> Unit,
) {
    val toggleButtons = listOf("로또", "연금복권", "스피또")

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
private fun LottoRound(
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
private fun LottoWinNumbers(
    modifier: Modifier = Modifier,
    winNumbers: List<Int>,
    bonusNumber: Int,
) {
    Column(modifier = modifier) {
        Text(
            text = "당첨 번호 보기",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier,
            colors = CardDefaults.cardColors(
                containerColor = LottoMateWhite,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = LottoMateGray20
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "당첨 번호",
                        style = MaterialTheme.typography.labelSmall
                            .copy(color = LottoMateGray70)
                    )
                    Text(
                        text = "보너스",
                        style = MaterialTheme.typography.labelSmall
                            .copy(color = LottoMateGray70)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    winNumbers.forEach { number ->
                        LottoBall(number = number)
                    }

                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Just Separator",
                    )

                    LottoBall(number = bonusNumber)
                }
            }
        }
    }
}

@Composable
private fun LottoBall(
    modifier: Modifier = Modifier,
    number: Int,
) {
    Box(
        modifier = modifier
            .background(
                color = when (number) {
                    in 1..10 -> LottoMateYellow50
                    in 11..20 -> LottoMateBlue50
                    in 21..30 -> LottoMateRed50
                    in 31..40 -> LottoMateGray90
                    in 41..45 -> LottoMateGreen50
                    else -> LottoMateTransparent
                },
                shape = CircleShape,
            )
            .size(30.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.labelMedium.copy(
                color = LottoMateWhite
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoBallPreview() {
    LottoMateTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            LottoBall(number = 1)
            LottoBall(number = 11)
            LottoBall(number = 21)
            LottoBall(number = 31)
            LottoBall(number = 41)
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