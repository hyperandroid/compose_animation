package com.spellington.animationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spellington.animationtest.gradient.SpiralGradient
import com.spellington.animationtest.gradient.SpiralGradientPresets
import com.spellington.animationtest.gradient.PolarGradient
import com.spellington.animationtest.gradient.PolarGradientPresets
import com.spellington.animationtest.gradient.FourColorGradient
import com.spellington.animationtest.gradient.FourColorGradientPresets
import com.spellington.animationtest.gradient.HatchGradient
import com.spellington.animationtest.gradient.HatchGradientPresets
import com.spellington.animationtest.gradient.WavyGradient
import com.spellington.animationtest.gradient.WavyGradientEffects
import com.spellington.animationtest.ui.theme.AnimationtestTheme
import com.spellington.animationtest.waves.VacationTime
import kotlinx.coroutines.launch

enum class AnimationDemos {
    Marquee,
    SpiralGradient,
    FlowerGradient,
    FourColorGradient,
    WavyGradient,
    HatchGradient,
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
                    mutableStateOf(AnimationDemos.HatchGradient)
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
                                label = { Text("Hatch Gradient") },
                                selected = false,
                                onClick = {
                                    selectedAnimation = AnimationDemos.HatchGradient
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
                                AnimationDemos.HatchGradient -> ShowHatchGradients()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowHatchGradients(modifier: Modifier = Modifier) {

    var effectIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val currentEffect = HatchGradientPresets[effectIndex % HatchGradientPresets.size]

    Box(
        modifier = modifier,
    ) {
        HatchGradient(
            modifier = Modifier
                .fillMaxSize(),
            animate = true,
            direction = currentEffect.direction,
            timeScale = currentEffect.timeScale,
            amplitude = currentEffect.amplitude,
            peaks = currentEffect.peaks,
            angle = currentEffect.angle,

            bounds = currentEffect . bounds,
            hardSampler = currentEffect.hardSampler,
            tileMode = currentEffect.tileMode,
            colors = currentEffect.colors,

            onClick = {
                effectIndex += 1
            }
        )
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
            amplitude = currentEffect.amplitude,
            period = currentEffect.period,
            angle = currentEffect.angle,

            bounds = currentEffect.bounds,
            hardSampler = currentEffect.hardSampler,
            tileMode = currentEffect.tileMode,
            colors = currentEffect.colors,

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

    val currentEffect = FourColorGradientPresets[effectIndex % FourColorGradientPresets.size]

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

    val currentEffect = PolarGradientPresets[effectIndex % PolarGradientPresets.size]


    PolarGradient(
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
        tileMode = currentEffect.tileMode,
        hardSampler = currentEffect.hardSampler,
        onClick = {
            effectIndex += 1
        }
    )
}

@Composable
fun ShowCheshireCat(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int = R.drawable.cheshire_cat,
) {

    var effectIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val currentEffect = SpiralGradientPresets[effectIndex % SpiralGradientPresets.size]

    SpiralGradient(
        modifier = modifier.fillMaxSize(),
        drawable = drawable,
        timeScale = currentEffect.timeScale,
        spiralThreshold = currentEffect.spiralThreshold,
        direction = currentEffect.direction,

        hardSampler = currentEffect.hardSampler,
        colors = currentEffect.colors,
        tileMode = currentEffect.tileMode,
        center = currentEffect.center,
        onClick = {
            effectIndex += 1
        }
    )

}

@Preview
@Composable
fun PreviewPolarGradientGradient() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
                hardSampler = currentEffect.hardSampler,
            )
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
                colors = currentEffect.colors,
                center = currentEffect.center,
                hardSampler = currentEffect.hardSampler,
                tileMode = currentEffect.tileMode,
                onClick = {}
            )
        }
    }
}
@Preview
@Composable
fun PreviewWavyGradient() {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WavyGradientEffects.forEach { currentEffect ->
            WavyGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .height(200.dp),
                timeScale = currentEffect.timeScale,
                direction = currentEffect.direction,
                amplitude = currentEffect.amplitude,
                period = currentEffect.period,
                angle = currentEffect.angle,

                hardSampler = currentEffect.hardSampler,
                bounds = currentEffect.bounds,
                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
            )
        }
    }
}

@Preview
@Composable
fun PreviewHatchGradients() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        HatchGradientPresets.forEach { currentEffect ->
            HatchGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .height(200.dp),
                timeScale = currentEffect.timeScale,
                direction = currentEffect.direction,
                amplitude = currentEffect.amplitude,
                peaks = currentEffect.peaks,
                angle = currentEffect.angle,

                hardSampler = currentEffect.hardSampler,
                bounds = currentEffect.bounds,
                colors = currentEffect.colors,
                tileMode = currentEffect.tileMode,
            )
        }
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
