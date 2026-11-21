package com.tyranor.next.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** 弹窗内滑动条：Material 3 Slider（0..1）。 */
@Composable
internal fun DialogMiuixSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Slider(
        value = value.coerceIn(0f, 1f),
        onValueChange = { onValueChange(it.coerceIn(0f, 1f)) },
        valueRange = 0f..1f,
        modifier = modifier.fillMaxWidth(),
    )
}
