package com.tyranor.next.theme

import androidx.compose.runtime.Composable

/**
 * 历史兼容：曾用于全局去除点击涟漪。界面改为 Material 3 后恢复标准状态层（ripple），
 * 本组件保留为透明包装，避免大面积改动调用点。
 */
@Composable
fun WithoutPressIndication(content: @Composable () -> Unit) {
    content()
}
