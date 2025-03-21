package com.lottomate.lottomate.presentation.screen.map.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.ui.LottoMateDim1
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LottoTypeSelectorBottomSheet(
    modifier: Modifier = Modifier,
    selectedLottoTypes: List<String>,
    onDismiss: () -> Unit,
    onSelectLottoTypes: (List<Boolean>) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    val allLottoType = LottoTypeFilter.toList()
        .slice(LottoTypeFilter.All.ordinal..LottoTypeFilter.Speetto.ordinal)

    val selectLottoTypeState = remember {
        mutableStateListOf(
            mutableStateOf(selectedLottoTypes.contains(allLottoType[1])),
            mutableStateOf(selectedLottoTypes.contains(allLottoType[2])),
            mutableStateOf(selectedLottoTypes.contains(allLottoType[3])),
        )
    }

    if (selectedLottoTypes.contains(allLottoType[0])) {
        selectLottoTypeState[0].value = true
        selectLottoTypeState[1].value = true
        selectLottoTypeState[2].value = true
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier.fillMaxWidth(),
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        ),
        containerColor = LottoMateWhite,
        dragHandle = null,
        scrimColor = LottoMateDim1,
        windowInsets = WindowInsets.navigationBars
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .padding(horizontal = 20.dp)
        ) {
            LottoMateText(
                text = stringResource(id = R.string.map_lotto_type_selection_title),
                style = LottoMateTheme.typography.headline1,
            )

            Spacer(modifier = Modifier.height(24.dp))

            allLottoType.subList(1, 4).forEachIndexed { index, type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .noInteractionClickable {
                            selectLottoTypeState[index].value = !selectLottoTypeState[index].value
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    LottoMateText(
                        text = type,
                        style = LottoMateTheme.typography.body1,
                    )

                    if (selectLottoTypeState[index].value) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_check),
                            contentDescription = stringResource(id = R.string.desc_selection_icon_map),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                if (index != allLottoType.lastIndex) Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                LottoMateAssistiveButton(
                    text = stringResource(id = R.string.map_lotto_type_selection_button_cancel),
                    buttonSize = LottoMateButtonProperty.Size.LARGE,
                    buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onDismiss,
                )

                Spacer(modifier = Modifier.width(15.dp))

                LottoMateSolidButton(
                    text = stringResource(id = R.string.map_lotto_type_selection_button_save),
                    buttonSize = LottoMateButtonProperty.Size.LARGE,
                    buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                    buttonType = if (selectLottoTypeState.all { !it.value }) LottoMateButtonProperty.Type.DISABLED
                    else LottoMateButtonProperty.Type.ACTIVE ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { onSelectLottoTypes(selectLottoTypeState.map { it.value }) }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoTypeSelectorBottomSheetPreview() {
    LottoMateTheme {
        LottoTypeSelectorBottomSheet(
            selectedLottoTypes = listOf(LottoTypeFilter.All.kr),
            onDismiss = {},
            onSelectLottoTypes = {}
        )
    }
}