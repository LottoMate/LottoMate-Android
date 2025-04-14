package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.dropShadow
import com.lottomate.lottomate.utils.noInteractionClickable

@Composable
internal fun WinInterviewCardsSection(
    modifier: Modifier = Modifier,
    interviews: List<ResponseInterviewsInfo>,
    onClickInterview: (Int, String) -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { interviews.size }
    )

    Column {
        Column(
            modifier = modifier.padding(horizontal = Dimens.DefaultPadding20),
        ) {
            LottoMateText(
                text = "로또 당첨자 인터뷰",
                style = LottoMateTheme.typography.body1
                    .copy(color = LottoMateGray100),
            )

            LottoMateText(
                text = "로또 1등 기 받아가요.",
                style = LottoMateTheme.typography.title3,
            )
        }

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 20.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = Dimens.DefaultPadding20),
                pageSpacing = 16.dp,
            ) { page ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dropShadow(
                            shape = RoundedCornerShape(16.dp),
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            blur = 8.dp,
                        )
                        .noInteractionClickable { onClickInterview(interviews[page].reviewNo, interviews[page].reviewPlace) },
                    shape = RoundedCornerShape(Dimens.RadiusLarge),
                ) {
                    Column(
                        modifier = Modifier.background(LottoMateWhite)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(interviews[page].reviewThumb)
                                .build(),
                            contentDescription = "Lotto Interview Image",
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.img_review),
                            // TODO : 기본 이미지 변경 필요 
                            error = painterResource(id = R.drawable.img_review),
                            modifier = Modifier
                                .height(160.dp)
                                .fillMaxWidth(),
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 16.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            LottoMateText(
                                text = interviews[page].reviewPlace,
                                style = LottoMateTheme.typography.caption
                                    .copy(color = LottoMateGray80),
                            )

                            LottoMateText(
                                text = interviews[page].reviewTitle,
                                style = LottoMateTheme.typography.headline1,
                                maxLines = 2,
                                minLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            LottoMateText(
                                text = interviews[page].intrvDate.replace("-", "."),
                                style = LottoMateTheme.typography.caption
                                    .copy(color = LottoMateGray80),
                            )
                        }

                    }

                }
            }

            if (interviews.size != 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.DefaultPadding20),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(pagerState.pageCount) { page ->
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
}