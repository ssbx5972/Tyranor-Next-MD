package com.tyranor.next.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * 平板侧栏：标准 Material 3 [NavigationRail]，选中项为 secondaryContainer 指示器。
 */
@Composable
internal fun AppNavigationRail(
    selectedIndex: Int,
    items: List<LiquidGlassNavItem>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemColors = NavigationRailItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .selectableGroup()
                .semantics { isTraversalGroup = true },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        ) {
            items.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                NavigationRailItem(
                    selected = selected,
                    onClick = { onItemClick(index) },
                    icon = {
                        NavigationTabIcon(
                            icon = item.icon ?: AppIcons.home,
                            unselectedIcon = item.unselectedIcon ?: AppIcons.homeOutlined,
                            contentDescription = item.label,
                            selected = selected,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            animationLabel = "railIconFill$index",
                        )
                    },
                    label = { Text(item.label) },
                    colors = itemColors,
                )
            }
        }
    }
}
