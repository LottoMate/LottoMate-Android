package com.lottomate.lottomate.presentation.screen.pocket.random

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.GifImageResourceType
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateGifImage
import com.lottomate.lottomate.presentation.component.LottoMateSnackBar
import com.lottomate.lottomate.presentation.component.LottoMateSnackBarHost
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
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
                when (uiState) {
                    DrawRandomNumbersUiState.Loading -> GeneratingRandomNumbersScreen()

                    is DrawRandomNumbersUiState.Success -> {
                        val numbers = uiState.randomNumbers

                        LottoMateText(
                            text = stringResource(id = R.string.pocket_title_complete),
                            style = LottoMateTheme.typography.title2
                                .copy(color = LottoMateBlack),
                            textAlign = TextAlign.Center,
                        )

                        Image(
                            painter = painterResource(id = R.drawable.img_pocket_random_number_complete),
                            contentDescription = stringResource(id = R.string.desc_pocket_image),
                            modifier = Modifier
                                .padding(top = 42.dp)
                                .size(260.dp),
                        )

                        Box(
                            modifier = Modifier
                                .padding(top = 48.dp)
                                .padding(horizontal = Dimens.DefaultPadding20)
                                .background(
                                    color = LottoMateGray10,
                                    shape = RoundedCornerShape(60.dp)
                                )
                                .padding(vertical = 12.dp, horizontal = 37.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(vertical = 4.dp, horizontal = 8.5.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                for (num in numbers) {
                                    LottoBall645(
                                        number = num,
                                        size = 28.dp,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (uiState is DrawRandomNumbersUiState.Success) {
                LottoMateSolidButton(
                    text = stringResource(id = R.string.common_complete),
                    buttonSize = LottoMateButtonProperty.Size.LARGE,
                    onClick = onClickComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.DefaultPadding20)
                        .padding(bottom = 36.dp)
                        .align(Alignment.BottomCenter),
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
    }
}

@Composable
private fun GeneratingRandomNumbersScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottoMateText(
            text = stringResource(id = R.string.draw_random_number_title_loading),
            style = LottoMateTheme.typography.title2,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Box(
            modifier = Modifier.padding(top = 16.dp),
        ) {
            LottoMateGifImage(
                modifier = Modifier.size(width = 262.dp, height = 284.dp),
                gifImageResourceType = GifImageResourceType.RANDOM_NUMBER,
            )

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_pocket_random_number_draw),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 14.dp)
                    .size(180.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawRandomNumbersScreenPreview() {
    DrawRandomNumbersScreen(
        uiState = DrawRandomNumbersUiState.Success(listOf(1, 2, 3, 4, 5, 6)),
        snackBarHostState = SnackbarHostState(),
        onClickComplete = {}
    )
}