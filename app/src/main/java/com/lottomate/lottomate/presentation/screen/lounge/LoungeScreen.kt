package com.lottomate.lottomate.presentation.screen.lounge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.error.LottoMateErrorType
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.BannerType
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.noInteractionClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoungeRoute(
    vm: LoungeViewModel = hiltViewModel(),
    padding: PaddingValues,
    moveToSetting: () -> Unit,
    moveToBanner: (BannerType) -> Unit,
    moveToInterview: (Int, String) -> Unit,
    onShowErrorSnackBar: (errorType: LottoMateErrorType) -> Unit,
) {
    val interviews by vm.interviews.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        vm.errorFlow.collectLatest { error -> onShowErrorSnackBar(error) }
    }

    LoungeScreen(
        modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
        interviews = interviews,
        onClickTopBanner = {},
        onClickSetting = moveToSetting,
        onClickBottomBanner = moveToBanner,
        onClickInterview = moveToInterview,
    )
}

@Composable
private fun LoungeScreen(
    modifier: Modifier = Modifier,
    interviews: List<ResponseInterviewsInfo>,
    onClickSetting: () -> Unit,
    onClickTopBanner: () -> Unit,
    onClickBottomBanner: (BannerType) -> Unit,
    onClickInterview: (Int, String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = Dimens.BaseTopPadding),
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.img_lounge_top),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickTopBanner() },
            )

            InterviewSection(
                modifier = Modifier.padding(top = 36.dp),
                interviews = interviews,
                onClickInterview = onClickInterview,
            )

            BannerCard(
                modifier = Modifier
                    .padding(top = 36.dp, bottom = 32.dp)
                    .padding(horizontal = Dimens.DefaultPadding20),
                type = BannerType.WINNER_GUIDE,
                onClickBanner = { onClickBottomBanner(BannerType.WINNER_GUIDE) },
            )
        }

        LottoMateTopAppBar(
            titleRes = R.string.lounge_title,
            hasNavigation = false,
            actionButtons = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_setting),
                    contentDescription = stringResource(id = R.string.desc_setting_icon),
                    tint = LottoMateGray100,
                    modifier = Modifier.noInteractionClickable { onClickSetting() }
                )
            }
        )
    }
}

@Composable
private fun InterviewSection(
    modifier: Modifier = Modifier,
    interviews: List<ResponseInterviewsInfo>,
    onClickInterview: (Int, String) -> Unit,
) {
    val pagerState = rememberPagerState {
        interviews.size
    }

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
        ) {
            LottoMateText(
                text = stringResource(id = R.string.lounge_interview_text_sub_title),
                style = LottoMateTheme.typography.body1
                    .copy(color = LottoMateGray100),
            )

            LottoMateText(
                text = stringResource(id = R.string.lounge_interview_text_title),
                style = LottoMateTheme.typography.title3,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(236.dp),
            contentPadding = PaddingValues(horizontal = Dimens.DefaultPadding20),
            modifier = Modifier.padding(top = 20.dp),
        ) {page ->
            InterviewItem(
                title = interviews[page].reviewTitle,
                subTitle = interviews[page].reviewPlace,
                thumb = interviews[page].reviewThumb,
                interviewDate = interviews[page].intrvDate,
                onClick = { onClickInterview(page, interviews[page].reviewPlace) },
            )
        }

        if (interviews.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.DefaultPadding20),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(interviews.size) { page ->
                    val color = if (pagerState.currentPage == page) LottoMateRed50 else LottoMateBlack.copy(alpha = 0.2f)

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
private fun InterviewItem(
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                LottoMateText(
                    text = subTitle,
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80)
                )

                LottoMateText(
                    text = title,
                    style = LottoMateTheme.typography.label2,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )

                LottoMateText(
                    text = interviewDate.replace("-", "."),
                    style = LottoMateTheme.typography.caption2
                        .copy(color = LottoMateGray80),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoungeScreenPreview() {
    LottoMateTheme {
        LoungeScreen(
            interviews = listOf(
                ResponseInterviewsInfo(1, 1001, "First Interview", "https://example.com/thumb1.jpg", "2024-01-01", "Seoul"),
                ResponseInterviewsInfo(2, 1002, "Second Interview", "https://example.com/thumb2.jpg", "2024-01-08", "Busan"),
                ResponseInterviewsInfo(3, 1003, "Third Interview", "https://example.com/thumb3.jpg", "2024-01-15", "Incheon"),
                ResponseInterviewsInfo(4, 1004, "Fourth Interview", "https://example.com/thumb4.jpg", "2024-01-22", "Daegu"),
                ResponseInterviewsInfo(5, 1005, "Fifth Interview", "https://example.com/thumb5.jpg", "2024-01-29", "Daejeon"),
                ResponseInterviewsInfo(6, 1006, "Sixth Interview", "https://example.com/thumb6.jpg", "2024-02-05", "Gwangju"),
                ResponseInterviewsInfo(7, 1007, "Seventh Interview", "https://example.com/thumb7.jpg", "2024-02-12", "Suwon"),
                ResponseInterviewsInfo(8, 1008, "Eighth Interview", "https://example.com/thumb8.jpg", "2024-02-19", "Ulsan"),
                ResponseInterviewsInfo(9, 1009, "Ninth Interview", "https://example.com/thumb9.jpg", "2024-02-26", "Jeju"),
                ResponseInterviewsInfo(10, 1010, "Tenth Interview", "https://example.com/thumb10.jpg", "2024-03-04", "Cheongju")
            ),
            onClickTopBanner = {},
            onClickSetting = {},
            onClickBottomBanner = {},
            onClickInterview = { _, _ -> },
        )
    }
}