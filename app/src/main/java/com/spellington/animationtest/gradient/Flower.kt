package com.spellington.animationtest.gradient

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class FlowerEffect(
    val animate: Boolean = true,
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
)

val FlowerEffects = listOf(
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
        petals = 13f,
        rotationTimeScale = .1f,
        petalInfluence = .35f,
        colors = palettes[6],
        wobblyFactor = .3f,
        inOutTimeScale = .5f,
    ),
    FlowerEffect(
        petals = 3f,
        rotationTimeScale = .1f,
        petalInfluence = 1f,
        colors = palettes[3],
        wobblyFactor = 1f,
        inOutTimeScale = .5f,
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
    alphaThreshold: Float = .02f,
    petalInfluence: Float = 1f,
    wobblyFactor: Float = 0f,
    colors: List<Color> = palettes[0],
    onClick: () -> Unit = {},
) {

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
                .flowerGradient(
                    colors = GradientColors(colors),
                    flowerPetals = petals,
                    animate = animate,
                    rotationTimeScale = rotationTimeScale,
                    inOutTimeScale = inOutTimeScale,
                    direction = direction,
                    rotationDirection = rotationDirection,
                    center = center,
                    alphaThreshold = alphaThreshold,
                    petalInfluence = petalInfluence,
                    wobblyFactor = wobblyFactor,
                )
                .drawBehind {
                    drawRect(Color.Red)
                }
        )
    }
}

@Composable
@Preview
fun FlowerPreviewV() {
    Flower(
        modifier = Modifier.size(
            width = 200.dp, height = 300.dp
        ),
        petals = 3f,
    )
}

@Composable
@Preview
fun FlowerPreviewH() {
    Flower(
        modifier = Modifier.size(
            width = 300.dp, height = 200.dp
        ),
        colors = palettes[1],
    )
}

@Composable
@Preview
fun FlowerPreviewH2() {
    Flower(
        modifier = Modifier.size(
            width = 300.dp, height = 200.dp
        ),
        colors = palettes[1],
        petalInfluence = .5f,
        wobblyFactor = .25f,
    )
}

@Composable
@Preview
fun FlowerPreviewVC() {
    Flower(
        modifier = Modifier.size(
            width = 200.dp, height = 300.dp
        ),
        petals = 7f,
        center = Offset(.25f,.25f),
    )
}

@Composable
@Preview
fun FlowerPreviewHC() {
    Flower(
        modifier = Modifier.size(
            width = 300.dp, height = 200.dp
        ),
        colors = palettes[1],
        center = Offset(1f,1f),
    )
}