package com.lottomate.lottomate.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.ShimmerSkeleton
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray20

@Composable
fun HomeShimmerSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        ShimmerSkeleton(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .fillMaxWidth()
                .height(64.dp)
        )

        ShimmerSkeleton(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(top = 41.dp)
                .size(width = 41.dp, height = 18.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(top = 14.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ShimmerSkeleton(
                modifier = Modifier
                    .size(width = 28.dp, height = 18.dp)
            )

            ShimmerSkeleton(
                modifier = Modifier
                    .size(width = 54.dp, height = 18.dp)
            )

            ShimmerSkeleton(
                modifier = Modifier
                    .size(width = 40.dp, height = 18.dp)
            )
        }

        HorizontalDivider(
            color = LottoMateGray20,
            thickness = 1.dp,
        )

        Row(
            modifier = Modifier
                .padding(Dimens.DefaultPadding20)
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(13.dp, Alignment.CenterHorizontally),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = "home win result shimmer left arrow icon",
                tint = LottoMateGray20,
                modifier = Modifier.padding(top = 102.dp),
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ShimmerSkeleton(
                    modifier = Modifier
                        .size(width = 212.dp, height = 38.dp)
                )

                ShimmerSkeleton(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(width = 175.dp, height = 62.dp)
                )

                ShimmerSkeleton(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(width = 255.dp, height = 28.dp)
                )

                ShimmerSkeleton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(width = 295.dp, height = 28.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = "home win result shimmer right arrow icon",
                tint = LottoMateGray20,
                modifier = Modifier.padding(top = 102.dp),
            )
        }

        ShimmerSkeleton(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(top = 32.dp)
                .size(width = 335.dp, height = 46.dp)
        )

        ShimmerSkeleton(
            modifier = Modifier
                .padding(top = 42.dp, start = Dimens.DefaultPadding20)
                .size(width = 151.dp, height = 18.dp)
        )

        Row(
            modifier = Modifier
                .padding(horizontal = Dimens.DefaultPadding20)
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            repeat(2) {
                ShimmerSkeleton(
                    modifier = Modifier
                        .size(width = 160.dp, height = 124.dp)
                )
            }
        }
    }
}