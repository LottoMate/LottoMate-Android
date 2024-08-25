package com.lottomate.lottomate.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class TabState {
    var currentTabIndex by mutableIntStateOf(0)
}

@Composable
fun rememberTabState() = remember { TabState() }