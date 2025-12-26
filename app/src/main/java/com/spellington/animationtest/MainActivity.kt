package com.spellington.animationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spellington.animationtest.gradient.CheshireCat
import com.spellington.animationtest.ui.theme.AnimationtestTheme
import com.spellington.animationtest.waves.VacationTime
import kotlinx.coroutines.launch

enum class AnimationDemos {
    Marquee,
    SpiralGradientCat,
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
                var selectedAnimation by remember {
                    mutableStateOf(AnimationDemos.SpiralGradientCat)
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        // 3. Define the content of the drawer
                        ModalDrawerSheet {
                            Text("Drawer Title", modifier = Modifier.padding(16.dp))
                            NavigationDrawerItem(
                                label = { Text("Cheshire cat") },
                                selected = false,
                                onClick = {
                                    selectedAnimation = AnimationDemos.SpiralGradientCat
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
                                AnimationDemos.SpiralGradientCat -> CheshireCat(
                                    spiralThreshold = 4f,
                                    timeScale = .5f,
                                )
                                AnimationDemos.Vacation -> VacationTime()
                            }
                        }
                    }
                }
            }
        }
    }
}
