package com.lottomate.lottomate.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    MainScreenContent(
        navigator = navigator,
        snackBarHostState = snackBarHostState,
        onShowErrorSnackBar = onShowErrorSnackBar
    )
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            LottoMateBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                tabs = MainBottomTab.entries.toList(),
                currentTab = navigator.currentTab,
                onTabSelected = { navigator.navigate(it) }
            )
        },
        content = { innerPadding ->
            Column {
                MainNavHost(
                    navigator = navigator,
                    padding = innerPadding,
                    onShowErrorSnackBar = onShowErrorSnackBar
                )
            }

        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
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