package com.lottomate.lottomate.presentation.screen.interview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateIconButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewDetailUiModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewQnA
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
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
        vm.getInterview(no, place)
    }

    val scrollState = rememberLazyListState()
    val uiState by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest{ error -> onShowErrorSnackBar(error) }
    }

    LaunchedEffect(uiState) {
        if (uiState is InterviewUiState.Success) {
            scrollState.animateScrollToItem(0)
        }
    }

    InterviewScreen(
        uiState = uiState,
        scrollState = scrollState,
        onBackPressed = onBackPressed,
        onClickBanner = onClickBanner,
        onClickWinnerInterview = { no, place ->
            vm.getInterview(no, place)
        },
        onClickOriginArticle = {

        },
    )
}

@Composable
private fun InterviewScreen(
    uiState: InterviewUiState,
    scrollState: LazyListState,
    onBackPressed: () -> Unit,
    onClickBanner: () -> Unit,
    onClickWinnerInterview: (Int, String) -> Unit,
    onClickOriginArticle: () -> Unit,
) {
    var showInterviewImage by remember { mutableStateOf(false) }
    var showInterviewImageIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
        ) {
            item { Spacer(modifier = Modifier.height(Dimens.BaseTopPadding)) }

            when (uiState) {
                InterviewUiState.Loading -> {}
                is InterviewUiState.Success -> {
                    val interview = uiState.interview
                    val interviews = uiState.interviews

                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    item {
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

                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(top = 32.dp)
                                .fillMaxWidth(),
                            thickness = 10.dp,
                            color = LottoMateGray20
                        )
                    }

                    item {
                        BottomInterviewListContent(
                            modifier = Modifier.fillMaxWidth(),
                            interviewList = interviews,
                            onClickInterviewItem = onClickWinnerInterview,
                        )
                    }

                    item { Spacer(modifier = Modifier.height(40.dp)) }

                    item {
                        BannerCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            onClickBanner = onClickBanner
                        )
                    }

                    item { Spacer(modifier = Modifier.height(60.dp)) }
                }
            }
        }

        LottoMateTopAppBar(
            titleRes = R.string.top_app_bar_empty_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )

        if (showInterviewImage) {
            InterviewImageView(
                index = showInterviewImageIndex,
                images = (uiState as InterviewUiState.Success).interview.imgs,
                onDismiss = { showInterviewImage = false },
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
                    .border(1.dp, LottoMateGray20, RoundedCornerShape(Dimens.RadiusLarge))
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

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels.toFloat()
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.toFloat()


    val imageGestureState = rememberTransformableState { zoomChange, panChange, _ ->
        // 1. 스케일 계산
        scale = (scale * zoomChange).coerceIn(1f, 5f)

        // 2. 이미지 너비 계산
        val imageWidth = screenWidth * scale
        val imageHeight = screenHeight * scale

        val maxOffsetX = ((imageWidth - screenWidth) / 2f).coerceAtLeast(0f)
        val maxOffsetY = ((imageHeight - screenHeight) / 2f).coerceAtLeast(0f)

        offsetX = (offsetX + panChange.x).coerceIn(-maxOffsetX, maxOffsetX)
        offsetY = (offsetY + panChange.y).coerceIn(-maxOffsetY, maxOffsetY)
    }

    LaunchedEffect(pagerState.currentPage) {
        scale = 1f
        offsetX = 0f
    }

    Box(
        modifier = modifier
            .background(color = LottoMateDim2)
            .fillMaxSize()
            .transformable(imageGestureState)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 5f)

                    val imageWidth = screenWidth * scale
                    val imageHeight = screenHeight * scale

                    val maxOffsetX = ((imageWidth - screenWidth) / 2f).coerceAtLeast(0f)
                    val maxOffsetY = ((imageHeight - screenHeight) / 2f).coerceAtLeast(0f)

                    offsetX = (offsetX + pan.x).coerceIn(-maxOffsetX, maxOffsetX)
                    offsetY = (offsetY + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                }
            }
            .noInteractionClickable { onDismiss() },
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.align(Alignment.Center),
            userScrollEnabled = scale <= 1f,
        ) {page ->
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                AsyncImage(
                    model = images[page],
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .fillMaxWidth()
                )

                if (page != 0) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_left_shadow),
                        contentDescription = null,
                        tint = Color.Unspecified,
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
                        painter = painterResource(id = R.drawable.icon_arrow_right_shadow),
                        contentDescription = null,
                        tint = Color.Unspecified,
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
    interview: InterviewDetailUiModel,
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
            Column(
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                interview.contents.forEach { qna ->
                    InterviewContentQnA(
                        question = qna.question,
                        answer = qna.answer,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LottoMateText(
                    text = stringResource(id = R.string.interview_content_bottom_notice),
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80),
                )

                Row(
                    modifier = Modifier.clickable { onClickOriginArticle() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    LottoMateText(
                        text = "원문 보러가기",
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray100)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = "",
                        tint = LottoMateGray100,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun InterviewTitle(
    modifier: Modifier = Modifier,
    interview: InterviewDetailUiModel,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LottoMateText(
                text = interview.place,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray120)
            )
            
//            LottoMateText(
//                text = "|",
//                textAlign = TextAlign.Center,
//                style = LottoMateTheme.typography.caption
//                    .copy(color = LottoMateGray120),
//                modifier = Modifier.padding(start = 4.dp),
//            )

//            LottoMateText(
////                text = "${interview.subTitle} ${if (interview.lottoPrize % 10 == 0.0) Math.round(interview.lottoPrize) else interview.lottoPrize}억",
//                text = "",
//                style = LottoMateTheme.typography.caption
//                    .copy(color = LottoMateGray120),
//                modifier = Modifier.padding(start = 4.dp),
//            )
        }

        LottoMateText(
            text = interview.title,
            style = LottoMateTheme.typography.title3,
        )

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            LottoMateText(
                text = "인터뷰 ${interview.interviewDate}",
                style = LottoMateTheme.typography.caption2
                    .copy(color = LottoMateGray80)
            )

            LottoMateText(
                text = "|",
                style = LottoMateTheme.typography.caption2
                    .copy(color = LottoMateGray80),
            )

            LottoMateText(
                text = "작성 ${interview.uploadDate}",
                style = LottoMateTheme.typography.caption2
                    .copy(color = LottoMateGray80),
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LottoMateText(
            text = "Q. $question",
            style = LottoMateTheme.typography.headline2
                .copy(color = LottoMateBlack),
        )

        LottoMateText(
            text = answer,
            style = LottoMateTheme.typography.body1
                .copy(color = LottoMateBlack),
        )
    }
}

@Composable
private fun BottomInterviewListContent(
    modifier: Modifier = Modifier,
    interviewList: List<InterviewUiModel>,
    onClickInterviewItem: (Int, String) -> Unit,
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(top = 24.dp)
        ) {
            LottoMateText(
                text = "로또 당첨자 후기",
                style = LottoMateTheme.typography.headline1
                    .copy(color = LottoMateBlack),
            )

            LottoMateText(
                text = "역대 로또 당첨자들의 생생한 후기예요.",
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray80)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
        ) {
            items(
                items = interviewList,
                key = { it.no }
            ) {interview ->
                BottomInterviewListItem(
                    interview = interview,
                    onClick = { onClickInterviewItem(interview.no, interview.place) },
                )
            }
        }
    }
}

