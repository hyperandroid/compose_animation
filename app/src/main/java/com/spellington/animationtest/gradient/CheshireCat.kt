package com.spellington.animationtest.gradient

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellington.animationtest.R

val palettes = arrayOf(
    listOf(
        Color(0xff98117f),
        Color(0xff520759),
    ),
    listOf(
        Color(0xffff00ff),
        Color.Red,
        Color.Yellow,
        Color.White,
        Color.Cyan,
    )
)

@Composable
fun CheshireCat(
    modifier: Modifier = Modifier,
    @DrawableRes drawable:  Int = R.drawable.cheshire_cat,
    timeScale: Float = .5f,
    spiralThreshold: Float = 2f,
    direction: SpiralGradientDirection = SpiralGradientDirection.In,
    colors: List<Color> = palettes[0],
    center: Offset = Offset(.5f, .5f),
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
                //.blur(16.dp)
                .fillMaxSize()
                .clip(shape = RectangleShape)
                .spiralGradient(
                    animate = true,
                    direction = direction,
                    spiralThreshold = spiralThreshold,
                    timeScale = timeScale,
                    colors = GradientColors(
                        paletteColors
                    ),
                    center = center,
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