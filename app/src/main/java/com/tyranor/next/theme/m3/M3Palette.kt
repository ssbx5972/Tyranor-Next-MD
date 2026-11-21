package com.tyranor.next.theme.m3

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Material 3 色板：从种子色生成完整的明/暗 [ColorScheme]。
 *
 * 采用 CIELAB 的 L*（感知亮度）作为 tone、LCh 的 C/h 作为色度与色相，按 Material 3
 * 的角色表取色。与官方 HCT 的差异只在色相/色度的感知度量上，实际观感与 M3 预设一致：
 * 同一色相下 tone 40/80 作为主色对，90/10 作为容器对，中性色承担背景与表面层级。
 */
object M3Palette {

    /** 主色板色度：官方对种子色取 max(48, seedChroma)（上限 60，避免荧光主色）。 */
    private const val PRIMARY_CHROMA_MIN = 48f
    private const val PRIMARY_CHROMA_MAX = 60f

    /** 次色板：低饱和，承担次级操作与低强调容器。 */
    private const val SECONDARY_CHROMA = 16f

    /** 第三色板：色相旋转 60°，承担点缀与高亮。 */
    private const val TERTIARY_CHROMA = 24f
    private const val TERTIARY_HUE_SHIFT = 60f

    /** 中性色板：背景、表面与容器层级。 */
    private const val NEUTRAL_CHROMA = 4f
    private const val NEUTRAL_VARIANT_CHROMA = 8f

    /** 错误色：M3 固定色相 25°、高色度，不随种子色变化。 */
    private const val ERROR_HUE = 25f
    private const val ERROR_CHROMA = 84f

    private class TonalPalette(val hue: Float, val chroma: Float) {
        /** 取 tone（L*）与色相下能落进 sRGB 色域的最大色度，返回不透明颜色。 */
        fun tone(tone: Int): Color {
            val l = tone.coerceIn(0, 100).toFloat()
            if (l <= 0f) return Color.Black
            if (l >= 100f) return Color.White
            var low = 0f
            var high = chroma
            var best = labToColor(l, 0f, hue)
            repeat(20) {
                val mid = (low + high) / 2f
                val (r, g, b) = labToLinear(l, mid, hue)
                if (inGamut(r, g, b)) {
                    best = colorFromLinear(r, g, b)
                    low = mid
                } else {
                    high = mid
                }
            }
            return best
        }
    }

    fun colorScheme(seed: Color, dark: Boolean): ColorScheme {
        val lab = seed.toLab()
        val hue = if (lab.chroma < 1f) 220f else lab.hue
        val primary = TonalPalette(
            hue = hue,
            chroma = lab.chroma.coerceIn(PRIMARY_CHROMA_MIN, PRIMARY_CHROMA_MAX),
        )
        val secondary = TonalPalette(hue, SECONDARY_CHROMA)
        val tertiary = TonalPalette((hue + TERTIARY_HUE_SHIFT) % 360f, TERTIARY_CHROMA)
        val neutral = TonalPalette(hue, NEUTRAL_CHROMA)
        val neutralVariant = TonalPalette(hue, NEUTRAL_VARIANT_CHROMA)
        val error = TonalPalette(ERROR_HUE, ERROR_CHROMA)

        return if (dark) {
            darkColorScheme(
                primary = primary.tone(80),
                onPrimary = primary.tone(20),
                primaryContainer = primary.tone(30),
                onPrimaryContainer = primary.tone(90),
                inversePrimary = primary.tone(40),
                secondary = secondary.tone(80),
                onSecondary = secondary.tone(20),
                secondaryContainer = secondary.tone(30),
                onSecondaryContainer = secondary.tone(90),
                tertiary = tertiary.tone(80),
                onTertiary = tertiary.tone(20),
                tertiaryContainer = tertiary.tone(30),
                onTertiaryContainer = tertiary.tone(90),
                background = neutral.tone(10),
                onBackground = neutral.tone(90),
                surface = neutral.tone(10),
                onSurface = neutral.tone(90),
                surfaceVariant = neutralVariant.tone(30),
                onSurfaceVariant = neutralVariant.tone(80),
                inverseSurface = neutral.tone(90),
                inverseOnSurface = neutral.tone(20),
                error = error.tone(80),
                onError = error.tone(20),
                errorContainer = error.tone(30),
                onErrorContainer = error.tone(90),
                outline = neutralVariant.tone(60),
                outlineVariant = neutralVariant.tone(30),
                scrim = Color.Black,
                surfaceBright = neutral.tone(24),
                surfaceDim = neutral.tone(6),
                surfaceContainerLowest = neutral.tone(4),
                surfaceContainerLow = neutral.tone(10),
                surfaceContainer = neutral.tone(12),
                surfaceContainerHigh = neutral.tone(17),
                surfaceContainerHighest = neutral.tone(22),
                surfaceTint = primary.tone(80),
            )
        } else {
            lightColorScheme(
                primary = primary.tone(40),
                onPrimary = primary.tone(100),
                primaryContainer = primary.tone(90),
                onPrimaryContainer = primary.tone(10),
                inversePrimary = primary.tone(80),
                secondary = secondary.tone(40),
                onSecondary = secondary.tone(100),
                secondaryContainer = secondary.tone(90),
                onSecondaryContainer = secondary.tone(10),
                tertiary = tertiary.tone(40),
                onTertiary = tertiary.tone(100),
                tertiaryContainer = tertiary.tone(90),
                onTertiaryContainer = tertiary.tone(10),
                background = neutral.tone(99),
                onBackground = neutral.tone(10),
                surface = neutral.tone(99),
                onSurface = neutral.tone(10),
                surfaceVariant = neutralVariant.tone(90),
                onSurfaceVariant = neutralVariant.tone(30),
                inverseSurface = neutral.tone(20),
                inverseOnSurface = neutral.tone(95),
                error = error.tone(40),
                onError = error.tone(100),
                errorContainer = error.tone(90),
                onErrorContainer = error.tone(10),
                outline = neutralVariant.tone(50),
                outlineVariant = neutralVariant.tone(80),
                scrim = Color.Black,
                surfaceBright = neutral.tone(98),
                surfaceDim = neutral.tone(87),
                surfaceContainerLowest = neutral.tone(100),
                surfaceContainerLow = neutral.tone(96),
                surfaceContainer = neutral.tone(94),
                surfaceContainerHigh = neutral.tone(92),
                surfaceContainerHighest = neutral.tone(90),
                surfaceTint = primary.tone(40),
            )
        }
    }

