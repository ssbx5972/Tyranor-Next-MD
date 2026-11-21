package com.tyranor.next.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 导航项图标：Material Icons，选中态用填充图标 + 自上而下的染色动画。
 * 未选中使用 [unselectedIcon]（通常为 Outlined），选中使用 [icon]（Filled）。
 */
@Composable
internal fun NavigationTabIcon(
    icon: ImageVector,
    unselectedIcon: ImageVector,
    contentDescription: String?,
    selected: Boolean,
    unselectedColor: Color,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    animationLabel: String = "navIconFill",
) {
    val fill by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = animationLabel,
    )
    Box(modifier.size(size)) {
        Icon(
            imageVector = unselectedIcon,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = unselectedColor,
        )
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    clip = true
                }
                .drawWithCache {
                    onDrawWithContent {
                        val edge = 1f - fill
                        val mask = Brush.verticalGradient(
                            colorStops = arrayOf(edge to Color.Transparent, edge to Color.White),
                        )
                        drawContent()
                        drawRect(brush = mask, blendMode = BlendMode.DstIn)
                    }
                },
            tint = selectedColor,
        )
    }
}

/** 兼容旧调用：drawable 资源版本（仍用于尚未替换的自定义图标）。 */
@Composable
internal fun NavigationTabIcon(
    @DrawableRes iconRes: Int,
    contentDescription: String?,
    selected: Boolean,
    unselectedColor: Color,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    animationLabel: String = "navIconFill",
) {
    val fill by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = animationLabel,
    )
    val painter: Painter = painterResource(iconRes)
    Box(modifier.size(size)) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(unselectedColor),
        )
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    clip = true
                }
                .drawWithCache {
                    onDrawWithContent {
                        val edge = 1f - fill
                        val mask = Brush.verticalGradient(
                            colorStops = arrayOf(edge to Color.Transparent, edge to Color.White),
                        )
                        drawContent()
                        drawRect(brush = mask, blendMode = BlendMode.DstIn)
                    }
                },
            colorFilter = ColorFilter.tint(selectedColor),
        )
    }
}
