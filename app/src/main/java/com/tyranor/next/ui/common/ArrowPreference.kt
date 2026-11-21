package com.tyranor.next.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.tyranor.next.ui.common.motion.BlurText

/**
 * 设置条目：Material 3 列表行（标题 + 可选摘要 + 起始图标 + 右侧值 / 操作 + 箭头）。
 * 点击态由 M3 ripple 提供；条目本身不自绘背景，由外层 Card / 分组容器提供表面色。
 */
@Composable
fun ArrowPreference(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = Color.Unspecified,
    summary: String? = null,
    summaryColor: Color = Color.Unspecified,
    startAction: @Composable (() -> Unit)? = null,
    endActions: @Composable RowScope.() -> Unit = {},
    bottomAction: (@Composable () -> Unit)? = null,
    insideMargin: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    onClick: (() -> Unit)? = null,
    holdDownState: Boolean = false,
    enabled: Boolean = true,
    /** 右侧当前值（可选）：变化时只替换真正变化的片段并原地模糊，见 [BlurText]。 */
    value: String? = null,
    /** 是否显示右侧箭头；下拉选择行用下拉三角代替箭头，传 false。 */
    showChevron: Boolean = true,
) {
    val resolvedTitleColor = if (titleColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurface
    } else {
        titleColor
    }
    val resolvedSummaryColor = if (summaryColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        summaryColor
    }
    val alpha = if (enabled) 1f else 0.38f
    Column(
        modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(enabled = enabled, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(insideMargin),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (startAction != null) {
                Row(
                    modifier = Modifier.padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) { startAction() }
            }
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = resolvedTitleColor.copy(alpha = resolvedTitleColor.alpha * alpha),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (summary != null) {
                    Spacer(Modifier.height(2.dp))
                    BlurText(
                        summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = resolvedSummaryColor.copy(alpha = resolvedSummaryColor.alpha * alpha),
                        partial = false,
                    )
                }
            }
            Row(
                modifier = Modifier.padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    if (value != null) {
                        BlurText(
                            value,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                            partial = false,
                        )
                    }
                    endActions()
                },
            )
            if (onClick != null && showChevron) {
                BoldChevronRight(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
        bottomAction?.invoke()
    }
}
