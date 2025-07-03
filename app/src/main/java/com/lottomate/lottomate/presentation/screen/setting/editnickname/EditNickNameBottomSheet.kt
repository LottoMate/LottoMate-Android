package com.lottomate.lottomate.presentation.screen.setting.editnickname

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTextField
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateDim1
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.launch

private const val LIMIT_NICKNAME_LENGTH = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNickNameBottomSheet(
    vm: EditNickNameBottomSheetViewModel = hiltViewModel(),
    nickName: String,
    onDismiss: () -> Unit = {},
    onComplete: () -> Unit = {},
) {
    val uiState by vm.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(nickName) {
        vm.init(nickName)
    }

    LaunchedEffect(Unit) {
        vm.effect.collect { isComplete ->
            if (isComplete) {
                onComplete()
            }
        }
    }

    LaunchedEffect(bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue == SheetValue.Expanded) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = LottoMateWhite,
        scrimColor = LottoMateDim1,
        dragHandle = null,
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        NickNameEditBottomSheetContents(
            uiState = uiState,
            focusRequester = focusRequester,
            focusManager = focusManager,
            onChangeValue = { input -> vm.editNickName(input) },
            onDismiss = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion { onDismiss() }
            },
            onClickChange = { vm.changeNickName() }
        )
    }
}

@Composable
private fun NickNameEditBottomSheetContents(
    modifier: Modifier = Modifier,
    uiState: EditNickNameBottomSheetUiState,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    onChangeValue: (String) -> Unit,
    onDismiss: () -> Unit,
    onClickChange: () -> Unit,
) {
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LottoMateWhite)
            .padding(horizontal = Dimens.DefaultPadding20)
            .focusRequester(focusRequester)
            .noInteractionClickable {
                focusManager.clearFocus()
            },
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        LottoMateText(
            text = stringResource(R.string.setting_nickname_edit_title),
            style = LottoMateTheme.typography.headline1
                .copy(color = LottoMateBlack),
        )

        LottoMateTextField(
            modifier = Modifier.padding(top = 28.dp),
            text = uiState.nickName,
            placeHolder = stringResource(R.string.setting_nickname_placeholder),
            limitTextLength = LIMIT_NICKNAME_LENGTH,
            errorText = when (uiState.errorType)  {
                EditNickNameBottomSheetUiState.ErrorType.MIN_LENGTH -> stringResource(R.string.setting_nickname_error_min_length)
                EditNickNameBottomSheetUiState.ErrorType.DUPLICATED_NICKNAME -> stringResource(R.string.setting_nickname_error_duplicated)
                else -> null
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onClickChange()
                },
            ),
            supportText = if (uiState.isComplete) stringResource(R.string.setting_nickname_complete) else null,
            onChangeValue = { onChangeValue(it) },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LottoMateAssistiveButton(
                text = stringResource(R.string.common_cancel),
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                modifier = Modifier.weight(1f),
                onClick = onDismiss,
            )

            LottoMateSolidButton(
                text = stringResource(R.string.common_confirm),
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                modifier = Modifier.weight(1f),
                buttonType = if (uiState.nickName.isEmpty()) LottoMateButtonProperty.Type.DISABLED else LottoMateButtonProperty.Type.ACTIVE,
                onClick = { onClickChange() },
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun EditNickNameBottomSheetPreview() {
    EditNickNameBottomSheet(
        nickName = "Test NickName",
        onDismiss = {},
    )
}