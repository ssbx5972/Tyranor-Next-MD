package com.tyranor.next.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonSkippableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tyranor.next.theme.m3.M3Palette
import com.tyranor.next.theme.m3.TyranorTypography
import com.tyranor.next.theme.m3.tyranorShapes

/**
 * 应用主题：完整 Material 3 配色（由「色调轮盘」的种子色生成整套 tonal palette）、
 * M3 字体阶梯与形状阶梯。不再提供玻璃 / 白雾等自定义外观风格。
 */
@Composable
@NonSkippableComposable
fun TyranorNextTheme(
  primaryColor: Color? = null,
  content: @Composable () -> Unit,
) {
  AppThemeColors.ensureLoaded(LocalContext.current)
  // 在函数体内直接读取全局主题色：配合 @NonSkippableComposable，主题色变更时本主题
  // 必然重组并向整棵组合树提供新 colorScheme（不依赖调用点对默认参数的订阅）。
  val seed = primaryColor ?: AppThemeColors.primary
  val dark = AppThemeColors.isDark
  val cornerRadius = AppThemeColors.appCornerRadius
  val colorScheme = remember(seed, dark) { M3Palette.colorScheme(seed, dark) }
  val shapes = remember(cornerRadius) { tyranorShapes(cornerRadius) }
  // 发布给 theme/Color.kt 的兼容色 getter（非组合函数 / 无 CompositionLocal 的调用点）。
  AppThemeColors.publishScheme(colorScheme)
  MaterialTheme(
    colorScheme = colorScheme,
    typography = TyranorTypography,
    shapes = shapes,
    content = content,
  )
}
