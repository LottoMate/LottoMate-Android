package com.lottomate.lottomate.presentation.screen.setting.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateCheckBoxWithText
import com.lottomate.lottomate.presentation.component.LottoMateDialog
import com.lottomate.lottomate.presentation.component.LottoMateMultiTextField
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun SignOutRoute(
    padding: PaddingValues,
    moveToHome: () -> Unit,
    onBackPressed: () -> Unit,
) {
    SignOutScreen(
        onClickSignOut = { index, message ->
            // TODO : 회원탈퇴 플로우 진행 -> 홈으로 이동
            Log.d("회원탈퇴", "index: $index, message: $message")
            moveToHome()
        },
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun SignOutScreen(
    modifier: Modifier = Modifier,
    onClickSignOut: (Int, String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val isReasonListChecked = remember { mutableStateListOf(false, false, false, false) }
    var goodByeMessage by remember { mutableStateOf("") }
    var showSignOutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite)
            .pointerInput(Unit) {
                detectTapGestures(
                    // TextField 외 터치 시, 포커스 해제
                    onTap = { focusManager.clearFocus() }
                )
            },
    ) {
        Column(
            modifier = Modifier
                .padding(top = Dimens.BaseTopPadding)
                .padding(horizontal = Dimens.DefaultPadding20)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_signout),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(top = 36.dp),
            )

            SignOutReasonSection(
                modifier = Modifier.padding(top = Dimens.DefaultPadding20),
                isReasonListChecked = isReasonListChecked,
                onChangeReasonState = { index, state ->
                    // 선택한 버튼 외 모두 선택 해제
                    for (i in isReasonListChecked.indices) {
                        isReasonListChecked[i] = false
                    }

                    isReasonListChecked[index] = state
                },
            )

            BottomGoodByeSection(
                modifier = Modifier.padding(top = 32.dp),
                message = goodByeMessage,
                onChangeMessage = { goodByeMessage = it },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp),
            ) {
                LottoMateAssistiveButton(
                    text = stringResource(id = R.string.common_cancel),
                    buttonSize = LottoMateButtonProperty.Size.LARGE,
                    onClick = onBackPressed,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(15.dp))

                LottoMateSolidButton(
                    text = stringResource(id = R.string.common_confirm),
                    buttonSize = LottoMateButtonProperty.Size.LARGE,
                    onClick = { showSignOutDialog = true },
                    buttonType = if (isReasonListChecked.any { it }) LottoMateButtonProperty.Type.ACTIVE else LottoMateButtonProperty.Type.DISABLED,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        if (showSignOutDialog) {
            LottoMateDialog(
                title = stringResource(id = R.string.signout_dialog_title),
                confirmText = stringResource(id = R.string.common_confirm),
                onConfirm = {
                    showSignOutDialog = false

                    // 선택된 탈퇴 이유의 index (만약 선택되어있지 않으면 -1 반환, but DISABLED 상태면 onClick 이벤트 발생 X)
                    onClickSignOut(isReasonListChecked.indexOfFirst { it }, goodByeMessage)
                },
                onDismiss = {},
            )
        }

        LottoMateTopAppBar(
            titleRes = R.string.signout_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )
    }
}

@Composable
private fun SignOutReasonSection(
    modifier: Modifier = Modifier,
    isReasonListChecked: List<Boolean>,
    onChangeReasonState: (Int, Boolean) -> Unit,
) {
    val reasonList = stringArrayResource(id = R.array.signout_reason_items)

    Column(
        modifier = modifier,
    ) {
        Row {
            LottoMateText(
                text = stringResource(id = R.string.signout_reason_title),
                style = LottoMateTheme.typography.headline1,
            )

            LottoMateText(
                text = "*",
                style = LottoMateTheme.typography.headline1
                    .copy(color = LottoMateRed50),
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        reasonList.forEachIndexed { index, reason ->
            LottoMateCheckBoxWithText(
                modifier = Modifier.padding(top = 16.dp),
                text = reason,
                isChecked = isReasonListChecked[index],
                onClick = { state -> onChangeReasonState(index, state) },
            )
        }
    }
}

@Composable
private fun BottomGoodByeSection(
    modifier: Modifier = Modifier,
    message: String,
    onChangeMessage: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        LottoMateText(
            text = stringResource(id = R.string.signout_goodbye_title),
            style = LottoMateTheme.typography.headline1,
        )

        LottoMateMultiTextField(
            modifier = Modifier.padding(top = 12.dp),
            text = message,
            placeHolder = stringResource(id = R.string.signout_goodbye_content_placeholder),
            hasLimitTextLength = true,
            textLengthLimit = 200,
            onTextChange = onChangeMessage,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignOutPreview() {
    LottoMateTheme {
        SignOutScreen(
            onClickSignOut = { _, _ -> },
            onBackPressed = {},
        )
    }
}