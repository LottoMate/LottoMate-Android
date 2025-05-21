package com.lottomate.lottomate.presentation.component

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.lottomate.lottomate.R

enum class GifImageResourceType(
    val path: String,
    @DrawableRes val loadingRes: Int,
) {
    MAP_LOADING(
        path = "file:///android_asset/gif_map_loading.gif",
        loadingRes = R.drawable.img_map_loading_placeholder,
    ),
    RANDOM_NUMBER(
        path = "file:///android_asset/gif_pocket_random_number.gif",
        loadingRes = R.drawable.img_pocket_random_number_placeholder,
    )
}

@Composable
fun LottoMateGifImage(
    modifier: Modifier = Modifier,
    gifImageResourceType: GifImageResourceType,
) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context)
            .data(gifImageResourceType.path)
            .size(Size.ORIGINAL)
            .build(),
        imageLoader = imageLoader,
        loading = {
            Image(
                painter = painterResource(id = gifImageResourceType.loadingRes),
                contentDescription = "gif image placeholder",
            )
        },
        contentDescription = "gif image",
    )
}