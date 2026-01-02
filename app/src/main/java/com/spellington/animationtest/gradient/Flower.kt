package com.spellington.animationtest.gradient

import android.graphics.Shader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class FlowerEffect(
    val rotationTimeScale: Float = .5f,
    val inOutTimeScale: Float = .1f,
    val petals: Float = 5f,
    val direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    val rotationDirection: RotationDirection = RotationDirection.CounterClockwise,
    val center: Offset = Offset(.5f, .5f),
    val alphaThreshold: Float = .02f,
    val petalInfluence: Float = 1f,
    val wobblyFactor: Float = 0f,
    val colors: List<Color> = palettes[0],
    val tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
)

val FlowerEffects = listOf(
    FlowerEffect(
        petals = 13f,
        rotationTimeScale = .1f,
        petalInfluence = .35f,
        colors = palettes[5],
        wobblyFactor = .3f,
        inOutTimeScale = .5f,
    ),
    FlowerEffect(
        colors = palettes[2],
        rotationDirection = RotationDirection.Clockwise,
        inOutTimeScale = .1f,
        rotationTimeScale = .3f,
    ),
    FlowerEffect(
        petals = 7f,
        rotationTimeScale = .1f,
        petalInfluence = .35f,
        colors = palettes[1],
        center = Offset(.25f, .25f),
    ),
    FlowerEffect(
        petals = 3f,
        rotationTimeScale = .1f,
        petalInfluence = 1f,
        colors = palettes[3],
        wobblyFactor = 1f,
        inOutTimeScale = .5f,
    ),
    FlowerEffect(
        petals = 5f,
        rotationTimeScale = .1f,
        petalInfluence = .3f,
        colors = palettes[4],
        wobblyFactor = .0f,
        inOutTimeScale = .5f,
        center = Offset(.5f, .5f),
        tileMode = Shader.TileMode.REPEAT,
    ),
)

@Composable
fun Flower(
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
    colors: List<Color> = palettes[0],
    tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
    onClick: () -> Unit = {},
) {

    var time by remember { mutableFloatStateOf(0f) }

    val sampler by remember(colors, tileMode) {
        mutableStateOf(
            FlowerGradientBrush.createSampler(
                colors = GradientColors(colors),
                tileMode = tileMode,
            )
        )
    }

    val brush by remember {
        mutableStateOf(
            FlowerGradientBrush(
                sampler = sampler,
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

    if (animate) {
        LaunchedEffect(Unit) {
            var timeStart = 0f
            while (true) {
                withFrameNanos {
                    if (timeStart == 0f) {
                        timeStart = it.toFloat()
                    }

                    time = (it - timeStart) / 1_000_000_000f
                }
            }
        }
    }

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

                    brush.run {
                        setSampler(sampler)
                        setPetals(petals)
                        setRotationDirection(rotationDirection)
                        setDirection(direction)
                        setCenter(center)
                        setInOutTimeScale(inOutTimeScale)
                        setRotationTimeScale(rotationTimeScale)
                        setPetalInfluence(petalInfluence)
                        setWobblyFactor(wobblyFactor)
                    }

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
fun PreviewFlowerGradient() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FlowerEffects.forEach { currentEffect ->
            Flower(
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
                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
            )
        }
    }
}