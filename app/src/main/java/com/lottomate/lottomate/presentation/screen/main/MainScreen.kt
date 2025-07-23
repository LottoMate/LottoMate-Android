package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.data.error.ErrorMessageProvider
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.LottoMateDialog
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.main.component.LottoMateBottomBar
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { throwable ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar("Error Message")
        }
    }

    val onShowGlobalSnackBar: (message: String) -> Unit = { message ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    MainScreenContent(
        navigator = navigator,
        snackBarHostState = snackBarHostState,
        onShowGlobalSnackBar = onShowGlobalSnackBar,
        onShowErrorSnackBar = onShowErrorSnackBar
    )
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    onShowGlobalSnackBar: (String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val context = LocalContext.current

    var showErrorDialog by remember { mutableStateOf<LottoMateErrorType?>(null) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = navigator.isInMainBottomTab,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LottoMateBottomBar(
                    modifier = Modifier.navigationBarsPadding(),
                    tabs = MainBottomTab.entries.toList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = { navigator.navigate(it) }
                )
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                MainNavHost(
                    navigator = navigator,
                    padding = innerPadding,
                    onShowGlobalSnackBar = { onShowGlobalSnackBar(it) },
                    onShowErrorSnackBar = { showErrorDialog = it }
                )

                showErrorDialog?.let {
                    LottoMateDialog(
                        title = ErrorMessageProvider.getErrorMessage(context, it),
                        confirmText = "확인",
                        onDismiss = { showErrorDialog = null },
                        onConfirm = { showErrorDialog = null },
                    )
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
            }
        },
        containerColor = LottoMateWhite,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenPreview() {
    LottoMateTheme {
        MainScreen()
    }
}