package com.spellington.animationtest.gradient

import android.graphics.Shader
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spellington.animationtest.R
import com.spellington.animationtest.gradient.brush.GradientColors
import com.spellington.animationtest.gradient.brush.SpiralGradientBrush
import com.spellington.animationtest.gradient.brush.SpiralGradientDirection
import com.spellington.animationtest.util.GradientSamplerOrientation
import com.spellington.animationtest.util.PausableAnimatedTime
import com.spellington.animationtest.util.SamplerFactory
import com.spellington.animationtest.util.samplePalettes


data class SpiralGradientPreset(
    val timeScale: Float = .5f,
    val spiralThreshold: Float = 2f,
    val direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    val center: Offset = Offset(.5f, .5f),

    val hardSampler: Boolean = false,
    val colors: List<Color> = samplePalettes[0],
    val tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
)

val SpiralGradientPresets = listOf(
    SpiralGradientPreset(
        spiralThreshold = 4f,
        hardSampler = true,
    ),
    SpiralGradientPreset(
        timeScale = .1f,
        spiralThreshold = 1f,
        colors = samplePalettes[1],
        direction = SpiralGradientDirection.In,
    ),
    SpiralGradientPreset(
        center = Offset(.25f, .25f),
        spiralThreshold = 4f,
        colors = samplePalettes[5],
        hardSampler = true,
    ),
    SpiralGradientPreset(
        timeScale = .1f,
        spiralThreshold = 2f,
        colors = samplePalettes[4],
        center = Offset(.75f, .75f),
        direction = SpiralGradientDirection.In,
    ),
)

@Composable
fun SpiralGradient(
    modifier: Modifier = Modifier,
    @DrawableRes drawable:  Int = R.drawable.cheshire_cat,
    animate: Boolean = true,
    timeScale: Float = .5f,
    spiralThreshold: Float = 2f,
    direction: SpiralGradientDirection = SpiralGradientDirection.In,

    hardSampler: Boolean = false,
    center: Offset = Offset(.5f, .5f),
    colors: List<Color> = samplePalettes[0],
    tileMode: Shader.TileMode = Shader.TileMode.MIRROR,

    onClick: () -> Unit = {},
) {

    val sampler by remember(hardSampler, colors, tileMode) {
        mutableStateOf(

            SamplerFactory.createSampler(
                orientation = GradientSamplerOrientation.Horizontal,
                colors = GradientColors(colors),
                tileMode = tileMode,
                hardColorTransition = hardSampler,
            )
        )
    }

    val brush by remember {
        mutableStateOf(
            SpiralGradientBrush(
                sampler = sampler.sampler,
                direction = direction,
                spiralThreshold = spiralThreshold,
                timeScale = timeScale,
                center = center,
            )
        )
    }

    brush.run {
        setSampler(sampler.sampler)
        setCenter(center)
        setDirection(direction)
        setSpiralThreshold(spiralThreshold)
        setTimeScale(timeScale)
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
                            brush.setTime(time)
                            drawRect(brush)
                        }
                    }
            )

            /*
            val resources = LocalContext.current.resources
            val imageBitmap = ImageBitmap.imageResource(resources, drawable)


            Waves(
                modifier = Modifier.align(Alignment.BottomCenter),
                content = {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Cheshire Cat",
                            modifier = Modifier
                                .fillMaxSize(0.5f)
                        )
                })
             */
        }
    }
}

@Preview
@Composable
fun PreviewSpiralGradient() {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SpiralGradientPresets.forEach { currentEffect ->
            SpiralGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .width(300.dp),
                timeScale = currentEffect.timeScale,
                spiralThreshold = currentEffect.spiralThreshold,
                direction = currentEffect.direction,
                center = currentEffect.center,

                hardSampler = currentEffect.hardSampler,
                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
                onClick = {}
            )
        }
    }
}