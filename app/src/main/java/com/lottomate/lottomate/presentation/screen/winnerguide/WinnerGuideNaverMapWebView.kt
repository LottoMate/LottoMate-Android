package com.lottomate.lottomate.presentation.screen.winnerguide

import android.graphics.Bitmap
import android.util.Log
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lottomate.lottomate.R
import com.lottomate.lottomate.presentation.component.LottoMateBasicWebView
import com.lottomate.lottomate.utils.LocationStateManager
import java.net.URLEncoder

@Composable
fun WinnerGuideNaverMapWebView(
    place: String,
    onBackPressed: () -> Unit,
) {
    val location by LocationStateManager.location.collectAsState()

    val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.d("NaverMapWebView", "onPageStarted: $url")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("NaverMapWebView", "onPageFinished: $url")
        }
    }

    val webChromeClient = object : WebChromeClient() {
        override fun onGeolocationPermissionsShowPrompt(
            origin: String?,
            callback: GeolocationPermissions.Callback?
        ) {
            callback?.invoke(origin, true, false)
        }
    }

    val encodedPlace = URLEncoder.encode(place, "UTF-8")

    LottoMateBasicWebView(
        title = R.string.guide_title,
        url = when {
            place.contains("동행복권") -> "https://m.map.naver.com/search2/search.naver?query=%EB%8F%99%ED%96%89%EB%B3%B5%EA%B6%8C&sm=hty&style=v5#/map/1/1706795753"
            place.contains("농협은행") -> {
                Log.d("NaverMapWebView", "location : $location")
                val coordKey = location?.let { "${it.latitude}:${it.longitude}" } ?: "37.7410651:127.0971133"

                "https://m.map.naver.com/search2/search.naver?query=$encodedPlace&style=v5&sm=clk&centerCoord=$coordKey#/map/1"
            }
            else -> "https://m.map.naver.com/search2/search.naver?query=$encodedPlace&sm=hty&style=v5#/map/1"
        },
        webViewClient = webViewClient,
        webChromeClient = webChromeClient,
        onBackPressed = onBackPressed,
    )
}