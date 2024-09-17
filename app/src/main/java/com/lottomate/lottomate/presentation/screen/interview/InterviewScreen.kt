package com.lottomate.lottomate.presentation.screen.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.interview.model.Interview
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewMockData
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateTransparent
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InterviewRoute(
    vm: InterviewViewModel = hiltViewModel(),
    onClickBanner: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBackPressed: () -> Unit,
) {
    val interviewUiState by vm.interview.collectAsStateWithLifecycle()
    val winnerInterviewsUiState by vm.winnerInterviews.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest{ throwable -> onShowErrorSnackBar(throwable) }
    }

    InterviewScreen(
        interviewUiState = interviewUiState,
        winnerInterviewsUiState = winnerInterviewsUiState,
        onBackPressed = onBackPressed,
        onClickBanner = onClickBanner,
        onClickWinnerInterview = {

        },
        onClickOriginArticle = {

        },
    )
}

@Composable
private fun InterviewScreen(
    interviewUiState: InterviewUiState<Interview>,
    winnerInterviewsUiState: InterviewUiState<List<Interview>>,
    onBackPressed: () -> Unit,
    onClickBanner: () -> Unit,
    onClickWinnerInterview: () -> Unit,
    onClickOriginArticle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(LottoMateWhite)
    ) {
        when (interviewUiState) {
            InterviewUiState.Loading -> {
                // TODO : Interview Screen loading
            }
            is InterviewUiState.Success -> {
                val interview = interviewUiState.data

                LottoMateTopAppBar(
                    titleRes = R.string.top_app_bar_empty_title,
                    backgroundColor = LottoMateTransparent,
                    hasNavigation = true,
                    onBackPressed = onBackPressed,
                )

                Spacer(modifier = Modifier.height(12.dp))

                InterviewContentDetail(
                    modifier = Modifier.fillMaxWidth(),
                    interview = interview,
                    onClickOriginArticle = onClickOriginArticle,
                )
            }
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 10.dp,
            color = LottoMateGray20
        )

        when (winnerInterviewsUiState) {
            InterviewUiState.Loading -> {
                // TODO : Interview Screen loading
            }
            is InterviewUiState.Success -> {
                val interviews = winnerInterviewsUiState.data

                BottomInterviewListContent(
                    modifier = Modifier.fillMaxWidth(),
                    interviewList = interviews,
                    onClickInterviewItem = onClickWinnerInterview,
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        BannerCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClickBanner = onClickBanner
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun InterviewImageSection(
    modifier: Modifier = Modifier,
    imgs: List<String>,
) {
    val pagerState = rememberPagerState(
        pageCount = { imgs.size }
    )

    Column(modifier = modifier.wrapContentHeight()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                shape = RoundedCornerShape(Dimens.RadiusLarge)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imgs[page])
                        .build(),
                    contentDescription = "Lotto Interview Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.img_review),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        if (imgs.size != 1) {
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pagerState.pageCount) { page ->
                    val color = if (pagerState.currentPage == page) MaterialTheme.colorScheme.primary else LottoMateBlack.copy(alpha = 0.2f)

                    Box(
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InterviewContentDetail(
    modifier: Modifier = Modifier,
    interview: Interview,
    onClickOriginArticle: () -> Unit,
) {
    Column(modifier = modifier) {
        InterviewTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            interview = interview,
        )

        if (interview.imgs.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            InterviewImageSection(
                modifier = Modifier.fillMaxWidth(),
                imgs = interview.imgs,
            )
        }

        Spacer(
            modifier = Modifier
                .height(
                    if (interview.imgs.isEmpty() || interview.imgs.size == 1) 32.dp
                    else 12.dp
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            interview.contents.forEachIndexed { index, qna ->
                InterviewContentQnA(
                    question = qna.question,
                    answer = qna.answer,
                )

                if (index != interview.contents.lastIndex) Spacer(modifier = Modifier.height(28.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                LottoMateText(
                    text = stringResource(id = R.string.interview_content_bottom_notice),
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.noInteractionClickable { onClickOriginArticle() }
                ) {
                    LottoMateText(
                        text = "원문 보러가기",
                        style = LottoMateTheme.typography.caption1
                            .copy(color = LottoMateGray100)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = "",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun InterviewTitle(
    modifier: Modifier = Modifier,
    interview: Interview,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LottoMateText(
                text = "${interview.lottoRound}회차",
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray120)
            )
            Spacer(modifier = Modifier.width(4.dp))
            LottoMateText(
                text = "•",
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.caption1
                    .copy(color = LottoMateGray120)
            )
            Spacer(modifier = Modifier.width(4.dp))
            LottoMateText(
                text = "${interview.subTitle} ${if (interview.lottoPrize % 10 == 0.0) Math.round(interview.lottoPrize) else interview.lottoPrize}억",
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray120)
            )
        }

        LottoMateText(
            text = interview.title,
            style = LottoMateTheme.typography.title3
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            LottoMateText(
                text = "인터뷰 ${interview.interviewDate}",
                style = LottoMateTheme.typography.caption1
                    .copy(color = LottoMateGray80)
            )
            Spacer(modifier = Modifier.width(8.dp))
            LottoMateText(
                text = "|",
                style = LottoMateTheme.typography.caption1
                    .copy(color = LottoMateGray80)
            )
            Spacer(modifier = Modifier.width(8.dp))
            LottoMateText(
                text = "작성 ${interview.uploadDate}",
                style = LottoMateTheme.typography.caption1
                    .copy(color = LottoMateGray80)
            )
        }
    }
}

@Composable
private fun InterviewContentQnA(
    modifier: Modifier = Modifier,
    question: String,
    answer: String,
) {
    Column(modifier = modifier) {
        LottoMateText(
            text = "Q. $question",
            style = LottoMateTheme.typography.headline2,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LottoMateText(
            text = answer,
            style = LottoMateTheme.typography.body1,
        )
    }
}

@Composable
private fun BottomInterviewListContent(
    modifier: Modifier = Modifier,
    interviewList: List<Interview>,
    onClickInterviewItem: () -> Unit,
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp)
        ) {
            LottoMateText(
                text = "로또 당첨자 후기",
                style = LottoMateTheme.typography.headline1
                    .copy(color = LottoMateGray120)
            )

            LottoMateText(
                text = "역대 로또 당첨자들의 생생한 후기예요.",
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray80)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            repeat(5) {
                if (it == 0) Spacer(modifier = Modifier.width(20.dp))

                BottomInterviewListItem(
                    title = interviewList[it].title,
                    subTitle = interviewList[it].subTitle,
                    thumb = interviewList[it].thumb,
                    interviewDate = interviewList[it].interviewDate,
                    onClick = onClickInterviewItem
                )

                if (it != 4) Spacer(modifier = Modifier.width(16.dp))
                else Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

@Composable
private fun BottomInterviewListItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    thumb: String,
    interviewDate: String,
    onClick: () -> Unit,
) {
    LottoMateCard(
        modifier = modifier.size(width = 220.dp, height = 202.dp),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = R.drawable.img_review,
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.img_review),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp) ){
                LottoMateText(
                    text = subTitle,
                    style = LottoMateTheme.typography.caption1
                        .copy(color = LottoMateGray80)
                )
                LottoMateText(
                    text = title,
                    style = LottoMateTheme.typography.label2,
                )
                LottoMateText(
                    text = interviewDate,
                    style = LottoMateTheme.typography.caption2
                        .copy(color = LottoMateGray80)
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
private fun InterviewImageSectionPreview() {
    LottoMateTheme {
        InterviewScreen(
            interviewUiState = InterviewUiState.Success(InterviewMockData),
            winnerInterviewsUiState = InterviewUiState.Success(List(5) { InterviewMockData }),
            onBackPressed = {},
            onClickBanner = {},
            onClickWinnerInterview = {},
            onClickOriginArticle = {}
        )
    }
}