package com.spellington.animationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animate
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellington.animationtest.gradient.CheshireCat
import com.spellington.animationtest.gradient.ChesireCatEffects
import com.spellington.animationtest.gradient.Flower
import com.spellington.animationtest.gradient.FlowerEffects
import com.spellington.animationtest.gradient.FourColorGradient
import com.spellington.animationtest.gradient.FourColorGradientEffects
import com.spellington.animationtest.gradient.WavyGradient
import com.spellington.animationtest.gradient.WavyGradientEffects
import com.spellington.animationtest.ui.theme.AnimationtestTheme
import com.spellington.animationtest.waves.VacationTime
import com.spellington.animationtest.waves.Waves
import kotlinx.coroutines.launch

enum class AnimationDemos {
    Marquee,
    SpiralGradient,
    FlowerGradient,
    FourColorGradient,
    WavyGradient,
    Vacation,
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationtestTheme {

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var selectedAnimation by rememberSaveable {
                    mutableStateOf(AnimationDemos.SpiralGradient)
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        // 3. Define the content of the drawer
                        ModalDrawerSheet {
                            Text("Drawer Title", modifier = Modifier.padding(16.dp))
                            NavigationDrawerItem(
                                label = { Text("Wavy Gradient") },
                                selected = false,
                                onClick = {
                                    selectedAnimation = AnimationDemos.WavyGradient
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Four Color Gradient") },
                                selected = false,
                                onClick = {
                                    selectedAnimation = AnimationDemos.FourColorGradient
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Spiral Gradient") },
                                selected = false,
                                onClick = {
                                    selectedAnimation = AnimationDemos.SpiralGradient
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Flower Gradient") },
                                selected = false,
                                onClick = {
                                    selectedAnimation = AnimationDemos.FlowerGradient
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Vacation Time") },
                                selected = false,
                                onClick = {
                                    selectedAnimation = AnimationDemos.Vacation
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            // 4. Add a TopAppBar with a button to open the drawer
                            TopAppBar(
                                title = { Text("Animations") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed)
                                                    open()
                                                else
                                                    close()
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Open Drawer"
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {

                            when (selectedAnimation) {
                                AnimationDemos.Marquee -> {}
                                AnimationDemos.SpiralGradient -> ShowCheshireCat()
                                AnimationDemos.FlowerGradient -> ShowFlowerGradient()
                                AnimationDemos.Vacation -> VacationTime()
                                AnimationDemos.FourColorGradient -> ShowFourColorGradients()
                                AnimationDemos.WavyGradient -> ShowWavyGradients()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowWavyGradients(modifier: Modifier = Modifier) {

    var effectIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val currentEffect = WavyGradientEffects[effectIndex % WavyGradientEffects.size]

    Box(
        modifier = modifier,
    ) {
        WavyGradient(
            modifier = Modifier
                .fillMaxSize(),
            animate = true,
            direction = currentEffect.direction,
            timeScale = currentEffect.timeScale,
            colors = currentEffect.colors,
            tileMode = currentEffect.tileMode,
            onClick = {
                effectIndex += 1
            }
        )
    }
}

@Composable
fun ShowFourColorGradients(modifier: Modifier = Modifier) {

    var effectIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val currentEffect = FourColorGradientEffects[effectIndex % FourColorGradientEffects.size]

    Box(
        modifier = modifier,
    ) {
        FourColorGradient(
            modifier = Modifier
                .fillMaxSize(),
            topLeft = currentEffect.topLeft,
            topRight = currentEffect.topRight,
            bottomLeft = currentEffect.bottomLeft,
            bottomRight = currentEffect.bottomRight,
            onClick = {
                effectIndex += 1
            }
        )
    }
}

@Composable
fun ShowFlowerGradient(modifier: Modifier = Modifier) {

    var effectIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val currentEffect = FlowerEffects[effectIndex % FlowerEffects.size]

    Box(modifier = modifier) {
        Flower(
            modifier = modifier,
            rotationTimeScale = currentEffect.rotationTimeScale,
            inOutTimeScale = currentEffect.inOutTimeScale,
            petals = currentEffect.petals,
            direction = currentEffect.direction,
            rotationDirection = currentEffect.rotationDirection,
            center = currentEffect.center,
            petalInfluence = currentEffect.petalInfluence,
            wobblyFactor = currentEffect.wobblyFactor,
            colors = currentEffect.colors,
            onClick = {
                effectIndex += 1
            }
        )

        Waves(content =  {
            Text(
                text = "Tap Me",
                modifier = Modifier
                    .padding(16.dp)
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
        })
    }
}

@Composable
fun ShowCheshireCat(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int = R.drawable.cheshire_cat,
) {

    var effectIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val currentEffect = ChesireCatEffects[effectIndex % ChesireCatEffects.size]

    CheshireCat(
        modifier = Modifier.fillMaxSize(),
        drawable = drawable,
        timeScale = currentEffect.timeScale,
        spiralThreshold = currentEffect.spiralThreshold,
        direction = currentEffect.direction,
        colors = currentEffect.colors,
        center = currentEffect.center,
        onClick = {
            effectIndex += 1
        }
    )

}

@Preview
@Composable
fun PreviewFlowerGradient() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
            )
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
                colors = currentEffect.colors,
                center = currentEffect.center,
                onClick = {}
            )
        }
    }
}