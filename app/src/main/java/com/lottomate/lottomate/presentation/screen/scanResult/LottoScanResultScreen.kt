package com.lottomate.lottomate.presentation.screen.scanResult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateAnnotatedText
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray110
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.DateUtils

@Composable
fun LottoScanResultRoute(
    vm: LottoScanResultViewModel = hiltViewModel(),
    padding: PaddingValues,
    data: String,
    moveToHome: () -> Unit,
    onBackPressed: () -> Unit,
) {
    val uiState by vm.lottoWinResultInfo.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        val parseResult = data.split("?v=")[1]

        vm.getLottoResultByRound(parseResult)
    }

    LottoScanResultScreen(
        modifier = Modifier.padding(top = padding.calculateTopPadding()),
        uiState = uiState,
        moveToHome = moveToHome,
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun LottoScanResultScreen(
    modifier: Modifier = Modifier,
    uiState: LottoScanResultUiState,
    onBackPressed: () -> Unit,
    moveToHome: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        when (uiState) {
            LottoScanResultUiState.Loading -> {
                LottoScanResultLoading(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            LottoScanResultUiState.NotYet -> {
                // 당첨 발표 전
                LottoScanResultNotYet(
                    moveToHome = moveToHome,
                )
            }

            is LottoScanResultUiState.Success -> {
                val winResultInfo = uiState.data
                val isFailed = winResultInfo.all { it == -1 }

                Column(
                    modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (isFailed) {
                        true -> {
                            // 미당첨 화면
                            LottoScanResultWin(
                                isFailed = true,
                                rank = -1,
                                isLast = true,
                                onComplete = { onBackPressed() },
                            )
                        }
                        false -> {
                            // 당첨 화면
                            val realWinResultInfo = winResultInfo.sorted().filter { it != -1 }

                            var currentIndex by remember { mutableStateOf(0) }

                            LottoScanResultWin(
                                isFailed = false,
                                rank = realWinResultInfo[currentIndex],
                                isLast = currentIndex == realWinResultInfo.lastIndex,
                                onNext = {
                                    if (currentIndex < realWinResultInfo.lastIndex) {
                                        currentIndex += 1 // 다음 결과로 이동
                                    }
                                },
                                onComplete = { onBackPressed() },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoScanResultNotYet(
    modifier: Modifier = Modifier,
    moveToHome: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoMateText(
                text = "아직 당첨 발표 전이에요",
                style = LottoMateTheme.typography.title2,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            // 남은 일수
            val daysLeft = if (DateUtils.getDaysUntilNextSaturday() == 0) {
                val time = DateUtils.getHoursUntilTargetTime(20, 45)
                time.toString().plus("시간")
            } else DateUtils.getDaysUntilNextSaturday().toString().plus("일")

            val message = pluralStringResource(id = R.plurals.lotto_result_days_left, count = 1, daysLeft)

            val startIndex = 9
            val endIndex = 9.plus(daysLeft.length)
            val annotatedMessage = AnnotatedString.Builder(message).apply {
                addStyle(SpanStyle(color = LottoMateRed50), startIndex, endIndex)
            }.toAnnotatedString()

            LottoMateAnnotatedText(
                annotatedString = annotatedMessage,
                style = LottoMateTheme.typography.headline1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                color = Color.Unspecified
            )

            AsyncImage(
                model = R.drawable.img_lotto_scan_result_slo_cony,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(210.dp)
            )

            LottoMateAssistiveButton(
                text = "로또메이트 홈으로 이동하기",
                buttonSize = LottoMateButtonProperty.Size.MEDIUM,
                onClick = moveToHome,
                modifier = Modifier.padding(top = 32.dp),
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 36.dp),
        ) {
            BannerCard(
                onClickBanner = {

                },
            )

            LottoMateSolidButton(
                text = "확인",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = moveToHome,
            )
        }
    }
}

@Composable
private fun LottoScanResultWin(
    modifier: Modifier = Modifier,
    isFailed: Boolean,
    rank: Int,
    isLast: Boolean,
    onNext: (() -> Unit)? = null,
    onComplete: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        LottoMateText(
            text = if (isFailed) "이런.. 아쉽게 미당첨" else {
                when (rank) {
                    1 -> "로또 1등 당첨"
                    2 -> "로또 2등 당첨"
                    3 -> "로또 3등 당첨"
                    4 -> "로또 4등 당첨"
                    5 -> "로또 5등 당첨"
                    else -> ""
                }
            },
            style = LottoMateTheme.typography.title2,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 92.dp.plus(Dimens.StatusBarHeight)),
        )

        LottoMateText(
            text = if (isFailed) "다음 기회엔 꼭 당첨을 기원합니다." else {
                when (rank) {
                    1 -> "로또 1등 당첨"
                    2 -> "로또 2등 당첨"
                    3 -> "로또 3등 당첨"
                    4 -> "로또 4등 당첨"
                    5 -> "로또 5등 당첨"
                    else -> ""
                }
            },
            style = LottoMateTheme.typography.headline1
                .copy(color = LottoMateGray110),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        AsyncImage(
            model = if (isFailed) R.drawable.img_lotto_scan_result_slo_cony_failed else R.drawable.img_lotto_scan_result_slo_cony,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Column {
            BannerCard(
                onClickBanner = {

                },
            )

            LottoMateSolidButton(
                text = if (isLast) "확인" else "다음",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = {
                    if (isLast) onComplete()
                    else onNext?.invoke()
                },
            )
        }
    }
}

@Composable
private fun LottoScanResultLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottoMateText(
            text = "두구두구두구....\n결과는 과연?",
            style = LottoMateTheme.typography.title2,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        AsyncImage(
            model = R.drawable.img_lotto_scan_result_slo_cony,
            contentDescription = null,
            modifier = Modifier.padding(top = 32.dp),
        )
    }
}