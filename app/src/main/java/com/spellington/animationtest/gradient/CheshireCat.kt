package com.spellington.animationtest.gradient

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spellington.animationtest.R

val palettes = arrayOf(
    // cheshire cat
    listOf(
        Color(0xff98117f),

        Color(0xff520759),
    ),
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
        Color(0xff1e2d00)  // Deep Forest Green
    ),
)

data class CheshireCatEffect(
    val timeScale: Float = .5f,
    val spiralThreshold: Float = 2f,
    val direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    val center: Offset = Offset(.5f, .5f),
    val alphaThreshold: Float = .02f,
    val colors: List<Color> = palettes[0],
)

val ChesireCatEffects = listOf(
    CheshireCatEffect(
        spiralThreshold = 4f,
    ),
    CheshireCatEffect(
        timeScale = .1f,
        spiralThreshold = 1f,
        colors = palettes[1],
        direction = SpiralGradientDirection.In,
    ),
    CheshireCatEffect(
        center = Offset(.25f, .25f),
        spiralThreshold = 4f,
        colors = palettes[5],
    ),
    CheshireCatEffect(
        timeScale = .1f,
        spiralThreshold = 2f,
        colors = palettes[4],
        center = Offset(.75f, .75f),
        direction = SpiralGradientDirection.In,
    ),
)

@Composable
fun CheshireCat(
    modifier: Modifier = Modifier,
    @DrawableRes drawable:  Int = R.drawable.cheshire_cat,
    animate: Boolean = true,
    timeScale: Float = .5f,
    spiralThreshold: Float = 2f,
    direction: SpiralGradientDirection = SpiralGradientDirection.In,
    colors: List<Color> = palettes[0],
    center: Offset = Offset(.5f, .5f),
    alphaThreshold: Float = .02f,
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
                .spiralGradient(
                    animate = animate,
                    direction = direction,
                    spiralThreshold = spiralThreshold,
                    timeScale = timeScale,
                    colors = GradientColors(colors),
                    center = center,
                    alphaThreshold = alphaThreshold,
                )
                .drawBehind {
                    drawRect(Color.Red)
                },
        )

        val resources = LocalContext.current.resources
        val imageBitmap = ImageBitmap.imageResource(resources, drawable)

        Image(
            bitmap = imageBitmap,
            contentDescription = "Cheshire Cat",
            modifier = Modifier
                .fillMaxSize(0.5f)
                .align(Alignment.BottomCenter),
        )
    }
}

@Preview
@Composable
fun PreviewCheshireCatV() {
    CheshireCat(
        modifier = Modifier.size(width = 200.dp, height = 300.dp),
        spiralThreshold = 4f
    )
}

@Preview
@Composable
fun PreviewCheshireCatH() {
    CheshireCat(
        modifier = Modifier.size(width = 300.dp, height = 200.dp),
        spiralThreshold = 1f,
        colors = palettes[1]
    )
}

@Preview
@Composable
fun PreviewCheshireCatVC() {
    CheshireCat(
        modifier = Modifier.size(width = 200.dp, height = 300.dp),
        spiralThreshold = 4f,
        center = Offset(.25f, .25f)
    )
}

@Preview
@Composable
fun PreviewCheshireCatHC() {
    CheshireCat(
        modifier = Modifier.size(width = 300.dp, height = 200.dp),
        spiralThreshold = 3f,
        colors = palettes[1],
        center = Offset(.75f, .75f)
    )
}