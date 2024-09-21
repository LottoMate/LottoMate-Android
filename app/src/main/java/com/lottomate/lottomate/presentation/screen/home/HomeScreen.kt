package com.lottomate.lottomate.presentation.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeRoute(
    padding: PaddingValues,
    onClickLottoInfo: () -> Unit,
    onClickInterview: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        onClickLottoInfo = onClickLottoInfo,
        onClickInterview = onClickInterview,
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onClickLottoInfo: () -> Unit,
    onClickInterview: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = "Home Screen")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onClickLottoInfo) {
            Text(text = "Move Lotto Info View")
        }

        Button(onClick = onClickInterview) {
            Text(text = "Move Lotto Review")
        }
    }
}