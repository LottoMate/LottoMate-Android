package com.lottomate.lottomate.presentation.screen.setting.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateDialog
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun MyPageRoute(
    vm: MyPageViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToHome: () -> Unit,
    moveToSignOut: () -> Unit,
    onBackPressed: () -> Unit,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    MyPageScreen(
        onClickLogout = { showLogoutDialog = true },
        onClickSignOut = moveToSignOut,
        onBackPressed = onBackPressed,
    )

    when {
        showLogoutDialog -> {
            LottoMateDialog(
                title = stringResource(id = R.string.mypage_logout_title),
                confirmText = stringResource(id = R.string.common_confirm),
                cancelText = stringResource(id = R.string.common_cancel),
                onConfirm = {
                    showLogoutDialog = false
                    vm.logOut()
                    moveToHome()
                },
                onDismiss = {
                    showLogoutDialog = false
                }
            )
        }
    }
}

@Composable
private fun MyPageScreen(
    modifier: Modifier = Modifier,
    onClickLogout: () -> Unit,
    onClickSignOut: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier.padding(top = Dimens.BaseTopPadding)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            MyPageRowItem(
                title = stringResource(id = R.string.mypage_menu_logout),
                onClick = onClickLogout,
            )

            MyPageRowItem(
                title = stringResource(id = R.string.mypage_menu_signout),
                onClick = onClickSignOut,
            )
        }

        LottoMateTopAppBar(
            titleRes = R.string.mypage_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )
    }
}

@Composable
private fun MyPageRowItem(
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 18.dp)
            .padding(start = 20.dp, end = 13.dp)
            .fillMaxWidth(),
    ) {
        LottoMateText(
            text = title,
            style = LottoMateTheme.typography.body1,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPagePreview() {
    LottoMateTheme {
        MyPageScreen(
            onClickLogout = {},
            onClickSignOut = {},
            onBackPressed = {},
        )
    }
}