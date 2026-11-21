package com.tyranor.next.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/** 弹窗颜色上下文由 AppAlertDialog 提供；历史字段保留以兼容既有调用点。 */
internal val LocalAppDialogContent = androidx.compose.runtime.staticCompositionLocalOf { false }

/**
 * 历史设置页主题包装：界面已统一为 Material 3，这里只提供内容色与无按压反馈，
 * 不再引入 Miuix 主题。
 */
@Composable
fun MiuixSettingsTheme(
    primaryColor: Color? = null,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        WithoutPressIndication(content)
    }
}
