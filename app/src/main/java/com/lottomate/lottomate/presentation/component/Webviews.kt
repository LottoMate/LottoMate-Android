package com.lottomate.lottomate.presentation.component

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

@Composable
fun LottoMateBasicWebView(
    @StringRes title: Int,
    url: String,
    webViewClient: WebViewClient = WebViewClient(),
    webChromeClient: WebChromeClient = WebChromeClient(),
    onBackPressed: () -> Unit,
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LottoMateWhite),
    ) {
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

                    clearCache(true) // 캐시 제거
                    clearHistory()   // 기록 제거
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE // 캐시 사용하지 않도록 설정

                    this.webViewClient = webViewClient
                    this.webChromeClient = webChromeClient

                    loadUrl(url)
                }
            },
        )

        LottoMateTopAppBar(
            titleRes = title,
            hasNavigation = true,
            onBackPressed = onBackPressed,
        )
    }
}