@Composable
private fun BottomInterviewListItem(
    modifier: Modifier = Modifier,
    interview: InterviewUiModel,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    LottoMateCard(
        modifier = modifier.size(width = 220.dp, height = 202.dp),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (interview.thumbs.isEmpty()) {
                Image(
                    painter = painterResource(id = interview.emptyThumbs),
                    contentDescription = "interview Thumbnail empty",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                )
            } else {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context).apply {
                        data(interview.thumbs)
                        size(600)
                        scale(Scale.FILL)
                    }
                        .build(),
                    contentDescription = "interview thumbnail image",
                    error = {
                        Image(
                            painter = painterResource(id = interview.emptyThumbs),
                            contentDescription = "interview Thumbnail error")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    LottoMateText(
                        text = interview.place,
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray80)
                    )
                    LottoMateText(
                        text = interview.title,
                        style = LottoMateTheme.typography.label2
                            .copy(color = LottoMateBlack),
                        maxLines = 2,
                        minLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                LottoMateText(
                    text = interview.date,
                    style = LottoMateTheme.typography.caption2
                        .copy(color = LottoMateGray80),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
private fun InterviewPreview() {
    LottoMateTheme {
        InterviewScreen(
            uiState = InterviewUiState.Success(InterviewMockData, InterviewsMockData),
            scrollState = rememberLazyListState(),
            onBackPressed = {},
            onClickBanner = {},
            onClickWinnerInterview = { _, _ -> },
            onClickOriginArticle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LongTitleInterviewPreview() {
    LottoMateTheme {
        InterviewScreen(
            uiState = InterviewUiState.Success(
                InterviewMockData.copy(
                    title = "일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이"
                ),
                InterviewsMockData,
            ),
            scrollState = rememberLazyListState(),
            onBackPressed = {},
            onClickBanner = {},
            onClickWinnerInterview = { _, _ -> },
            onClickOriginArticle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InterviewEmptyImagePreview() {
    LottoMateTheme {
        InterviewScreen(
            uiState = InterviewUiState.Success(InterviewMockData.copy(imgs = emptyList()), InterviewsMockData),
            scrollState = rememberLazyListState(),
            onBackPressed = {},
            onClickBanner = {},
            onClickWinnerInterview = { _, _ -> },
            onClickOriginArticle = {}
        )
    }
}

val InterviewMockData = InterviewDetailUiModel(
    no = 20,
    title = "스피또 1등은 아버지의 성실함 덕분!",
    interviewDate = "2017.04.14",
    uploadDate = "2017.04.14",
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
    place = "스피또 500, 29회차 1등 2억"
)

val InterviewsMockData = List(5) {
    InterviewUiModel(
        no = it,
        title = "인터뷰 목록 데이터 $it",
        thumbs = "",
        emptyThumbs = R.drawable.img_interview_empty01,
        date = "2023.01.01",
        place = "스피또 500, 29회차 1등 2억",
    )
}