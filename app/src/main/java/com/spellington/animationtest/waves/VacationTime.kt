package com.spellington.animationtest.waves

import android.content.res.Configuration
import android.graphics.Matrix
import android.graphics.RuntimeShader
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellington.animationtest.R
import kotlinx.coroutines.android.awaitFrame


@Composable
fun VacationTime(
    modifier: Modifier = Modifier,
    @DrawableRes drawableLandscape: Int = R.drawable.vacation_time,
    @DrawableRes drawablePortrait: Int = R.drawable.vacation_time_portrait,
    fontSize: TextUnit = 64.sp,
) {

    val configuration = LocalConfiguration.current
    val isPortrait =
        configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val drawable = if (isPortrait)
        drawablePortrait
    else
        drawableLandscape

    VacationTimeBackground(
        modifier = modifier,
        drawable = drawable,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Waves(
                modifier = Modifier,
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Vacation",
                            fontSize = fontSize
                        )
                        Text(
                            text = "Time",
                            fontSize = fontSize
                        )
                    }
                }
            )

            BlurRadialGradient(
                modifier = Modifier
                    //.blur(16.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .fillMaxWidth()

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Vacation",
                        fontSize = fontSize
                    )
                    Text(
                        text = "Time",
                        fontSize = fontSize
                    )
                }
            }

            BlurRadialGradient2(
                modifier = Modifier
                    //.blur(16.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .height(80.dp)

            )

            OutlinedTextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    ,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                value = "",
                placeholder = {
                    Text(text = "User")
                },
                onValueChange = { },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
            )

            OutlinedTextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                ,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                value = "",
                onValueChange = { },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                placeholder = {
                    Text(text = "Password")
                },
                colors = colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
    }
}

val GRADIENT_SHADER_SRC = """
    
    uniform shader gradient;   
    uniform shader content;   
    uniform float2 iResolution; 
    uniform float iTime;
    
    float remap(float v0, float v1, float r0, float r1, float v) {
        return r0 + (r1-r0)*(v-v0)/(v1-v0);
    }

    half4 main(float2 fragCoord) {
					
        float pi = 3.1415926;
        
        float2 uv = fragCoord / iResolution;
        float c = cos(iTime);
        float s = sin(iTime);
        uv -= .5;
        uv *= mat2(c,s,-s,c);
        uv += .5;
        half4 color = gradient.eval(uv);

        /*
        uv = (2*uv) - 1.;
        float c = cos(iTime);
        float s = sin(iTime);
        uv *= mat2(c,s,-s,c);
        float angle = atan(uv.y, uv.x);
        float t = remap(-pi, pi, 0, 1, angle);
        
        half4 color = gradient.eval(float2(t, 0));
        */
        
       
        return color;
    }
""".trimIndent()


@Composable
fun BlurRadialGradient(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {

    val runtimeShader = remember { RuntimeShader(GRADIENT_SHADER_SRC) }
    var timeMs by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        var startNanos = 0L

        while (true) {
            val frameTimeNanos = awaitFrame()

            if (startNanos == 0L) {
                startNanos = frameTimeNanos
            }

            timeMs = (frameTimeNanos - startNanos) / 1_000_000_000f
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged {
                val width = it.width.toFloat()
                val height = it.height.toFloat()
                runtimeShader.setFloatUniform("iResolution", width, height)

                val gradShader = android.graphics.LinearGradient(
                    0f, 0f, 1f, 0f,
                    intArrayOf(
                        android.graphics.Color.MAGENTA,
                        android.graphics.Color.RED,
                        android.graphics.Color.YELLOW,
                        android.graphics.Color.WHITE,
                        android.graphics.Color.CYAN,
                        android.graphics.Color.GREEN,
                    ),
                    null,
                    android.graphics.Shader.TileMode.CLAMP
                )

                runtimeShader.setInputShader("gradient", gradShader)
            }
            .graphicsLayer {
                clip = true
                renderEffect = android.graphics.RenderEffect
                    .createShaderEffect(runtimeShader)
                    .asComposeRenderEffect()

                runtimeShader.setFloatUniform("iTime", timeMs)
            }

    ) {

        content()
    }
}

@Composable
fun BlurRadialGradient2(
    modifier: Modifier = Modifier,
) {

    var timeMs by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        var startNanos = 0L

        while (true) {
            val frameTimeNanos = awaitFrame()

            if (startNanos == 0L) {
                startNanos = frameTimeNanos
            }

            timeMs = (frameTimeNanos - startNanos) / 1_000_000_000f
        }
    }

    val brush = remember {
        Brush.linearGradient(
            listOf(
                Color.Magenta,
                Color.Red,
                Color.Yellow,
                Color.Green,
                Color.White,
            )
        )
    }

    val view = LocalView.current
    var w by remember { mutableFloatStateOf(view.width.toFloat()) }
    var h by remember { mutableFloatStateOf(view.height.toFloat()) }

    val path by remember(w, h) {
        val p = Path()
        p.moveTo(0f, 0f)
        p.addRect(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = w,
                bottom = h,
            )
        )
        mutableStateOf(p)
    }

    Box(
        modifier = modifier
            .onSizeChanged {
                w = it.width.toFloat()
                h = it.height.toFloat()
            }
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            withTransform(
                {
                    rotate(timeMs)
                }
            ) {
                drawPath(
                    path = path,
                    brush = brush,
                )
            }
        }

    }
}

@Composable
fun VacationTimeBackground(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int = R.drawable.vacation_time,
    overlay: @Composable () -> Unit,
) {

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {

        val resources = LocalContext.current.resources

        val image = ImageBitmap.imageResource(resources, drawable)

        val w = constraints.maxWidth.toFloat()
        val h = constraints.maxHeight.toFloat()
        val scale = maxOf(w / image.width, h / image.height)
        val dx = (w - image.width * scale) / 2f
        val dy = (h - image.height * scale) / 2f

        val matrix = Matrix().apply {
            setScale(scale, scale)
            preTranslate(dx / scale, dy / scale)
        }

        val shader = remember {
            ImageShader(
                image,
            ).apply {
                setLocalMatrix(matrix)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ShaderBrush(shader))
        )

        overlay()
    }
}

@Preview
@Composable
fun VacationTimePreview() {
    VacationTime(
        modifier = Modifier.fillMaxSize(),
        drawableLandscape = R.drawable.vacation_time,
        drawablePortrait = R.drawable.vacation_time_portrait,
    )
}