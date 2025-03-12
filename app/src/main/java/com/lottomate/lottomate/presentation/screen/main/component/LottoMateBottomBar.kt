package com.lottomate.lottomate.presentation.screen.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.screen.main.MainBottomTab
import com.lottomate.lottomate.presentation.ui.LottoMateBottomTabDivider
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite

private val BOTTOM_BAR_CONTAINER_HEIGHT = 62.dp

@Composable
fun LottoMateBottomBar(
    modifier: Modifier = Modifier,
    tabs: List<MainBottomTab>,
    currentTab: MainBottomTab?,
    onTabSelected: (MainBottomTab) -> Unit,
) {
    Surface(
        color = LottoMateWhite,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(BOTTOM_BAR_CONTAINER_HEIGHT)
        ) {
            Divider(color = LottoMateBottomTabDivider)

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LottoMateWhite),
            ) {
                tabs.forEach { tab ->
                    LottoMateBottomBarItem(
                        modifier = Modifier.weight(1f),
                        tab = tab,
                        selected = tab == currentTab,
                        onClick = { onTabSelected(tab) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LottoMateBottomBarItem(
    modifier: Modifier = Modifier,
    tab: MainBottomTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Icon(
            painter = painterResource(id = tab.icon),
            contentDescription = tab.contentDescription,
            tint = if (selected) LottoMateRed50 else LottoMateGray100,
        )

        LottoMateText(
            text = tab.contentDescription,
            style = LottoMateTheme.typography.caption,
            color = if (selected) LottoMateRed50 else LottoMateGray100,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateBottomBarPreview() {
    LottoMateTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            LottoMateBottomBar(
                tabs = listOf(
                    MainBottomTab.HOME,
                    MainBottomTab.MAP,
                    MainBottomTab.POCKET,
                    MainBottomTab.LOUNGE,
                ),
                currentTab = MainBottomTab.HOME,
                onTabSelected = {}
            )
        }
    }
}