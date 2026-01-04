package com.spellington.animationtest.gradient


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
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spellington.animationtest.gradient.brush.FourColorGradientBrush
import com.spellington.animationtest.util.PausableAnimatedTime

data class FourColorGradientPreset(
    val topLeft: Color,
    val topRight: Color,
    val bottomLeft: Color,
    val bottomRight: Color,
)

val FourColorGradientPresets = listOf(
// 2) Sunset Heat
    FourColorGradientPreset(
        topRight =  Color(0xFFFFB703),
        topLeft =   Color(0xFFFB5607),
        bottomRight = Color(0xFFFF006E),
        bottomLeft =  Color(0xFF8338EC)
    ),

// 3) Deep Ocean
    FourColorGradientPreset(
        topRight =  Color(0xFF2EC4B6),
        topLeft =   Color(0xFF0B132B),
        bottomRight = Color(0xFF1C2541),
        bottomLeft =  Color(0xFF5BC0EB)
    ),

// 4) Cotton Candy
    FourColorGradientPreset(
        topRight =  Color(0xFFB8F2E6),
        topLeft =   Color(0xFFAED9E0),
        bottomRight = Color(0xFFFFA6C1),
        bottomLeft =  Color(0xFFFFC8DD)
    ),

// 5) Forest + Gold
    FourColorGradientPreset(
        topRight =  Color(0xFFF2C14E),
        topLeft =   Color(0xFF0B3D2E),
        bottomRight = Color(0xFF2D6A4F),
        bottomLeft =  Color(0xFF95D5B2)
    ),

// 6) Cyberpunk Night
    FourColorGradientPreset(
        topRight =  Color(0xFF00F5D4),
        topLeft =   Color(0xFF0B0F2B),
        bottomRight = Color(0xFFF15BB5),
        bottomLeft =  Color(0xFF9B5DE5)
    ),

// 7) Desert Sky
    FourColorGradientPreset(
        topRight =  Color(0xFF90E0EF),
        topLeft =   Color(0xFFFFDDD2),
        bottomRight = Color(0xFF3A86FF),
        bottomLeft =  Color(0xFFE29578)
    ),

// 8) Monochrome + Accent
    FourColorGradientPreset(
        topRight =  Color(0xFFFFFFFF),
        topLeft =   Color(0xFF111827),
        bottomRight = Color(0xFF60A5FA),
        bottomLeft =  Color(0xFFE5E7EB)
    ),

)

@Composable
fun FourColorGradient(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    timeScale: Float = .1f,
    topLeft: Color,
    topRight: Color,
    bottomLeft: Color,
    bottomRight: Color,
    onClick: () -> Unit = {}
) {

    val brush by remember {
        mutableStateOf(
            FourColorGradientBrush(
                topLeft = topLeft,
                topRight = topRight,
                bottomLeft = bottomLeft,
                bottomRight = bottomRight,
            )
        )
    }

    brush.run {
        setTopLeftColor(topLeft)
        setTopRightColor(topRight)
        setBottomLeftColor(bottomLeft)
        setBottomRightColor(bottomRight)
        setTimeScale(timeScale)
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
fun FourColorGradient1() {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        FourColorGradientPresets.forEach { effect ->
            FourColorGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .height(100.dp),
                topLeft = effect.topLeft,
                topRight = effect.topRight,
                bottomLeft = effect.bottomLeft,
                bottomRight = effect.bottomRight,
            )
        }
    }
}