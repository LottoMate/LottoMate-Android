package com.lottomate.lottomate.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAB_WIDTH = 40
private const val TAB_CONTAINER_WIDTH = 72
private const val TAB_HEIGHT = 44
private const val TAB_INDICATOR_HEIGHT = 2
private const val TAB_EDGE_PADDING = 20

@Composable
fun LottoMateScrollableTabRow(
    tabState: TabState,
    tabs: List<String>,
) {
    LottoMateScrollableTabRow(
        selectedTabIndex = tabState.currentTabIndex,
        modifier = Modifier.fillMaxWidth(),
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = tabState.currentTabIndex == index,
                onClick = { tabState.currentTabIndex = index },
                selectedContentColor = LottoMateBlack,
                unselectedContentColor = LottoMateGray80,
                modifier = Modifier.wrapContentWidth(),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .size(width = TAB_CONTAINER_WIDTH.dp, height = TAB_HEIGHT.dp)
                            .selectable(
                                selected = tabState.currentTabIndex == index,
                                onClick = { tabState.currentTabIndex = index },
                                enabled = true,
                                role = Role.Tab,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ),
                    ) {
                        Text(
                            text = tab,
                            textAlign = TextAlign.Center,
                            style = if (index == tabState.currentTabIndex) {
                                LottoMateTheme.typography.headline1
                            } else {
                                LottoMateTheme.typography.body1
                            },
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.width(TAB_WIDTH.dp),
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoMateTabRowsPreview() {
    LottoMateTheme {
        LottoMateScrollableTabRow(
            tabState = rememberTabState(),
            tabs = listOf(
                LottoType.S2000.num.toString(),
                LottoType.S1000.num.toString(),
                LottoType.S500.num.toString(),
            )
        )
    }
}

@Composable
private fun LottoMateScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = LottoMateWhite,
    contentColor: Color = LottoMateBlack,
    edgePadding: Dp = TAB_EDGE_PADDING.dp,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        TabRowDefaults.Indicator(
            modifier = Modifier.customTabIndicatorOffset(
                currentTabPosition = tabPositions[selectedTabIndex]
            ),
            height = TAB_INDICATOR_HEIGHT.dp
        )
    },
    divider: @Composable () -> Unit = {
        Divider(
            thickness = 1.dp,
            color = LottoMateGray20,
        )
    },
    tabs: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor
    ) {
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        val scrollableTabData = remember(scrollState, coroutineScope) {
            ScrollableTabData(
                scrollState = scrollState,
                coroutineScope = coroutineScope
            )
        }
        SubcomposeLayout(
            Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.CenterStart)
                .horizontalScroll(scrollState)
                .selectableGroup()
                .clipToBounds()
        ) { constraints ->
            val minTabWidth = 0
            val padding = edgePadding.roundToPx()

            val tabMeasurables = subcompose(TabSlots.Tabs, tabs)

            val layoutHeight = tabMeasurables.fold(initial = 0) { curr, measurable ->
                maxOf(curr, measurable.maxIntrinsicHeight(Constraints.Infinity))
            }

            val tabConstraints = constraints.copy(
                minWidth = minTabWidth,
                minHeight = layoutHeight,
                maxHeight = layoutHeight,
            )
            val tabPlaceables = tabMeasurables
                .map { it.measure(tabConstraints) }

            val layoutWidth = tabPlaceables.fold(initial = padding * 2) { curr, measurable ->
                curr + measurable.width
            }

            // Position the children.
            layout(layoutWidth, layoutHeight) {
                // Place the tabs
                val tabPositions = mutableListOf<TabPosition>()
                var left = padding
                tabPlaceables.forEach {
                    it.placeRelative(left, 0)
                    tabPositions.add(TabPosition(left = left.toDp(), width = it.width.toDp()))
                    left += it.width
                }

                // The divider is measured with its own height, and width equal to the total width
                // of the tab row, and then placed on top of the tabs.
                subcompose(TabSlots.Divider, divider).forEach {
                    val placeable = it.measure(
                        constraints.copy(
                            minHeight = 0,
                            minWidth = layoutWidth,
                            maxWidth = layoutWidth
                        )
                    )
                    placeable.placeRelative(0, layoutHeight - placeable.height)
                }

                // The indicator container is measured to fill the entire space occupied by the tab
                // row, and then placed on top of the divider.
                subcompose(TabSlots.Indicator) {
                    indicator(tabPositions)
                }.forEach {
                    it.measure(Constraints.fixed(layoutWidth, layoutHeight)).placeRelative(0, 0)
                }

                scrollableTabData.onLaidOut(
                    density = this@SubcomposeLayout,
                    edgeOffset = padding,
                    tabPositions = tabPositions,
                    selectedTab = selectedTabIndex
                )
            }
        }
    }
}

private enum class TabSlots {
    Tabs,
    Divider,
    Indicator
}

private class ScrollableTabData(
    private val scrollState: ScrollState,
    private val coroutineScope: CoroutineScope
) {
    private var selectedTab: Int? = null

    fun onLaidOut(
        density: Density,
        edgeOffset: Int,
        tabPositions: List<TabPosition>,
        selectedTab: Int
    ) {
        // Animate if the new tab is different from the old tab, or this is called for the first
        // time (i.e selectedTab is `null`).
        if (this.selectedTab != selectedTab) {
            this.selectedTab = selectedTab
            tabPositions.getOrNull(selectedTab)?.let {
                // Scrolls to the tab with [tabPosition], trying to place it in the center of the
                // screen or as close to the center as possible.
                val calculatedOffset = it.calculateTabOffset(density, edgeOffset, tabPositions)
                if (scrollState.value != calculatedOffset) {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(
                            calculatedOffset,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * @return the offset required to horizontally center the tab inside this TabRow.
     * If the tab is at the start / end, and there is not enough space to fully centre the tab, this
     * will just clamp to the min / max position given the max width.
     */
    private fun TabPosition.calculateTabOffset(
        density: Density,
        edgeOffset: Int,
        tabPositions: List<TabPosition>
    ): Int = with(density) {
        val totalTabRowWidth = tabPositions.last().right.roundToPx() + edgeOffset
        val visibleWidth = totalTabRowWidth - scrollState.maxValue
        val tabOffset = left.roundToPx()
        val scrollerCenter = visibleWidth / 2
        val tabWidth = width.roundToPx()
        val centeredTabOffset = tabOffset - (scrollerCenter - tabWidth / 2)
        // How much space we have to scroll. If the visible width is <= to the total width, then
        // we have no space to scroll as everything is always visible.
        val availableSpace = (totalTabRowWidth - visibleWidth).coerceAtLeast(0)
        return centeredTabOffset.coerceIn(0, availableSpace)
    }
}

@Immutable
private class TabPosition internal constructor(val left: Dp, val width: Dp) {
    val right: Dp get() = left + width

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabPosition) return false

        if (left != other.left) return false
        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + width.hashCode()
        return result
    }

    override fun toString(): String {
        return "TabPosition(left=$left, right=$right, width=$width)"
    }
}

private fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp = 40.dp,
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}