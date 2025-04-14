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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateIconButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewQnA
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUIModel
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateDim2
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateTransparent
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun InterviewRoute(
    vm: InterviewViewModel = hiltViewModel(),
    no: Int,
    place: String,
    onClickBanner: () -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
    onBackPressed: () -> Unit,
) {
    LaunchedEffect(Unit) {
        vm.getInterview(no)
    }

    val interviewUiState by vm.interview.collectAsStateWithLifecycle()
    val winnerInterviewsUiState by vm.winnerInterviews.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest{ error -> onShowErrorSnackBar(error) }
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
    interviewUiState: InterviewUiState<InterviewUIModel>,
    winnerInterviewsUiState: InterviewUiState<List<InterviewUIModel>>,
    onBackPressed: () -> Unit,
    onClickBanner: () -> Unit,
    onClickWinnerInterview: () -> Unit,
    onClickOriginArticle: () -> Unit,
) {
    var showInterviewImage by remember { mutableStateOf(false) }
    var showInterviewImageIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.BaseTopPadding))
            when (interviewUiState) {
                InterviewUiState.Loading -> {
                    // TODO : Interview Screen loading
                }
                is InterviewUiState.Success -> {
                    val interview = interviewUiState.data

                    Spacer(modifier = Modifier.height(12.dp))

                    InterviewContentDetail(
                        modifier = Modifier.fillMaxWidth(),
                        interview = interview,
                        onClickOriginArticle = onClickOriginArticle,
                        onClickInterviewImage = {
                            showInterviewImageIndex = it
                            showInterviewImage = true
                        },
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
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

        LottoMateTopAppBar(
            titleRes = R.string.top_app_bar_empty_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )

        if (showInterviewImage) {
            InterviewImageView(
                index = showInterviewImageIndex,
                images = (interviewUiState as InterviewUiState.Success).data.imgs,
                onDismiss = {
                    showInterviewImage = false
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun InterviewImageSection(
    modifier: Modifier = Modifier,
    imgs: List<String>,
    onClickInterviewImage: (Int) -> Unit,
) {
    val interviewImageHeight = if (imgs.size == 1) 250.dp else 230.dp
    val pagerState = rememberPagerState(
        pageCount = { imgs.size }
    )

    Column(modifier = modifier.wrapContentHeight()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = Dimens.DefaultPadding20),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(interviewImageHeight)
                    .noInteractionClickable { onClickInterviewImage(page) },
                shape = RoundedCornerShape(Dimens.RadiusLarge)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imgs[page])
                        .build(),
                    contentDescription = "Lotto Interview Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.img_review),
                    error = painterResource(id = R.drawable.img_review),
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
                            .padding(start = 6.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InterviewImageView(
    modifier: Modifier = Modifier,
    index: Int,
    images: List<String>,
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { images.size },
    )

    Box(
        modifier = modifier
            .background(color = LottoMateDim2)
            .noInteractionClickable { onDismiss() },
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.align(Alignment.Center)
        ) {page ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = images[page],
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                )

                if (page != 0) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_left),
                        contentDescription = null,
                        tint = LottoMateWhite,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 13.dp)
                            .noInteractionClickable {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(page - 1)
                                }
                            },
                    )
                }

                if (page != images.lastIndex) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null,
                        tint = LottoMateWhite,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 13.dp)
                            .noInteractionClickable {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(page + 1)
                                }
                            },
                    )
                }
            }
        }

        LottoMateTopAppBar(
            titleRes = R.string.top_app_bar_empty_title,
            hasNavigation = false,
            actionButtons = {
                LottoMateIconButton(
                    iconRes = R.drawable.icon_close,
                    contentDescription = "Interview Image Close Button",
                    color = LottoMateWhite.copy(alpha = 0.6f),
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.width(7.dp))
            },
            backgroundColor = LottoMateTransparent
        )
    }
}

