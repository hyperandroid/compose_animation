package com.spellington.animationtest.waves

import android.content.res.Configuration
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.graphics.SweepGradient
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spellington.animationtest.R
import com.spellington.animationtest.gradient.GradientColors
import com.spellington.animationtest.gradient.SpiralGradientDirection
import com.spellington.animationtest.gradient.flowerGradient
import kotlinx.coroutines.android.awaitFrame
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin


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
                            fontSize = fontSize,
                            color = Color.Magenta
                        )
                        Text(
                            text = "Time",
                            fontSize = fontSize
                        )
                    }
                }
            )

            Canvas(
                modifier = Modifier
                    //.blur(16.dp)
                    .padding(16.dp)
                    .clip(shape = RectangleShape)
                    .height(100.dp)
                    .fillMaxWidth()
                    .flowerGradient(
                        animate = true,
                        colors = GradientColors(
                            listOf(
                                Color(0xffff00ff),
                                Color.Red,
                                Color.Yellow,
                                Color.White,
                                Color.Cyan,

                                )
                        ),
                        flowerPetals = 5f,
                    ),
            ) {
                drawRect(Color.Blue)
            }

            BlurRadialGradient2(
                modifier = Modifier
                    //.blur(16.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .height(80.dp),
                colors = GradientColors(
                    listOf(
                        Color.Magenta,
                        Color.Red,
                        Color.Yellow,
                        Color.White,
                        Color.Cyan,
                        Color.Green,
                    )
                ),

            )

            RotatingSweepGradient(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .height(100.dp) // Example size
                    .fillMaxWidth(),
                colors = GradientColors(
                    listOf(
                        Color.Magenta,
                        Color.Red,
                        Color.Yellow,
                        Color.White,
                        Color.Cyan,
                        Color.Green,
                    )
                )
            )


            OutlinedTextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
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

enum class GradientType {
    Spiral,
    WavyHorizontal,
    WavyVertical,
    Hatch,
}

