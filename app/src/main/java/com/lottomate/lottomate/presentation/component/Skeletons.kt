package com.lottomate.lottomate.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray30
import com.lottomate.lottomate.presentation.ui.LottoMateTheme

@Composable
fun ShimmerSkeleton(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = Dimens.RadiusSmall,
    shimmerColors: List<Color> = listOf(
        LottoMateGray20,
        LottoMateGray30,
        LottoMateGray20
    ),
    shimmerDuration: Int = 1000
) {
    BoxWithConstraints(modifier = modifier) {
        val widthInPx = with(LocalDensity.current) { maxWidth.toPx() }

        val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
        val translateAnim by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = widthInPx * 2, // 더 여유 있게 이동
            animationSpec = infiniteRepeatable(
                animation = tween(shimmerDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "translate"
        )

        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - widthInPx, 0f),
            end = Offset(translateAnim, 0f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush, shape = RoundedCornerShape(cornerRadius))
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ShimmerSkeletonPreview() {
    LottoMateTheme {
        ShimmerSkeleton(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(64.dp)
        )
    }
}