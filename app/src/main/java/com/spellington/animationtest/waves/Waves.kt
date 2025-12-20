package com.spellington.animationtest.waves

import android.os.Build
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.android.awaitFrame

val SHADER_SRC = """
    
    uniform shader contents;   // this will be the video frames
    uniform float2 iResolution; // width, height if you need it
    uniform float iTime;
    
    float3 background(float2 vUV) {
    
        float3 c1 = float3(float(0x29)/255., float(0x7f)/255., float(0xaf)/255.);
        float3 c2 = float3(float(0x7d)/255., float(0xd8)/255., float(0xca)/255.);
        float3 c3 = float3(float(0xfe)/255., float(0xe8)/255., float(0xc7)/255.);
        float3 c4 = float3(float(0xfd)/255., float(0xdb)/255., float(0xba)/255.);
				
        float R1 = .4;
        float R2 = .6;
        float R3 = .75;
        float R4 = 1.;
				
        float2 uv = vUV + float2(vUV.x, .03*sin(vUV.x*7.));	
				    
        float3 col = c4;
        if (uv.y< R1) {
            col = c1;
        } else
        if (uv.y < R2) {
            col = mix( c1, c2, (uv.y-R1) / (R2-R1));
        } else
        if (uv.y < R3) {
            col = mix( c2, c3, (uv.y-R2) / (R3-R2));
        } else
        if (uv.y < R4) {
            col = mix( c3, c4, (uv.y-R3) / (R4-R3));
        } 			    
				    
        return col;
    }
    

    half4 main(float2 fragCoord) {
        float TWOPI = 6.283185307179586;
        float F1 = 15.;
        float F00 = 15.;
        float F01 = 10.;
        
        float time = iTime;
					
        float2 uv = fragCoord / iResolution;
        uv.x += sin(mod(uv.y * F00 + time, TWOPI)) * .001 * F01 ;
        uv.y += cos(mod(uv.x * F01 + time, TWOPI)) * .001 * F00;
					
        float det= 2. * sin(mod(time, TWOPI));
        uv.x += sin(mod((uv.y+uv.x) * F1 + time, TWOPI)) / (180. + det);
        uv.y += cos(mod((uv.y+uv.x) * F1 + time, TWOPI)) / (200. + det);        
                
        half4 color = contents.eval(uv*iResolution);
       
        return color;
    }
""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Waves(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {

    val runtimeShader = remember { RuntimeShader(SHADER_SRC) }
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
                runtimeShader.setFloatUniform(
                    "iResolution",
                    it.width.toFloat(),
                    it.height.toFloat()
                )

            }
            .graphicsLayer {
                clip = true
                renderEffect = RenderEffect
                    .createRuntimeShaderEffect(runtimeShader, "contents")
                    .asComposeRenderEffect()
                runtimeShader.setFloatUniform("iTime", timeMs)
            }
    ) {
        content()
    }

}