    // ===== sRGB <-> CIELAB(D65) =====

    private val WhiteX = 0.95047f
    private val WhiteY = 1.0f
    private val WhiteZ = 1.08883f
    private const val Epsilon = 216f / 24389f
    private const val Kappa = 24389f / 27f

    private fun gammaExpand(c: Float): Float =
        if (c <= 0.04045f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)

    private fun gammaCompress(c: Float): Float =
        if (c <= 0.0031308f) 12.92f * c else 1.055f * c.pow(1f / 2.4f) - 0.055f

    private fun fLab(t: Float): Float =
        if (t > Epsilon) t.pow(1f / 3f) else (Kappa * t + 16f) / 116f

    private fun fLabInv(t: Float): Float {
        val t3 = t * t * t
        return if (t3 > Epsilon) t3 else (116f * t - 16f) / Kappa
    }

    private class Lab(val l: Float, val a: Float, val b: Float) {
        val chroma: Float get() = sqrt(a * a + b * b)
        val hue: Float
            get() {
                val deg = Math.toDegrees(atan2(b.toDouble(), a.toDouble())).toFloat()
                return if (deg < 0f) deg + 360f else deg
            }
    }

    private fun Color.toLab(): Lab {
        val r = gammaExpand(red)
        val g = gammaExpand(green)
        val b = gammaExpand(blue)
        val x = (0.4124564f * r + 0.3575761f * g + 0.1804375f * b) / WhiteX
        val y = (0.2126729f * r + 0.7151522f * g + 0.0721750f * b) / WhiteY
        val z = (0.0193339f * r + 0.1191920f * g + 0.9503041f * b) / WhiteZ
        val fx = fLab(x)
        val fy = fLab(y)
        val fz = fLab(z)
        return Lab(
            l = 116f * fy - 16f,
            a = 500f * (fx - fy),
            b = 200f * (fy - fz),
        )
    }

    private fun labToLinear(l: Float, c: Float, hueDeg: Float): Triple<Float, Float, Float> {
        val rad = Math.toRadians(hueDeg.toDouble())
        val a = (c * cos(rad)).toFloat()
        val b = (c * sin(rad)).toFloat()
        val fy = (l + 16f) / 116f
        val fx = fy + a / 500f
        val fz = fy - b / 200f
        val x = WhiteX * fLabInv(fx)
        val y = WhiteY * fLabInv(fy)
        val z = WhiteZ * fLabInv(fz)
        val r = 3.2404542f * x - 1.5371385f * y - 0.4985314f * z
        val g = -0.9692660f * x + 1.8760108f * y + 0.0415560f * z
        val bb = 0.0556434f * x - 0.2040259f * y + 1.0572252f * z
        return Triple(r, g, bb)
    }

    private fun inGamut(r: Float, g: Float, b: Float): Boolean =
        r >= -0.0005f && r <= 1.0005f &&
            g >= -0.0005f && g <= 1.0005f &&
            b >= -0.0005f && b <= 1.0005f

    private fun colorFromLinear(r: Float, g: Float, b: Float): Color = Color(
        red = gammaCompress(r).coerceIn(0f, 1f),
        green = gammaCompress(g).coerceIn(0f, 1f),
        blue = gammaCompress(b).coerceIn(0f, 1f),
    )

    private fun labToColor(l: Float, c: Float, hueDeg: Float): Color {
        val (r, g, b) = labToLinear(l, c, hueDeg)
        return colorFromLinear(r, g, b)
    }

    /** 以 tone 取纯中性灰（无色度），用于分隔线与占位。 */
    fun neutralTone(tone: Int): Color = labToColor(tone.toFloat(), 0f, 0f)

    /** 便捷：判断两色是否近似（主题切换时避免重复发布）。 */
    fun Color.approxEquals(other: Color): Boolean =
        abs(red - other.red) < 0.002f &&
            abs(green - other.green) < 0.002f &&
            abs(blue - other.blue) < 0.002f
}
