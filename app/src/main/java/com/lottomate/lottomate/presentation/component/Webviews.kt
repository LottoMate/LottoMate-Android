package com.lottomate.lottomate.presentation.component

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import java.net.URLEncoder

@Composable
fun LottoMateNaverMapWebView(
    place: String,
    onBackPressed: () -> Unit,
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
        val encodedPlace = URLEncoder.encode(place, "UTF-8")

        AndroidView(
            modifier = Modifier
                .padding(top = Dimens.BaseTopPadding)
                .fillMaxSize(),
            factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    settings.setSupportZoom(true)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false


                    webViewClient = WebViewClient()

                    if (place.contains("동행복권")) {
                        loadUrl("https://m.map.naver.com/search2/search.naver?query=%EB%8F%99%ED%96%89%EB%B3%B5%EA%B6%8C&sm=hty&style=v5#/map/1/1706795753")
                    } else loadUrl("https://m.map.naver.com/search2/search.naver?query=$encodedPlace&sm=hty&style=v5#/map/1")
                }
            }
        )
        LottoMateTopAppBar(
            titleRes = R.string.guide_title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateWebViewPreview() {
    LottoMateTheme {
        LottoMateNaverMapWebView(
            place = "www.naver.com",
            onBackPressed = {},
        )
    }
}