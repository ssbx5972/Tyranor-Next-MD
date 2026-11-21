package com.tyranor.next.ui.common

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Material 3 主题色取色盘：HSV 色轮（色相沿圆周、饱和度沿半径），value 固定为 1。
 * 拖动或点按即回调颜色；中心为白色（无饱和度，调用方按非法主题色处理）。
 */
@Composable
fun M3ColorWheel(
    color: Color,
    onColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hsv = hsvOf(color)
    val hueColors = remember {
        List(361) { index ->
            Color(AndroidColor.HSVToColor(floatArrayOf(index.toFloat(), 1f, 1f)))
        }
    }
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    fun pick(position: Offset) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = minOf(size.width, size.height) / 2f
                        val dx = position.x - center.x
                        val dy = position.y - center.y
                        val dist = sqrt(dx * dx + dy * dy)
                        val saturation = (dist / radius).coerceIn(0f, 1f)
                        var hue = Math.toDegrees(atan2(dy, dx).toDouble()).toFloat()
                        if (hue < 0f) hue += 360f
                        onColorChanged(Color(AndroidColor.HSVToColor(floatArrayOf(hue, saturation, 1f))))
                    }
                    pick(down.position)
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!change.pressed) break
                        change.consume()
                        pick(change.position)
                    }
                }
            },
    ) {
        val radius = minOf(size.width, size.height) / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(brush = Brush.sweepGradient(colors = hueColors, center = center), radius = radius)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Color.Transparent),
                center = center,
                radius = radius,
            ),
            radius = radius,
        )
        val angle = Math.toRadians(hsv[0].toDouble())
        val indicator = Offset(
            center.x + (cos(angle) * radius * hsv[1]).toFloat(),
            center.y + (sin(angle) * radius * hsv[1]).toFloat(),
        )
        drawCircle(Color.Black.copy(alpha = 0.5f), radius = 12.dp.toPx(), center = indicator, style = Stroke(3.dp.toPx()))
        drawCircle(Color.White, radius = 12.dp.toPx(), center = indicator, style = Stroke(1.5.dp.toPx()))
    }
}

private fun hsvOf(color: Color): FloatArray {
    val hsv = FloatArray(3)
    AndroidColor.RGBToHSV(
        (color.red * 255).toInt(),
        (color.green * 255).toInt(),
        (color.blue * 255).toInt(),
        hsv,
    )
    if (hsv[2] < 0.01f) hsv[1] = 0f
    return hsv
}
