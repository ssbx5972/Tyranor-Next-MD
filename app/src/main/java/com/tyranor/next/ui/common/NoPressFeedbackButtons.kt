package com.tyranor.next.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tyranor.next.theme.AppComponentShape
import com.tyranor.next.ui.common.motion.pressSquish
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.draw.drawBehind
import com.tyranor.next.ui.common.motion.MotionMode
import com.tyranor.next.ui.common.motion.MotionTokens

/**
 * 页内无按压反馈按钮（配合 AGENTS.md「弹窗点击反馈统一规范」的同一意图）。
 *
 * `AppScreenScaffold` 的 `WithoutPressIndication` 只影响走 `LocalIndication` 的组件；
 * Material3 的 `Button` / `TextButton` 内部显式使用 `ripple()`，不经过 `LocalIndication`，
 * 因此页内需要「无按压反馈」的按钮必须使用本文件组件（`indication = null`）。
 */

/** 页内文本按钮：无涟漪，颜色随可用态变化（正文档 bodyMedium）。 */
@Composable
fun NoRippleTextButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    /** 可用态文字色；破坏性动作（删除/清除）传 `MaterialTheme.colorScheme.error`。 */
    contentColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
) {
    val targetColor = if (enabled) {
        contentColor
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    }
    NoRippleActionText(
        text, modifier, enabled, targetColor,
        verticalPadding = 8.dp, colorLabel = "noRippleTextButtonColor", onClick = onClick,
    )
}

/** 页内与弹窗文本按钮共用文字过渡、按压反馈与点击区域；各自决定颜色、留白和点击动作。 */
@Composable
internal fun NoRippleActionText(
    text: String,
    modifier: Modifier,
    enabled: Boolean,
    targetColor: Color,
    verticalPadding: Dp,
    colorLabel: String,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = targetColor),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * 页内填充按钮：无涟漪/按压效果。
 * [tonal] = true 时为浅色底（[fillColor] 的 14%）+ 同色文字，用于次级动作（如「前往授权」）。
 */
@Composable
fun NoRippleButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tonal: Boolean = false,
    leadingIcon: Painter? = null,
    leadingIconVector: ImageVector? = null,
    iconContentDescription: String? = null,
    fillColor: Color = MaterialTheme.colorScheme.primary,
    maxLines: Int = 1,
    onClick: () -> Unit,
) {
    val container = when {
        tonal -> MaterialTheme.colorScheme.secondaryContainer
        else -> fillColor
    }
    val contentColor = when {
        tonal -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> Color.White
    }
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = contentColor,
        ),
    ) {
        if (leadingIconVector != null) {
            Icon(
                imageVector = leadingIconVector,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
        } else {
            leadingIcon?.let { painter ->
                Icon(
                    painter = painter,
                    contentDescription = iconContentDescription,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
