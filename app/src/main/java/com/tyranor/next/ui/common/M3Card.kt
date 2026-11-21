package com.tyranor.next.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import com.tyranor.next.theme.AppComponentCornerRadius

/**
 * Material 3 卡片容器：surfaceContainer 填充、色调高度分层。
 * 供设置页 / 工具页的分组卡片统一使用。
 */
@Composable
fun M3Card(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = AppComponentCornerRadius,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    shape: Shape = RoundedCornerShape(cornerRadius),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Column(content = content)
    }
}
