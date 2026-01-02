package com.spellington.animationtest.gradient

import android.graphics.Shader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.spellington.animationtest.gradient.brush.WavyGradientBrush
import com.spellington.animationtest.gradient.brush.WavyGradientOrientation
import com.spellington.animationtest.util.PausableAnimatedTime
import com.spellington.animationtest.waves.Waves


data class WavyGradientEffect(
    val timeScale: Float = .5f,
    val direction: WavyGradientOrientation = WavyGradientOrientation.Horizontal,
    val amplitude: Float = .1f,
    val period: Float = 2f,
    val colors: List<Color> = palettes[0],
    val tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
) {

    companion object {
        val palettes = arrayOf(

            // candy bar
            listOf(
                Color(0xffff00ff),
                Color.Red,
                Color.Yellow,
                Color.White,
                Color.Cyan,
            ),
            // Sunset Flare
            listOf(
                Color(0xfff8b500), // Bright Yellow
                Color(0xfff2722b), // Orange
                Color(0xffd90429), // Deep Red
                Color(0xff8d0801)  // Dark Burgundy
            ),
// Ocean Breeze
            listOf(
                Color(0xffe0f7fa), // Very Light Cyan
                Color(0xff80deea), // Light Cyan
                Color(0xff00acc1), // Main Cyan
                Color(0xff006064)  // Deep Teal
            ),
// Aurora Dream
            listOf(
                Color(0xff00f5a0), // Bright Mint Green
                Color(0xff00d9e9), // Electric Blue
                Color(0xff8A2BE2), // Blue Violet
                Color(0xff4B0082)  // Indigo
            ),
// Flamingo Dance
            listOf(
                Color(0xffff8fab), // Light Pink
                Color(0xfffb6f92), // Hot Pink
                Color(0xfff72585), // Neon Pink
                Color(0xffb5179e)  // Bright Magenta
            ),
// Forest Moss
            listOf(
                Color(0xffaacc00), // Lime Green
                Color(0xff6b9d02), // Leaf Green
                Color(0xff436400), // Forest Green
                Color(0xff1e2d00)  // modeDeep Forest Green
            ),
        )

    }
}

val WavyGradientEffects = listOf(
    WavyGradientEffect(
        colors=WavyGradientEffect.palettes[0],
        amplitude = .5f,
        period = 6f,
        tileMode = Shader.TileMode.MIRROR
    ),
    WavyGradientEffect(
        colors=WavyGradientEffect.palettes[3],
        direction = WavyGradientOrientation.Vertical,
        amplitude = .5f,
        period = 1f,
    ),
)

@Composable
fun WavyGradient(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    direction: WavyGradientOrientation = WavyGradientOrientation.Horizontal,
    amplitude: Float = .1f,
    period: Float = 2f,
    timeScale: Float = .2f,
    colors: List<Color> = WavyGradientEffect.palettes[0],
    tileMode: Shader.TileMode = Shader.TileMode.CLAMP,
    onClick: () -> Unit = {}
) {

    val sampler by remember(direction, colors, tileMode) {
        mutableStateOf(
            WavyGradientBrush.createSampler(
                orientation = direction,
                GradientColors(colors),
                tileMode
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
                                setAmplitude(amplitude)
                                setPeriod(period)
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
fun PreviewWavyGradients() {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WavyGradientEffects.forEach { currentEffect ->
            WavyGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .width(300.dp),
                timeScale = currentEffect.timeScale,
                direction = currentEffect.direction,
                amplitude = currentEffect.amplitude,
                period = currentEffect.period,
                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
            )
        }
    }
}