val GRADIENT_SHADER_SRC = """
    
    uniform shader gradient;   
    uniform shader content;   
    uniform float2 iResolution; 
    uniform float iTime;
    
    float remap(float v0, float v1, float r0, float r1, float v) {
        return r0 + (r1-r0)*(v-v0)/(v1-v0);
    }
    
    float2 rotate(float2 uv, float angle) {
        float c = cos(angle);
        float s = sin(angle);
        uv -= .5;
        uv *= mat2(c,s,-s,c);
        uv += .5;
            
        return uv;
    }
    
    float2 hatchy(float2 uv, float stripes, float factor) {
        float displacement = stripes * uv.y + iTime;
        float repeat = fract(displacement);
        int index = int(displacement);
        if (mod(float(index),2)==0) {
            uv.x += factor*repeat;
        } else {
            uv.x += factor*(1.-repeat);
        }
        
        return uv;
    }
    
    float2 horizontalWaves(float2 uv, float amplitude, float period) {
        uv.x += amplitude*sin(uv.y*period + iTime);   // horizontal waves
        return uv;
    }
        
    float2 verticalWaves(float2 uv, float amplitude, float period) {
        uv.y += amplitude*sin(uv.x*period + iTime);   // horizontal waves
        return uv;
    }
    
    float2 spiral(float2 uv, float spiral, float time) {
        float pi = 3.1415926;
        uv = (2*uv) - 1.;
        float aspect = min(iResolution.x/iResolution.y, iResolution.y/iResolution.x);
        uv.y *= aspect;
  
        float radius = length(uv);
        float angle = atan(uv.y, uv.x);
        float t = remap(-pi, pi, 0, 1, angle);
        
        t = (t + spiral*radius + time);
        
        return float2(t, 0);
    }
    
    float2 wavySpiral(float2 uv, float spiral) {
        float pi = 3.1415926;
        uv = (2*uv) - 1.;
        float aspect = min(iResolution.x/iResolution.y, iResolution.y/iResolution.x);
        uv.y *= aspect;
  
        float radius = length(uv);
        float angle = atan(uv.y, uv.x);
        float t = remap(-pi, pi, 0, 1, angle + spiral*radius - iTime);
        
        return float2(t, 0);
    }

    half4 main(float2 fragCoord) {
	    
        float2 uv = fragCoord / iResolution;
        
        // uv = hatchy(uv, 8, .2);
        // uv = hatchy(uv, 16, .1);
        //uv = spiral(uv, iTime, 40);
        uv = spiral(uv, 3, -iTime);
        
        //uv = horizontalWaves(uv, .1, 2);
        
        
        half4 color = gradient.eval(uv);

        /*
        uv = (2*uv) - 1.;
        uv *= -1;
        uv.y *= iResolution.y/iResolution.x;    // aspect.
        rotate(uv, iTime);
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
    colors: GradientColors = GradientColors(listOf(Color.Red, Color.White)),
    type: GradientType = GradientType.WavyHorizontal,
    content: @Composable BoxScope.() -> Unit,
) {

    val runtimeShader = remember { RuntimeShader(GRADIENT_SHADER_SRC) }
    var timeMs by remember { mutableFloatStateOf(0f) }

    val gradShader = remember {
        val colorsArray = colors.colors.map { color ->
            0xff000000.toInt() or
                    ((color.red * 255).toInt() shl 16) or
                    ((color.green * 255).toInt() shl 8) or
                    ((color.blue * 255).toInt())
        }.toIntArray()

        LinearGradient(
            0f, 0f, 1f, 0f,
            colorsArray,
            null,
                Shader.TileMode.REPEAT
        )
    }

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
                runtimeShader.setInputShader("gradient", gradShader)
            }
            .graphicsLayer {
                clip = true
                renderEffect = RenderEffect
                    .createShaderEffect(runtimeShader)
                    .asComposeRenderEffect()

                runtimeShader.setFloatUniform("iTime", timeMs)
            }

    ) {

        content()
    }
}

@Composable
fun RotatingSweepGradient(
    modifier: Modifier = Modifier,
    colors: GradientColors = GradientColors(listOf(Color.Red, Color.Yellow))
) {
    var angle by remember { mutableFloatStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    // Animate the angle on every frame to create rotation
    LaunchedEffect(Unit) {
        var startTime = 0L
        while (true) {
            val frameTime = awaitFrame()
            if (startTime == 0L) {
                startTime = frameTime
            }
            // Rotate 360 degrees every 5 seconds
            angle = (frameTime - startTime) * 360f / 5_000_000_000f
        }
    }

    val shader = remember(center) {
        SweepGradient(
            center.x,
            center.y,
            colors.colors.map { it.toArgb() }.toIntArray(),
            null,
        )
    }

    // Recreate the brush only when the angle or center changes.
    val brush = remember(angle, center) {
            // Create a matrix and apply rotation around the center
            val matrix = Matrix().apply {
                postRotate(angle, center.x, center.y)
            }
            shader.setLocalMatrix(matrix)

            // Wrap the native shader in a Compose ShaderBrush
            ShaderBrush(shader)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                // Update the center when the composable's size is known or changes
                center = Offset(size.width / 2f, size.height / 2f)
            }
    ) {
        // Now, drawRect uses a brush that is already rotated.
        // The canvas itself is not transformed.
        drawRect(brush = brush)

        // Any other drawing done here would NOT be rotated.
        // For example:
        // drawCircle(color = Color.Black, radius = 20f)
    }
}


@Composable
fun BlurRadialGradient2(
    modifier: Modifier = Modifier,
    colors: GradientColors = GradientColors(listOf(Color.Red, Color.Yellow))
) {

    var angle by remember { mutableFloatStateOf(0f) }

    // Animate the angle on every frame
    LaunchedEffect(Unit) {
        var startTime = 0L
        while (true) {
            val frameTime = awaitFrame()
            if (startTime == 0L) {
                startTime = frameTime
            }
            // Rotate 360 degrees every 5 seconds
            angle = (frameTime - startTime) * 360f / 10_000_000_000f
        }
    }

    // This state will hold the center of the composable
    var center by remember { mutableStateOf(Offset.Zero) }

    // Recreate the brush whenever the angle or center changes
    val brush = remember(angle, center) {
        val radius = max(center.x, center.y) * .1f
        if (radius == 0f) {
            // Return a simple brush if size is not yet calculated
            Brush.linearGradient(colors.colors)
        } else {
            val angleRad = Math.toRadians(angle.toDouble()).toFloat()

            // Calculate start and end points for the gradient
            val start = Offset(
                x = center.x + radius * cos(angleRad),
                y = center.y + radius * sin(angleRad)
            )
            val end = Offset(
                x = center.x - radius * cos(angleRad),
                y = center.y - radius * sin(angleRad)
            )

            Brush.linearGradient(
                colors = colors.colors,
                start = start,
                end = end,
                tileMode = TileMode.Mirror
            )
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged {
                // Update the center when the size changes
                center = Offset(it.width / 2f, it.height / 2f)
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw a rectangle covering the whole canvas with the rotating brush
            drawRect(brush = brush)
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