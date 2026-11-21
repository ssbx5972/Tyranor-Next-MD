package com.tyranor.next.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 分类标签栏：Material 3 SecondaryTabRow（等分）或 ScrollableTabRow（可滚动）。
 * 选中项由 M3 标准指示器标记。
 *
 * @param tabs 标签文案列表
 * @param selectedIndex 当前选中下标（越界自动收敛）
 * @param onTabSelected 点击标签回调
 * @param scrollable 是否横向滚动形态
 * @param pager 历史参数（M3 指示器不再使用），保留以兼容调用点
 * @param scrollResetKey 历史参数，保留以兼容调用点
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlidingTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    pager: TabPagerState? = null,
    scrollResetKey: Int = 0,
) {
    if (tabs.isEmpty()) return
    val safeIndex = selectedIndex.coerceIn(0, tabs.lastIndex)
    val tabContent: @Composable (Int, String) -> Unit = { index, label ->
        Tab(
            selected = index == safeIndex,
            onClick = { onTabSelected(index) },
            text = {
                Text(
                    label,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    if (scrollable) {
        ScrollableTabRow(
            selectedTabIndex = safeIndex,
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            edgePadding = 0.dp,
        ) {
            tabs.forEachIndexed { index, label -> tabContent(index, label) }
        }
    } else {
        SecondaryTabRow(
            selectedTabIndex = safeIndex,
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ) {
            tabs.forEachIndexed { index, label -> tabContent(index, label) }
        }
    }
}
