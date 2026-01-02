package com.spellington.animationtest.util

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.spellington.animationtest.gradient.brush.GradientColors

enum class GradientSamplerOrientation {

    Horizontal, Vertical;

    fun toFloat() = if (this == Horizontal)
        1f
    else
        -1f
}

class GradientSampler(
    val sampler: LinearGradient,
    val orientation: GradientSamplerOrientation,
)

object SamplerFactory {

    fun createSampler(
        orientation: GradientSamplerOrientation,
        bounds: Pair<Float, Float> = 0f to 1f,
        colors: GradientColors,
        tileMode: Shader.TileMode,
        hardColorTransition: Boolean = false,
    ): GradientSampler = if (orientation == GradientSamplerOrientation.Horizontal) {
        GradientSampler(
            orientation = GradientSamplerOrientation.Horizontal,
            sampler = createHorizontalSampler(bounds, colors, tileMode, hardColorTransition)
        )
    } else {
        GradientSampler(
            orientation = GradientSamplerOrientation.Vertical,
            sampler = createVerticalSampler(bounds, colors, tileMode, hardColorTransition)
        )
    }

    fun createHorizontalSampler(
        bounds: Pair<Float, Float>,
        colors: GradientColors,
        tileMode: Shader.TileMode,
        hardColorTransition: Boolean,
    ): LinearGradient {

        val (x0, x1) = bounds

        return createSampler(x0, 0f, x1, 0f, colors, tileMode, hardColorTransition)
    }

    fun createVerticalSampler(
        bounds: Pair<Float, Float>,
        colors: GradientColors,
        tileMode: Shader.TileMode,
        hardColorTransition: Boolean,
    ): LinearGradient {
        val (y0, y1) = bounds

        return createSampler(0f, y0, 0f, y1, colors, tileMode, hardColorTransition)
    }

    fun createSampler(
        x0: Float,
        y0: Float,
        x1: Float,
        y1: Float,
        colors: GradientColors,
        tileMode: Shader.TileMode,
        hardColorTransition: Boolean,
    ) = if (hardColorTransition) {
        hardSampler(x0, y0, x1, y1, colors, tileMode)
    } else {
        softSampler(x0, y0, x1, y1, colors, tileMode)
    }

    fun hardSampler(
        x0: Float,
        y0: Float,
        x1: Float,
        y1: Float,
        colors: GradientColors,
        tileMode: Shader.TileMode,
    ): LinearGradient {

        val stops = mutableListOf<Float>()
        val newColors = mutableListOf<Color>()
        val step = 1f / colors.colors.size.toFloat()
        colors.colors.forEachIndexed { index, color ->
            newColors.add(color)
            newColors.add(color)
            stops.add(index * step)
            stops.add((1 + index) * step)
        }

        return LinearGradient(
            x0, y0, x1, y1,
            newColors.map { it.toArgb() }.toIntArray(),
            stops.toFloatArray(),
            tileMode
        )
    }

    fun softSampler(
        x0: Float,
        y0: Float,
        x1: Float,
        y1: Float,
        colors: GradientColors,
        tileMode: Shader.TileMode,
    ) = LinearGradient(
        x0, y0, x1, y1,
        colors.colors.map { it.toArgb() }.toIntArray(),
        null,
        tileMode
    )
}