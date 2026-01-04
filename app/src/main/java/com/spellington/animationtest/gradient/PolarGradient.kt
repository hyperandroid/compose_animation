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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spellington.animationtest.gradient.brush.PolarGradientBrush
import com.spellington.animationtest.gradient.brush.GradientColors
import com.spellington.animationtest.gradient.brush.RotationDirection
import com.spellington.animationtest.gradient.brush.SpiralGradientDirection
import com.spellington.animationtest.util.GradientSamplerOrientation
import com.spellington.animationtest.util.PausableAnimatedTime
import com.spellington.animationtest.util.SamplerFactory
import com.spellington.animationtest.util.samplePalettes

data class PolarGradientPreset(
    val rotationTimeScale: Float = .5f,
    val inOutTimeScale: Float = .1f,
    val petals: Float = 5f,
    val direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    val rotationDirection: RotationDirection = RotationDirection.CounterClockwise,
    val center: Offset = Offset(.5f, .5f),
    val petalInfluence: Float = 1f,
    val wobblyFactor: Float = 0f,

    val hardSampler: Boolean = false,
    val colors: List<Color> = samplePalettes[0],
    val tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
)

val PolarGradientPresets = listOf(
    PolarGradientPreset(
        colors = samplePalettes[2],
        rotationDirection = RotationDirection.Clockwise,
        inOutTimeScale = .1f,
        rotationTimeScale = .3f,
    ),
    PolarGradientPreset(
        petals = 13f,
        rotationTimeScale = .1f,
        petalInfluence = .35f,
        colors = samplePalettes[5],
        wobblyFactor = .3f,
        inOutTimeScale = .5f,
        hardSampler = true,
    ),
    PolarGradientPreset(
        petals = 7f,
        rotationTimeScale = .1f,
        petalInfluence = .35f,
        colors = samplePalettes[1],
        center = Offset(.25f, .25f),
    ),
    PolarGradientPreset(
        petals = 3f,
        rotationTimeScale = .1f,
        petalInfluence = 1f,
        colors = samplePalettes[3],
        wobblyFactor = 1f,
        inOutTimeScale = .5f,
    ),
    PolarGradientPreset(
        petals = 5f,
        rotationTimeScale = .1f,
        petalInfluence = .3f,
        colors = samplePalettes[4],
        wobblyFactor = .0f,
        inOutTimeScale = .5f,
        center = Offset(.5f, .5f),
        tileMode = Shader.TileMode.MIRROR,
        hardSampler = true,
    ),
)

@Composable
fun PolarGradient(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    rotationTimeScale: Float = .5f,
    inOutTimeScale: Float = .1f,
    petals: Float = 5f,
    direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    rotationDirection: RotationDirection = RotationDirection.CounterClockwise,
    center: Offset = Offset(.5f, .5f),
    petalInfluence: Float = 1f,
    wobblyFactor: Float = 0f,

    hardSampler: Boolean = false,
    colors: List<Color> = samplePalettes[0],
    tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
    onClick: () -> Unit = {},
) {

    val sampler by remember(colors, tileMode) {
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
            PolarGradientBrush(
                sampler = sampler.sampler,
                rotationTimeScale = rotationTimeScale,
                inOutTimeScale = inOutTimeScale,
                flowerPetals = petals,
                direction = direction,
                rotationDirection = rotationDirection,
                center = center,
                petalInfluence = petalInfluence,
                wobblyFactor = wobblyFactor,
            )
        )
    }

    brush.run {
        setSampler(sampler.sampler)
        setPetals(petals)
        setRotationDirection(rotationDirection)
        setDirection(direction)
        setCenter(center)
        setInOutTimeScale(inOutTimeScale)
        setRotationTimeScale(rotationTimeScale)
        setPetalInfluence(petalInfluence)
        setWobblyFactor(wobblyFactor)
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
fun PreviewPolarGradientGradient() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PolarGradientPresets.forEach { currentEffect ->
            PolarGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .width(300.dp),
                animate = true,
                rotationTimeScale = currentEffect.rotationTimeScale,
                inOutTimeScale = currentEffect.inOutTimeScale,
                petals = currentEffect.petals,
                direction = currentEffect.direction,
                rotationDirection = currentEffect.rotationDirection,
                center = currentEffect.center,
                petalInfluence = currentEffect.petalInfluence,
                wobblyFactor = currentEffect.wobblyFactor,

                hardSampler = currentEffect.hardSampler,
                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
            )
        }
    }
}