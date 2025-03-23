package com.lottomate.lottomate.presentation.screen.pocket.random

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DrawRandomNumbersRoute(
    vm: DrawRandomNumbersViewModel = hiltViewModel(),
    padding: PaddingValues,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
    onBackPressed: () -> Unit,
) {
    val uiState by vm.randomNumbers.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        vm.snackBarFlow.collectLatest { message ->
            snackBarHostState.showSnackbar(
                message = message,
            )
        }

        vm.errorFlow.collectLatest { error -> onShowErrorSnackBar(error) }
    }

    DrawRandomNumbersScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onClickComplete = onBackPressed,
    )
}

@Composable
private fun DrawRandomNumbersScreen(
    modifier: Modifier = Modifier,
    uiState: DrawRandomNumbersUiState,
    snackBarHostState: SnackbarHostState,
    onClickComplete: () -> Unit,
) {
    val density = LocalDensity.current
    var bottomCompleteBtnHeight by remember { mutableIntStateOf(0) }
    val bottomCompleteBtnHeightDp = with (density) { bottomCompleteBtnHeight.toDp() }

    Scaffold(
        containerColor = LottoMateWhite,
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LottoMateWhite),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LottoMateText(
                    text = if (uiState is DrawRandomNumbersUiState.Loading) stringResource(id = R.string.pocket_title_processing)
                    else stringResource(id = R.string.pocket_title_complete),
                    style = LottoMateTheme.typography.title2,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(36.dp))

                Image(
                    painter = painterResource(id = R.drawable.pocket_venus_boli_bori_2),
                    contentDescription = stringResource(id = R.string.desc_pocket_image),
                )

                Spacer(modifier = Modifier.height(36.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = Dimens.DefaultPadding20)
                        .background(LottoMateGray10, RoundedCornerShape(60.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(modifier = Modifier.padding(vertical = 12.dp, horizontal = 37.dp)) {
                        when (uiState) {
                            DrawRandomNumbersUiState.Loading -> {
                                // TODO : 로띠 전달받으면 수정 예정
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(LottoMateGray60, CircleShape)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(LottoMateGray60, CircleShape)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(LottoMateGray60, CircleShape)
                                )
                            }

                            is DrawRandomNumbersUiState.Success -> {
                                val numbers = uiState.randomNumbers

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    numbers.forEach { num ->
                                        LottoBall645(
                                            number = num,
                                            size = 28.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(74.dp))
            }

            if (uiState is DrawRandomNumbersUiState.Success) {
                LottoMateSolidButton(
                    text = stringResource(id = R.string.common_complete),
                    buttonSize = LottoMateButtonProperty.Size.LARGE,
                    onClick = onClickComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.DefaultPadding20)
                        .align(Alignment.BottomCenter)
                        .onSizeChanged {
                            bottomCompleteBtnHeight = it.height
                        }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = bottomCompleteBtnHeightDp.plus(14.dp),
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                snackBarHostState.currentSnackbarData?.let {
                    LottoMateSnackBarHost(snackBarHostState = snackBarHostState) {
                        LottoMateSnackBar(message = it.visuals.message)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawRandomNumbersScreenPreview() {
    DrawRandomNumbersScreen(
        uiState = DrawRandomNumbersUiState.Success(listOf(1, 2, 3, 4, 5, 6, 7)),
        snackBarHostState = SnackbarHostState(),
        onClickComplete = {}
    )
}