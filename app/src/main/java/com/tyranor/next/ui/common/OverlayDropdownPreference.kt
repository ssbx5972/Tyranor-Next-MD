package com.tyranor.next.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tyranor.next.core.privacy.AppLockController
import com.tyranor.next.ui.common.motion.BlurText
import com.tyranor.next.ui.common.privacy.DismissOnAppLockEffect

/** 下拉项（Material 3 菜单条目）。 */
class DropdownItem(
    val text: String,
    val enabled: Boolean = true,
    val selected: Boolean = false,
    val onClick: (() -> Unit)? = null,
)

/** 下拉条目集合。 */
class DropdownEntry(
    val items: List<DropdownItem>,
    val enabled: Boolean = true,
)

/**
 * 下拉选择条目：Material 3 列表行 + [DropdownMenu]。
 * 右侧显示当前值（原位模糊替换）与下拉箭头。
 */
@Composable
fun OverlayDropdownPreference(
    items: List<String>,
    selectedIndex: Int,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = Color.Unspecified,
    summary: String? = null,
    summaryColor: Color = Color.Unspecified,
    startAction: @Composable (() -> Unit)? = null,
    bottomAction: (@Composable () -> Unit)? = null,
    insideMargin: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    maxHeight: Dp? = null,
    enabled: Boolean = true,
    showValue: Boolean = true,
    renderInRootScaffold: Boolean = true,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    onSelectedIndexChange: ((Int) -> Unit)? = null,
    onLockDismiss: (() -> Unit)? = null,
) {
    val entry = remember(items, selectedIndex, onSelectedIndexChange) {
        DropdownEntry(
            items.mapIndexed { index, item ->
                DropdownItem(
                    text = item,
                    selected = index == selectedIndex,
                    onClick = { onSelectedIndexChange?.invoke(index) },
                )
            },
        )
    }
    OverlayDropdownPreference(
        entry = entry,
        title = title,
        modifier = modifier,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        startAction = startAction,
        bottomAction = bottomAction,
        insideMargin = insideMargin,
        maxHeight = maxHeight,
        enabled = enabled,
        showValue = showValue,
        renderInRootScaffold = renderInRootScaffold,
        onExpandedChange = onExpandedChange,
        onLockDismiss = onLockDismiss,
    )
}

@Composable
fun OverlayDropdownPreference(
    entry: DropdownEntry,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = Color.Unspecified,
    summary: String? = null,
    summaryColor: Color = Color.Unspecified,
    startAction: @Composable (() -> Unit)? = null,
    bottomAction: (@Composable () -> Unit)? = null,
    insideMargin: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    maxHeight: Dp? = null,
    enabled: Boolean = true,
    showValue: Boolean = true,
    renderInRootScaffold: Boolean = true,
    collapseOnSelection: Boolean = true,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    onLockDismiss: (() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val currentOnExpandedChange by rememberUpdatedState(onExpandedChange)
    val actualEnabled = enabled && entry.enabled && entry.items.isNotEmpty()
    val selectedText = entry.items.firstOrNull { it.selected }?.text
    fun setExpanded(show: Boolean) {
        if (expanded != show) {
            expanded = show
            currentOnExpandedChange?.invoke(show)
        }
    }
    val locked by AppLockController.lockedState.collectSettingAsState()
    DismissOnAppLockEffect({ if (expanded) onLockDismiss?.invoke() ?: setExpanded(false) })
    LaunchedEffect(locked) {
        if (locked && expanded) setExpanded(false)
    }
    Box(modifier) {
        ArrowPreference(
            title = title,
            titleColor = titleColor,
            summary = summary,
            summaryColor = summaryColor,
            startAction = startAction,
            bottomAction = bottomAction,
            insideMargin = insideMargin,
            enabled = actualEnabled,
            onClick = { if (actualEnabled) setExpanded(!expanded) },
            showChevron = false,
            endActions = {
                if (showValue && !selectedText.isNullOrEmpty()) {
                    BlurText(
                        selectedText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        partial = false,
                    )
                }
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp),
                )
            },
        )
        if (entry.items.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) },
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Column(
                    Modifier
                        .heightIn(max = maxHeight ?: 400.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    entry.items.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    item.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (item.selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                )
                            },
                            onClick = {
                                if (item.enabled) {
                                    item.onClick?.invoke()
                                    if (collapseOnSelection) setExpanded(false)
                                }
                            },
                            enabled = item.enabled,
                            leadingIcon = if (item.selected) {
                                {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            } else {
                                null
                            },
                        )
                    }
                }
            }
        }
    }
}
