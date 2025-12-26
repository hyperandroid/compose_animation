package com.spellington.animationtest.gradient

import androidx.annotation.DrawableRes
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
import com.spellington.animationtest.R

@Composable
fun Flower(
    modifier: Modifier = Modifier,
    @DrawableRes drawable:  Int = R.drawable.cheshire_cat,
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
) {

    var paletteIndex by remember {
        mutableIntStateOf(0)
    }

    var paletteColors by remember {
        mutableStateOf(colors)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                paletteIndex += 1
                paletteColors = palettes[paletteIndex%palettes.size]
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .flowerGradient(
                    colors = GradientColors(paletteColors),
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
        petals = 3f,
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