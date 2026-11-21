package com.tyranor.next.ui.common

import androidx.annotation.IntRange
import androidx.compose.material3.Slider as MaterialSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** 滑动条触感档位（历史参数兼容）。 */
object SliderDefaults {
    enum class SliderHapticEffect { None, Step }
    val DefaultHapticEffect: SliderHapticEffect = SliderHapticEffect.Step
    val MinHeight: Dp = 48.dp
}

/**
 * 滑动条：统一使用 Material 3 [Slider]。
 *
 * 参数保留历史的刻度 / 触感选项签名，供既有页面零改动调用；
 * M3 Slider 通过 [steps] 提供离散刻度与吸附。
 */
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0) steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    height: Dp = SliderDefaults.MinHeight,
    hapticEffect: SliderDefaults.SliderHapticEffect = SliderDefaults.DefaultHapticEffect,
    showKeyPoints: Boolean = false,
    keyPoints: List<Float>? = null,
    magnetThreshold: Float = 0.02f,
) {
    MaterialSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
    )
}
