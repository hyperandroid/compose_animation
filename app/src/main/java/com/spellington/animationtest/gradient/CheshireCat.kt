package com.spellington.animationtest.gradient

import android.graphics.Shader
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellington.animationtest.R
import com.spellington.animationtest.gradient.brush.GradientColors
import com.spellington.animationtest.gradient.brush.SpiralGradientBrush
import com.spellington.animationtest.gradient.brush.SpiralGradientDirection
import com.spellington.animationtest.util.GradientSamplerOrientation
import com.spellington.animationtest.util.PausableAnimatedTime
import com.spellington.animationtest.util.SamplerFactory
import com.spellington.animationtest.waves.Waves

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
        Color(0xff1e2d00)  // modeDeep Forest Green
    ),
)

data class CheshireCatEffect(
    val timeScale: Float = .5f,
    val spiralThreshold: Float = 2f,
    val direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    val center: Offset = Offset(.5f, .5f),

    val hardSampler: Boolean = false,
    val colors: List<Color> = palettes[0],
    val tileMode: Shader.TileMode = Shader.TileMode.MIRROR,
)

val ChesireCatEffects = listOf(
    CheshireCatEffect(
        spiralThreshold = 4f,
        hardSampler = true,
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
        hardSampler = true,
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

    hardSampler: Boolean = false,
    center: Offset = Offset(.5f, .5f),
    colors: List<Color> = palettes[0],
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

                        brush.run {
                            setSampler(sampler.sampler)
                            setCenter(center)
                            setDirection(direction)
                            setSpiralThreshold(spiralThreshold)
                            setTimeScale(timeScale)
                        }

                        onDrawBehind {
                            brush.setTime(time)
                            drawRect(brush)
                        }
                    }
            )

            val resources = LocalContext.current.resources
            val imageBitmap = ImageBitmap.imageResource(resources, drawable)


            Waves(
                modifier = Modifier.align(Alignment.BottomCenter),
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Cheshire Cat",
                            modifier = Modifier
                                .fillMaxSize(0.5f)
                        )
                        Text(
                            text = "Tap Me",
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            fontSize = 60.sp,
                            color = Color.White,
                            style = TextStyle(
                                shadow = androidx.compose.ui.graphics.Shadow(
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
fun PreviewCheshireCat() {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ChesireCatEffects.forEach { currentEffect ->
            CheshireCat(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .width(300.dp),
                timeScale = currentEffect.timeScale,
                spiralThreshold = currentEffect.spiralThreshold,
                direction = currentEffect.direction,
                hardSampler = currentEffect.hardSampler,
                colors = currentEffect.colors,
                center = currentEffect.center,
                onClick = {}
            )
        }
    }
}