@Composable
private fun InterviewContentDetail(
    modifier: Modifier = Modifier,
    interview: InterviewUIModel,
    onClickOriginArticle: () -> Unit,
    onClickInterviewImage: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        InterviewTitle(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .fillMaxWidth(),
            interview = interview,
        )

        if (interview.imgs.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            InterviewImageSection(
                modifier = Modifier.fillMaxWidth(),
                imgs = interview.imgs,
                onClickInterviewImage = onClickInterviewImage
            )
        }

        Spacer(
            modifier = Modifier
                .height(
                    if (interview.imgs.isEmpty() || interview.imgs.size == 1) 32.dp
                    else 10.dp
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.DefaultPadding20)
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
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.noInteractionClickable { onClickOriginArticle() }
                ) {
                    LottoMateText(
                        text = "원문 보러가기",
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray100)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InterviewTitle(
    modifier: Modifier = Modifier,
    interview: InterviewUIModel,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LottoMateText(
                text = "${interview.lottoRound}회차",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray120)
            )
            
            LottoMateText(
                text = "|",
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray120),
                modifier = Modifier.padding(start = 4.dp),
            )

            LottoMateText(
//                text = "${interview.subTitle} ${if (interview.lottoPrize % 10 == 0.0) Math.round(interview.lottoPrize) else interview.lottoPrize}억",
                text = "",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray120),
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        LottoMateText(
            text = interview.title,
            style = LottoMateTheme.typography.title3
        )

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LottoMateText(
                text = "인터뷰 ${interview.interviewDate}",
                style = LottoMateTheme.typography.caption2
                    .copy(color = LottoMateGray80)
            )

            LottoMateText(
                text = "|",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray80),
                modifier = Modifier.padding(start = 8.dp)
            )

            LottoMateText(
                text = "작성 ${interview.uploadDate}",
                style = LottoMateTheme.typography.caption2
                    .copy(color = LottoMateGray80),
                modifier = Modifier.padding(start = 8.dp),
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

        LottoMateText(
            text = answer,
            style = LottoMateTheme.typography.body1,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun BottomInterviewListContent(
    modifier: Modifier = Modifier,
    interviewList: List<InterviewUIModel>,
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    LottoMateText(
                        text = subTitle,
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray80)
                    )
                    LottoMateText(
                        text = title,
                        style = LottoMateTheme.typography.label2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                LottoMateText(
                    text = interviewDate,
                    style = LottoMateTheme.typography.caption2
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

@Preview(showBackground = true)
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

@Preview(showBackground = true)
@Composable
private fun InterviewPreview() {
    LottoMateTheme {
        InterviewScreen(
            interviewUiState = InterviewUiState.Success(InterviewMockData.copy(title = "만약두줄이넘어가면세로로내려가야하는거겠죠그럼만약에세줄까지나오는경우가있을까요설마", imgs = emptyList())),
            winnerInterviewsUiState = InterviewUiState.Success(List(5) { InterviewMockData }),
            onBackPressed = {},
            onClickBanner = {},
            onClickWinnerInterview = {},
            onClickOriginArticle = {}
        )
    }
}

val InterviewMockData = InterviewUIModel(
    no = 20,
    title = "스피또 1등은 아버지의 성실함 덕분!",
    interviewDate = "2017-04-14",
    uploadDate = "2017-04-14",
    thumb = "https://lottomate-review.s3.ap-northeast-2.amazonaws.com/13406_1.jpg",
    imgs = listOf(
        "https://lottomate-review.s3.ap-northeast-2.amazonaws.com/13406_1.jpg",
        "https://lottomate-review.s3.ap-northeast-2.amazonaws.com/13406_2.jpg"
    ),
    contents = listOf(
        InterviewQnA(
            "▶ 당첨되신 걸 어떻게 알게 되셨고, 또 알았을 때 기분이 어떠셨나요?",
            " -> 이전에 구입했던 스피또500 4장을 바꾸면서 추가로 복권을 좀 더 구입했다. 사무실에 와서 혼자 복권을 긁었는데 1등에 당첨되서 너무 놀랐다. 심장이 두근두근 터질 것 같은 기분이 들었고 집으로 뛰어가 남편에게 당첨사실을 말했다. 남편은 집에 큰일이 일어난 줄 알고 깜짝 놀랐지만 복권에 당첨됐다는 사실을 알고는 기뻐했다."
        ),
        InterviewQnA(
            "▶ 최근 기억에 남는 꿈이 있으신가요?",
            "-> 주변 지인이 꿈에 3번 나왔다. 이상한 꿈이라고만 생각했고 꿈 때문에 당첨됐다고는 생각하지 않는다. 지금까지 힘들게 살아왔는데 앞으로 잘 살아보라는 하늘의 뜻이라고 생각한다. "
        ),
        InterviewQnA(
            "▶ 당첨이 되기 위한 본인만의 전략이나 구매 방법이 있으신가요?",
            "-> 특별한 전략은 없다. 그냥 복권판매점 주인이 주는 복권을 구입한다."
        ),
        InterviewQnA(
            "▶ 평소에 어떤 복권을 자주 구매하시나요?",
            "-> 로또복권, 연금복권, 즉석복권을 자주 구입한다."
        ),
        InterviewQnA(
            "▶ 당첨금은 어디에 사용하실 계획인가요?",
            "-> 대출금을 갚고 나머지는 사업자금으로 쓸 계획이다."
        ),
    ),
    originalNo = 13406,
)