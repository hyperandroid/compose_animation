package com.spellington.animationtest.gradient

import android.graphics.Shader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellington.animationtest.gradient.brush.GradientColors
import com.spellington.animationtest.gradient.brush.HatchGradientBrush
import com.spellington.animationtest.util.GradientDomain
import com.spellington.animationtest.util.GradientSamplerOrientation
import com.spellington.animationtest.util.PausableAnimatedTime
import com.spellington.animationtest.util.SamplerFactory
import com.spellington.animationtest.util.samplePalettes
import com.spellington.animationtest.waves.Waves

data class HatchGradientEffect(
    val timeScale: Float = .5f,
    val direction: GradientSamplerOrientation = GradientSamplerOrientation.Horizontal,
    val amplitude: Float = .1f,
    val peaks: Float = 4f,

    val colors: List<Color> = samplePalettes[0],
    val tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
    val bounds: GradientDomain = 0f to 1f,
    val hardSampler: Boolean = false,
)

val HatchGradientEffects = listOf(
    HatchGradientEffect(
        colors= samplePalettes[1],
        amplitude = .5f,
        peaks = 8f,
        tileMode = Shader.TileMode.MIRROR,
        direction = GradientSamplerOrientation.Vertical,
    ),
    HatchGradientEffect(
        colors= samplePalettes[1],
        amplitude = .5f,
        peaks = 10f,
        tileMode = Shader.TileMode.MIRROR,
        direction = GradientSamplerOrientation.Horizontal,
        hardSampler = true,
    ),
)

@Composable
fun HatchGradient(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    direction: GradientSamplerOrientation = GradientSamplerOrientation.Horizontal,
    timeScale: Float = .2f,
    amplitude: Float = .1f,
    peaks: Float = 4f,

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
            HatchGradientBrush(
                sampler = sampler,
                timeScale = timeScale,
                amplitude = amplitude,
                peaks = peaks,
            )
        )
    }

    PausableAnimatedTime(isPaused = !animate) { time ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {

                        onDrawBehind {
                            brush.run {
                                setSampler(sampler)
                                setTimeScale(timeScale)
                                setTime(time)
                            }
                            drawRect(brush)
                        }
                    }
            )

            Waves(
                modifier = Modifier.align(Alignment.BottomCenter),
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        Text(
                            text = "Tap Me",
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            fontSize = 60.sp,
                            color = Color.White,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    blurRadius = 10f
                                )
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }
                })
        }
    }
}

@Preview
@Composable
fun PreviewHatchGradients() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Make it scrollable
        //verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FlowRow(
            modifier = Modifier.padding(8.dp),
            // You can specify the arrangement for items on the main axis (horizontal)
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            // and the arrangement for items on the cross axis (vertical)
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HatchGradientEffects.forEach { currentEffect ->
                HatchGradient(
                    modifier = Modifier

                        .width(200.dp)
                        .height(300.dp),
                    timeScale = currentEffect.timeScale,
                    direction = currentEffect.direction,
                    amplitude = currentEffect.amplitude,
                    peaks = currentEffect.peaks * 2,

                    hardSampler = currentEffect.hardSampler,
                    bounds = currentEffect.bounds,
                    colors = currentEffect.colors,
                    tileMode = currentEffect.tileMode,
                )
            }
        }
    }
}