package com.lottomate.lottomate.presentation.screen.scanResult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.Lotto645ResultInfo
import com.lottomate.lottomate.domain.model.Lotto720ResultInfo
import com.lottomate.lottomate.domain.model.LottoRank
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.component.LottoMateAnnotatedText
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateDialog
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray110
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.StringUtils
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LottoScanResultRoute(
    vm: LottoScanResultViewModel = hiltViewModel(),
    padding: PaddingValues,
    data: String,
    moveToHome: () -> Unit,
    moveToWinningGuide: () -> Unit,
    moveToMap: () -> Unit,
    moveToInterview: (Int, String) -> Unit,
    moveToPocket: () -> Unit,
    onBackPressed: () -> Unit,
    onShowErrorSnackBar: (LottoMateErrorType) -> Unit,
) {
    val uiState by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest { onShowErrorSnackBar(it) }
    }

    LaunchedEffect(Unit) {
        vm.getLottoResult(data)
    }

    LottoScanResultScreen(
        modifier = Modifier.padding(top = padding.calculateTopPadding()),
        uiState = uiState,
        moveToHome = moveToHome,
        onClickNumberSave = { winningNumbers ->
            vm.saveWinningNumbers(winningNumbers)
//            moveToPocket()
        },
        onBackPressed = onBackPressed,
        onClickBanner = { banner ->
            when (banner) {
                BannerType.MAP -> moveToMap()
                BannerType.WINNER_GUIDE -> moveToWinningGuide()
                BannerType.INTERVIEW -> {
                    val latestInterview = vm.interviews.first()
                    moveToInterview(latestInterview.no, latestInterview.place)
                }
                else -> {}
            }
        },
    )
}

@Composable
private fun LottoScanResultScreen(
    modifier: Modifier = Modifier,
    uiState: LottoScanResultUiState,
    onClickBanner: (BannerType) -> Unit,
    onBackPressed: () -> Unit,
    moveToHome: () -> Unit,
    onClickNumberSave: (List<List<Int>>) -> Unit,
) {
    var showNumberSaveDialog by remember { mutableStateOf(false) }

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

            LottoScanResultUiState.CelebrationLoading -> {
                LottoScanResultCelebrationLoading(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            is LottoScanResultUiState.NotYet -> {
                // 당첨 발표 전
                LottoScanResultNotYet(
                    lottoType = uiState.type,
                    remainDays = uiState.days,
                    moveToHome = moveToHome,
                    onClick = onBackPressed,
                    onClickBanner = onClickBanner,
                )
            }

            is LottoScanResultUiState.Success -> {
                val winResult = uiState.data

                when {
                    winResult.isClaimPeriodExpired -> {
                        LottoResultExpired(
                            onClickBanner = onClickBanner,
                            onClick = onBackPressed,
                        )
                    }
                    winResult.isWinner -> {
                        val winRank = winResult.winningRanksByType
                            .filterNot { it == LottoRank.NONE }
                            .sortedBy { it.rank }
                        val resultInfo = if (winResult.type == LottoType.L645) winResult.winningInfoByType as Lotto645ResultInfo
                        else winResult.winningInfoByType as Lotto720ResultInfo

                        var currentIndex by remember { mutableIntStateOf(0) }

                        LottoScanResultWin(
                            type = winResult.type,
                            isFailed = false,
                            rank = winRank[currentIndex].rank,
                            price = resultInfo.getPrize(winRank[currentIndex].rank),
                            isLast = currentIndex == winRank.lastIndex,
                            onNext = {
                                if (currentIndex < winRank.lastIndex) {
                                    currentIndex += 1 // 다음 결과로 이동
                                }
                            },
                            onComplete = { onBackPressed() },
                            onClickBanner = onClickBanner,
                            onShowNumberSaveDialog = { showNumberSaveDialog = true },
                        )
                    }
                    !winResult.isWinner -> {
                        LottoScanResultWin(
                            type = winResult.type,
                            isFailed = true,
                            rank = -1,
                            isLast = true,
                            onComplete = { onBackPressed() },
                            onClickBanner = onClickBanner,
                            onShowNumberSaveDialog = { showNumberSaveDialog = true },
                        )
                    }
                }
            }
        }

        if (showNumberSaveDialog) {
            val data = (uiState as LottoScanResultUiState.Success).data

            LottoMateDialog(
                title = "${data.type.kr} 당첨 결과를 저장하시겠어요?",
                body = """
                    '로또 보관소 > 내 로또'에서
                    확인할 수 있어요
                """.trimIndent(),
                cancelText = "아니오",
                confirmText = "저장하기",
                onDismiss = {
                    showNumberSaveDialog = false
                    onBackPressed()
                },
                onConfirm = { onClickNumberSave(data.myWinningNumbers) },
            )
        }
    }
}

