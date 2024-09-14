package com.lottomate.lottomate.presentation.screen.review

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTopAppBar
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray120
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateTransparent
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow

@Composable
fun InterviewRoute(
    vm: InterviewViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBackPressed: () -> Unit,
) {
    InterviewContent(
        onClickBanner = {}
    )
}

@Composable
private fun InterviewScreen() {

}

@Composable
private fun InterviewContent(
    onClickBanner: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(LottoMateWhite)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            InterviewImageSection(
                modifier = Modifier.fillMaxWidth()
            )

            LottoMateTopAppBar(
                titleRes = R.string.top_app_bar_empty_title,
                backgroundColor = LottoMateTransparent,
                hasNavigation = true,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            InterviewContent(
                modifier = Modifier.fillMaxWidth()
            )
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 10.dp,
            color = LottoMateGray20
        )

        BottomInterviewListContent(
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        BannerCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClickBanner = onClickBanner
        )

        Spacer(modifier = Modifier.height(98.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun InterviewImageSection(
    modifier: Modifier = Modifier,
) {
    GlideImage(
        model = painterResource(id = R.drawable.img_review),
        contentDescription = "Lotto Interview Image",
        loading = placeholder(painterResource(id = R.drawable.img_review)),
        contentScale = ContentScale.Crop,
        modifier = modifier.height(280.dp),
    )
}

@Composable
private fun InterviewContent(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LottoMateText(
                text = "NNN회차",
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray120)
            )
            Spacer(modifier = Modifier.width(4.dp))
            LottoMateText(
                text = "•",
                textAlign = TextAlign.Center,
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray120)
            )
            Spacer(modifier = Modifier.width(4.dp))
            LottoMateText(
                text = "연금복권 1등 NN억",
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray120)
            )
        }

        LottoMateText(
            text = "사회 초년생 시절부터 꾸준히\n구매해서 1등 당첨!",
            style = LottoMateTheme.typography.title3
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            LottoMateText(
                text = "인터뷰 2024.06.29",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray80)
            )
            Spacer(modifier = Modifier.width(8.dp))
            LottoMateText(
                text = "|",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray80)
            )
            Spacer(modifier = Modifier.width(8.dp))
            LottoMateText(
                text = "작성 2024.07.01",
                style = LottoMateTheme.typography.caption
                    .copy(color = LottoMateGray80)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        InterviewContentQnA(
            question = stringResource(id = R.string.interview_exam_question),
            answer = stringResource(id = R.string.interview_exam_answer)
        )

        Spacer(modifier = Modifier.height(28.dp))

        InterviewContentQnA(
            question = stringResource(id = R.string.interview_exam_question),
            answer = stringResource(id = R.string.interview_exam_answer)
        )

        Spacer(modifier = Modifier.height(28.dp))

        InterviewContentQnA(
            question = stringResource(id = R.string.interview_exam_question),
            answer = stringResource(id = R.string.interview_exam_answer)
        )

        Spacer(modifier = Modifier.height(28.dp))

        InterviewContentQnA(
            question = stringResource(id = R.string.interview_exam_question),
            answer = stringResource(id = R.string.interview_exam_answer)
        )

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

            Row(verticalAlignment = Alignment.CenterVertically) {
                LottoMateText(
                    text = "원문 보러가기",
                    style = LottoMateTheme.typography.caption
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

        Spacer(modifier = Modifier.height(24.dp))
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


        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
            .horizontalScroll(rememberScrollState())
        ) {
            repeat(5) {
                BottomInterviewListItem()
                if (it != 4) Spacer(modifier = Modifier.width(16.dp))
                else Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun BottomInterviewListItem(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .size(width = 220.dp, height = 200.dp)
            .dropShadow(
                shape = RoundedCornerShape(Dimens.RadiusLarge),
                color = LottoMateBlack.copy(alpha = 0.16f),
                blur = 8.dp,
                offsetX = 0.dp,
                offsetY = 0.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = LottoMateWhite,
        ),
        shape = RoundedCornerShape(Dimens.RadiusLarge),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            GlideImage(
                model = painterResource(id = R.drawable.img_review),
                contentDescription = "",
                loading = placeholder(painterResource(id = R.drawable.img_review)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(101.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 13.dp, bottom = 6.dp)
            ) {
                LottoMateText(
                    text = "연금복권 1등",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80)
                )
                LottoMateText(
                    text = "사회 초년생 시절부터 꾸준히 구매해서 1등 당첨",
                    style = LottoMateTheme.typography.label2,
                )
                LottoMateText(
                    text = "YYYY.MM.DD",
                    style = LottoMateTheme.typography.caption
                        .copy(color = LottoMateGray80)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoReviewScreenPreview() {
    LottoMateTheme {
        InterviewContent(
            onClickBanner = {}
        )
    }
}