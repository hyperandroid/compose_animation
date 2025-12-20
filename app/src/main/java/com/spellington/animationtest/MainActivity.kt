package com.spellington.animationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellington.animationtest.waves.Waves
import com.spellington.animationtest.marquee.marquee
import com.spellington.animationtest.ui.theme.AnimationtestTheme
import com.spellington.animationtest.waves.VacationTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationtestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column {
                        VacationTime()

/*
                        Waves(
                            modifier = Modifier

                                .background(Color.Cyan),
                            content = {
                                Column(modifier = Modifier.padding(innerPadding)) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .marquee(),
                                        text = "Hello Android! Hello Android! Hello Android!",
                                        fontSize = 24.sp,

                                        )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                            .marquee(
                                                duration = 10000
                                            ),
                                        text = "Hello Android!",
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(fraction = .5f)
                                                .marquee(duration = 2500),
                                            text = "Text 1"
                                        )
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(fraction = .5f)
                                                .marquee(duration = 4000),
                                            text = "Text 2"
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .marquee(duration = 6000)
                                            .background(Color.Gray)
                                    ) {
                                        for (i in 1..10) {
                                            Text(
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .width(100.dp)
                                                    .background(Color.White),
                                                text = "Hello $i",
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        */
                    }
                }
            }
        }
    }
}