@Composable
private fun LottoScanResultNotYet(
    modifier: Modifier = Modifier,
    lottoType: LottoType,
    remainDays: Int,
    onClick: () -> Unit,
    moveToHome: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
) {
    val banner by remember {
        mutableStateOf(BannerType.getRandomBannerTypeBeforeResult())
    }
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
                style = LottoMateTheme.typography.title2
                    .copy(color = LottoMateBlack),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            val annotatedMessage = if (remainDays == 0) {
                val keyword = stringResource(
                    id = if (lottoType == LottoType.L645) { R.string.lotto_result_days_today_keyword_645 }
                    else { R.string.lotto_result_days_today_keyword_720 }
                )

                val message = pluralStringResource(id = R.plurals.lotto_result_days_today, count = 1, keyword)
                val startIndex = 3
                val endIndex = startIndex.plus(keyword.length)

                AnnotatedString.Builder(message).apply {
                    addStyle(SpanStyle(color = LottoMateRed50), startIndex, endIndex)
                }.toAnnotatedString()
            } else {
                val days = remainDays.toString().plus("일")
                val message = pluralStringResource(id = R.plurals.lotto_result_days_left, count = 1, days)

                val startIndex = 9
                val endIndex = startIndex.plus(days.length)
                AnnotatedString.Builder(message).apply {
                    addStyle(SpanStyle(color = LottoMateRed50), startIndex, endIndex)
                }.toAnnotatedString()
            }

            LottoMateAnnotatedText(
                annotatedString = annotatedMessage,
                style = LottoMateTheme.typography.headline1
                    .copy(color = LottoMateGray110),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                color = Color.Unspecified
            )

            AsyncImage(
                model = R.drawable.img_lotto_result_loading,
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
            modifier = Modifier.padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            BannerCard(
                type = banner,
                onClickBanner = onClickBanner,
            )

            LottoMateSolidButton(
                text = "확인",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun LottoResultExpired(
    onClick: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoMateText(
                text = "당첨금을 받을 수 있는\n기한이 지났어요",
                style = LottoMateTheme.typography.title2
                    .copy(color = LottoMateBlack),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            AsyncImage(
                model = R.drawable.img_lotto_result00,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(210.dp)
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BannerCard(
                type = BannerType.getBannerResultExpired(),
                onClickBanner = onClickBanner,
            )

            LottoMateSolidButton(
                text = "확인",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LottoScanResultWin(
    type: LottoType,
    isFailed: Boolean,
    rank: Int,
    price: String? = null,
    isLast: Boolean,
    onNext: (() -> Unit)? = null,
    onComplete: () -> Unit,
    onClickBanner: (BannerType) -> Unit,
    onShowNumberSaveDialog: () -> Unit,
) {
    val lotto720Prize = stringArrayResource(id = R.array.lotto720_win_prize)

    Column(
        modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoMateText(
                text = if (isFailed) "아쉽게 미당첨" else {
                    "${type.kr} ${rank}등 당첨"
                },
                style = when (isFailed) {
                    true -> LottoMateTheme.typography.title2
                        .copy(color = LottoMateBlack)

                    false -> LottoMateTheme.typography.title3
                        .copy(color = LottoMateGray120)
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            LottoMateText(
                text = when (isFailed) {
                    true -> "다음 기회엔 꼭 당첨되기를 바라요!"
                    false -> {
                        if (type == LottoType.L645) price.plus("원") ?: ""
                        else lotto720Prize[rank - 1]
                    }
                },
                style = when (isFailed) {
                    true -> LottoMateTheme.typography.headline1
                        .copy(color = LottoMateGray110)
                    false -> LottoMateTheme.typography.display2
                        .copy(color = LottoMateBlack)
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
            )

            if (type == LottoType.L645 && !isFailed && rank != 5) {
                LottoMateText(
                    text = when (rank) {
                        in 1..4 -> StringUtils.formatLottoPrize(
                            price?.replace(",", "")?.toLong() ?: 0L
                        )

                        else -> ""
                    },
                    style = LottoMateTheme.typography.headline2
                        .copy(color = LottoMateGray100),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                )
            }

            AsyncImage(
                model = if (isFailed) {
                    R.drawable.img_lotto_result00
                } else {
                    when (rank) {
                        1 -> R.drawable.img_lotto_result03
                        2, 3 -> R.drawable.img_lotto_result02
                        4, 5 -> R.drawable.img_lotto_result01
                        else -> R.drawable.img_lotto_result_loading
                    }
                },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(188.dp)
                    .padding(top = 24.dp),
            )

            if (!isFailed) {
                LottoMateText(
                    text = "당첨금 수령 방법은 가이드를 확인해 주세요",
                    style = LottoMateTheme.typography.body1
                        .copy(color = LottoMateGray110),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                )
            }
        }

        Column(
            modifier = Modifier.padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BannerCard(
                type = BannerType.getResultScreenBannerType(!isFailed),
                onClickBanner = onClickBanner,
            )

            LottoMateSolidButton(
                text = if (isLast) "확인" else "다음",
                buttonSize = LottoMateButtonProperty.Size.LARGE,
                buttonShape = LottoMateButtonProperty.Shape.NORMAL,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (isLast) {
                        if (isFailed) onComplete()
                        else onShowNumberSaveDialog()
                    }
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
            model = R.drawable.img_lotto_result_loading,
            contentDescription = null,
            modifier = Modifier.padding(top = 32.dp),
        )
    }
}

@Composable
private fun LottoScanResultCelebrationLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottoMateText(
            text = "어라?\n이 느낌은...",
            style = LottoMateTheme.typography.title2,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        AsyncImage(
            model = R.drawable.img_lotto_result_loading,
            contentDescription = null,
            modifier = Modifier.padding(top = 32.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun LottoResultExpiredPreview() {
    LottoMateTheme {
        LottoResultExpired(
            onClick = {},
            onClickBanner = {},
        )
    }
}