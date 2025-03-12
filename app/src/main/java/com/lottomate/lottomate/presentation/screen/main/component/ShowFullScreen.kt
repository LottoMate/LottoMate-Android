package com.lottomate.lottomate.presentation.screen.main.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.lottomate.lottomate.presentation.screen.main.model.FullScreenType
import com.lottomate.lottomate.presentation.screen.map.component.MapLoadingScreen

/**
 * BottomTab 을 덮는 Full Screen이 보여야 할 때 호출됩니다.
 */
@Composable
fun ShowFullScreen(screenType: FullScreenType) {
    when (screenType) {
        FullScreenType.MAP_LOADING -> {
            MapLoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
            )
        }
    }
}