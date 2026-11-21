package com.tyranor.next.theme.compat

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * 历史 Miuix 取色 / 字体调用的兼容层：全部映射到当前 Material 3 主题，
 * 保证尚未改写的页面也使用 M3 配色与字体。
 */
object MiuixTheme {

    val colorScheme: CompatColorScheme
        @Composable get() = CompatColorScheme(MaterialTheme.colorScheme)

    val textStyles: CompatTextStyles
        @Composable get() = CompatTextStyles
}

class CompatColorScheme(scheme: androidx.compose.material3.ColorScheme) {
    val primary: Color = scheme.primary
    val onPrimary: Color = scheme.onPrimary
    val primaryContainer: Color = scheme.primaryContainer
    val secondary: Color = scheme.secondary
    val secondaryContainer: Color = scheme.secondaryContainer
    val background: Color = scheme.background
    val onBackground: Color = scheme.onBackground
    val surface: Color = scheme.surface
    val onSurface: Color = scheme.onSurface
    val onSurfaceVariant: Color = scheme.onSurfaceVariant
    val onSurfaceContainer: Color = scheme.onSurface
    val onSurfaceVariantSummary: Color = scheme.onSurfaceVariant
    val onSurfaceVariantActions: Color = scheme.onSurfaceVariant
    val error: Color = scheme.error
    val onError: Color = scheme.onError
    val disabledPrimary: Color = scheme.onSurface.copy(alpha = 0.38f)
    val disabledOnPrimary: Color = scheme.surface.copy(alpha = 0.5f)
    val disabledSecondary: Color = scheme.surfaceContainerHighest
    val disabledOnSecondary: Color = scheme.onSurface.copy(alpha = 0.38f)
    val disabledOnSecondaryVariant: Color = scheme.onSurfaceVariant.copy(alpha = 0.5f)
    val disabledOnSurface: Color = scheme.onSurface.copy(alpha = 0.38f)
    val disabledPrimarySlider: Color = scheme.onSurface.copy(alpha = 0.38f)
    val outline: Color = scheme.outline
    val dividerLine: Color = scheme.outlineVariant
    val windowDimming: Color = scheme.scrim.copy(alpha = 0.32f)
}

object CompatTextStyles {
    val main: TextStyle
        @Composable get() = MaterialTheme.typography.bodyLarge
    val body2: TextStyle
        @Composable get() = MaterialTheme.typography.bodyMedium
    val headline1: TextStyle
        @Composable get() = MaterialTheme.typography.titleMedium
}
