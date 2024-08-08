package com.lottomate.lottomate.presentation.screen.lottoinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.BottomSheetScaffold
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
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoWinNumberCard
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfoItem
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray40
import com.lottomate.lottomate.presentation.ui.LottoMateGray70
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.launch

enum class LottoType(val game: String) {
    LOTTO645("로또"), LOTTO720("연금복권"), SPEETTO("스피또");

    companion object {
        fun findCurrentLottoType(currentIndex: Int): LottoType {
            return entries[currentIndex]
        }
    }
}

class PickerState {
    var selectedItem by mutableStateOf("")
}

@Composable
fun rememberPickerState() = remember { PickerState() }

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
    var currentLottoTypeIndex by rememberSaveable {
        mutableIntStateOf(LottoType.LOTTO720.ordinal)
    }

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
                initialRound = currentRound,
                scaffoldState = scaffoldState,
                pickerState = pickerState,
                onSelectRound = {
                    currentRound = pickerState.selectedItem.toInt()
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            TopToggleButtonSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                currentIndex = currentLottoTypeIndex,
                onClick = { currentLottoTypeIndex = it }
            )

            Spacer(modifier = Modifier.height(28.dp))

            LottoInfoContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                currentLottoType = LottoType.findCurrentLottoType(currentLottoTypeIndex),
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
}

@Composable
private fun LottoInfoContent(
    modifier: Modifier = Modifier,
    currentLottoType: LottoType,
    lottoWinInfo: LottoInfoItem,
    hasPreRound: Boolean,
    hasNextRound: Boolean,
    onClickPreRound: () -> Unit,
    onClickNextRound: () -> Unit,
    onClickCurrentRound: () -> Unit,
) {
    Column(modifier = modifier) {
        LottoRoundSection(
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

        LottoWinNumberSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            currentLottoType = currentLottoType,
            winNumbers = lottoWinInfo.drwtNums,
            bonusNumber = lottoWinInfo.drwtBonusNum,
        )
    }
}

@Composable
private fun TopToggleButtonSection(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    onClick: (Int) -> Unit,
) {
    val toggleButtons = listOf(
        LottoType.LOTTO645.game,
        LottoType.LOTTO720.game,
        LottoType.SPEETTO.game,
    )

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

@Preview(showBackground = true)
@Composable
private fun LottoInfoScreenPreview() {
    LottoMateTheme {
        LottoInfoScreen(
            onBackPressed = {}
        )
    }
}