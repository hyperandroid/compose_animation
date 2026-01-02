package com.spellington.animationtest.gradient

import android.graphics.Shader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spellington.animationtest.gradient.brush.GradientColors
import com.spellington.animationtest.gradient.brush.WavyGradientBrush
import com.spellington.animationtest.util.GradientDomain
import com.spellington.animationtest.util.GradientSamplerOrientation
import com.spellington.animationtest.util.PausableAnimatedTime
import com.spellington.animationtest.util.SamplerFactory
import com.spellington.animationtest.util.samplePalettes

data class WavyGradientEffect(
    val timeScale: Float = .5f,
    val direction: GradientSamplerOrientation = GradientSamplerOrientation.Horizontal,
    val amplitude: Float = .1f,
    val period: Float = 2f,
    val angle: Float = 0f,

    val colors: List<Color> = samplePalettes[0],
    val tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
    val bounds: GradientDomain = 0f to 1f,
    val hardSampler: Boolean = false,
)

val WavyGradientEffects = listOf(
    WavyGradientEffect(
        colors= samplePalettes[1],
        amplitude = .5f,
        period = 6f,
        tileMode = Shader.TileMode.MIRROR,
        angle = (Math.PI * .25).toFloat()
    ),
    WavyGradientEffect(
        colors= samplePalettes[2],
        direction = GradientSamplerOrientation.Vertical,
        amplitude = .1f,
        period = 6f,
        bounds = .45f to .75f,
        tileMode = Shader.TileMode.CLAMP,
        hardSampler = true,
        angle = (Math.PI * .1).toFloat(),
    ),
    WavyGradientEffect(
        colors= samplePalettes[5],
        direction = GradientSamplerOrientation.Vertical,
        amplitude = .1f,
        period = 3f,
        bounds = .25f to 1f,
        tileMode = Shader.TileMode.CLAMP,
        angle = (Math.PI * .1).toFloat(),
    ),
    WavyGradientEffect(
        colors= samplePalettes[3],
        direction = GradientSamplerOrientation.Vertical,
        amplitude = .1f,
        period = 12f,
        bounds = .5f to .75f,
        tileMode = Shader.TileMode.CLAMP,
        hardSampler = true,
        angle = -(Math.PI * .1).toFloat(),
        timeScale = -.3f,
    ),
)

@Composable
fun WavyGradient(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    direction: GradientSamplerOrientation = GradientSamplerOrientation.Horizontal,
    amplitude: Float = .1f,
    period: Float = 4f,
    timeScale: Float = .2f,
    angle: Float = 0f,

    hardSampler: Boolean = false,
    bounds: GradientDomain = 0f to 1f,
    colors: List<Color> = samplePalettes[0],
    tileMode: Shader.TileMode = Shader.TileMode.CLAMP,
    onClick: () -> Unit = {}
) {

    val sampler by remember(hardSampler, colors, tileMode) {
        mutableStateOf(
            SamplerFactory.createSampler(
                orientation = direction,
                bounds = bounds,
                colors = GradientColors(colors),
                tileMode = tileMode,
                hardColorTransition = hardSampler,
            )
        )
    }

    val brush by remember {
        mutableStateOf(
            WavyGradientBrush(
                sampler = sampler,
                amplitude = amplitude,
                period = period,
                timeScale = timeScale,
                angle = angle,
            )
        )
    }

    brush.run {
        setSampler(sampler)
        setTimeScale(timeScale)
        setAmplitude(amplitude)
        setPeriod(period)
        setAngle(angle)
    }

    PausableAnimatedTime(isPaused = !animate) { time ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
                .drawWithCache {

                    onDrawBehind {
                        brush.setTime(time)
                        drawRect(brush)
                    }
                }
        )
    }
}

@Preview
@Composable
fun PreviewWavyGradients() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Make it scrollable
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WavyGradientEffects.forEach { currentEffect ->
            WavyGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .height(200.dp),
                timeScale = currentEffect.timeScale,
                direction = currentEffect.direction,
                amplitude = currentEffect.amplitude,
                period = currentEffect.period,
                angle = currentEffect.angle,

                hardSampler = currentEffect.hardSampler,
                bounds = currentEffect.bounds,
                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
            )
        }

    }
}