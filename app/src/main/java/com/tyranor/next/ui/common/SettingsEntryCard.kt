package com.tyranor.next.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tyranor.next.theme.NoticeDotRed
import com.tyranor.next.ui.common.motion.BlurVisibility
import com.tyranor.next.ui.common.motion.bounceOnChange
import com.tyranor.next.ui.common.motion.breathing

/**
 * 单条功能入口卡：Material 3 Card 内承载一行 [ArrowPreference]。
 */
@Composable
internal fun SettingsEntryCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    showBadge: Boolean = false,
    /** 该入口对应的操作正在进行（如检查更新）：图标呼吸并在开始 / 结束时弹一下。 */
    busy: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        ArrowPreference(
            title = title,
            startAction = { SettingsItemIcon(icon, showBadge = showBadge, busy = busy) },
            enabled = enabled,
            onClick = if (enabled) onClick else null,
        )
    }
}

@Composable
private fun SettingsItemIcon(icon: ImageVector, showBadge: Boolean = false, busy: Boolean = false) {
    Box(modifier = Modifier.size(24.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.bounceOnChange(busy, depth = 0.18f).breathing(busy).fillMaxSize(),
        )
        // 红点提醒：固定亮红，不随主题色变化
        BlurVisibility(
            visible = showBadge,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 5.dp, y = (-5).dp)
                .size(8.dp),
        ) {
            Box(Modifier.fillMaxSize().background(NoticeDotRed, CircleShape))
        }
    }
}
