package com.tyranor.next.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.tyranor.next.ui.common.motion.MotionMode
import com.tyranor.next.ui.common.motion.MotionTokens
import com.tyranor.next.ui.common.motion.staggeredEntrance
import kotlinx.coroutines.launch

/** 顶部栏图标按钮（Material Icons 版本）。 */
@Composable
fun TopBarIcon(
    icon: ImageVector,
    contentDescription: String?,
    tint: Color,
    stateDescription: String? = null,
    spin: Boolean = false,
    onClick: () -> Unit,
) {
    TopBarIconImpl(
        imageVector = icon,
        painter = null,
        contentDescription = contentDescription,
        tint = tint,
        stateDescription = stateDescription,
        spin = spin,
        onClick = onClick,
    )
}

/** 顶部栏图标按钮（Painter 版本，保留给仍有自定义图形的调用点）。 */
@Composable
fun TopBarIcon(
    painter: Painter,
    contentDescription: String?,
    tint: Color,
    stateDescription: String? = null,
    spin: Boolean = false,
    onClick: () -> Unit,
) {
    TopBarIconImpl(
        imageVector = null,
        painter = painter,
        contentDescription = contentDescription,
        tint = tint,
        stateDescription = stateDescription,
        spin = spin,
        onClick = onClick,
    )
}

@Composable
private fun TopBarIconImpl(
    imageVector: ImageVector?,
    painter: Painter?,
    contentDescription: String?,
    tint: Color,
    stateDescription: String?,
    spin: Boolean,
    onClick: () -> Unit,
) {
    val spinAngle = remember { Animatable(0f) }
    val spinScope = rememberCoroutineScope()
    val shownTint = if (MotionMode.enhanced) {
        animateColorAsState(tint, MotionTokens.fadeIn(), label = "topBarIconTint").value
    } else {
        tint
    }
    IconButton(
        onClick = {
            if (spin && MotionMode.enhanced) {
                spinScope.launch {
                    spinAngle.animateTo(spinAngle.targetValue + 360f, MotionTokens.layout(0.5f))
                    spinAngle.snapTo(0f)
                }
            }
            onClick()
        },
        modifier = Modifier
            .then(
                if (stateDescription != null) {
                    Modifier.semantics { this.stateDescription = stateDescription }
                } else {
                    Modifier
                },
            )
            .staggeredEntrance(TOP_BAR_ICON_ENTRANCE_INDEX, distance = TopBarEntranceDistance),
    ) {
        val glyphModifier = Modifier.graphicsLayer {
            if (spin && MotionMode.enhanced) rotationZ = spinAngle.value
        }
        if (imageVector != null) {
            Icon(imageVector, contentDescription, glyphModifier, tint = shownTint)
        } else if (painter != null) {
            Icon(painter, contentDescription, glyphModifier, tint = shownTint)
        }
    }
}

private const val TOP_BAR_ICON_ENTRANCE_INDEX = 5
