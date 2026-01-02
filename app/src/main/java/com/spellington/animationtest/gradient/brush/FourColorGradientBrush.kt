package com.spellington.animationtest.gradient.brush


import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb

class FourColorShader() {

    val shader: RuntimeShader = RuntimeShader(SPIRAL_GRADIENT_SHADER)

    private var _resolution: Size = Size.Zero
    var resolution: Size
        get() = _resolution
        set(value) {
            if (_resolution == value) return
            _resolution = value
            shader.setFloatUniform("iResolution", _resolution.width, _resolution.height)
        }

    private var _timeScale = .1f
    var timeScale: Float
        get() = _timeScale
        set(value) {
            if (_timeScale == value) return
            _timeScale = value
            shader.setFloatUniform("iTimeScale", _timeScale)
        }

    private var _center: Offset = Offset(.5f, .5f)
    var center: Offset
        get() = _center
        set(value) {
            if (_center ==value) return
            _center = value
            shader.setFloatUniform("iCenter", _center.x, _center.y)
        }

    private var _time: Float = 0f
    var time: Float
        get() = _time
        set(value) {
            if (_time == value) return
            _time = value
            shader.setFloatUniform("iTime", value)
        }
    
    private var _topLeftColor: Color = Color.Unspecified
    var topLeftColor: Color
        get() = _topLeftColor
        set(value) {
            if (_topLeftColor == value) return
            _topLeftColor = value
            shader.setColorUniform("iColorTopLeft", _topLeftColor.toArgb())
        }
    
    private var _topRightColor: Color = Color.Unspecified
    var topRightColor: Color
        get() = _topRightColor
        set(value) {
            if (_topRightColor == value) return
            _topRightColor = value
            shader.setColorUniform("iColorTopRight", _topRightColor.toArgb())
        }
    
    private var _bottomLeftColor: Color = Color.Unspecified
    var bottomLeftColor: Color
        get() = _bottomLeftColor
        set(value) {
            if (_bottomLeftColor == value) return
            _bottomLeftColor = value
            shader.setColorUniform("iColorBottomLeft", _bottomLeftColor.toArgb())
        }
    
    private var _bottomRightColor: Color = Color.Unspecified
    var bottomRightColor: Color
        get() = _bottomRightColor
        set(value) {
            if (_bottomRightColor == value) return
            _bottomRightColor = value
            shader.setColorUniform("iColorBottomRight", _bottomRightColor.toArgb())
        }


    init {

        shader.setFloatUniform("iTime", 0f)
        shader.setFloatUniform("iCenter", center.x, center.y)
        shader.setFloatUniform("iTimeScale", timeScale)
    }

    companion object {
        private const val SPIRAL_GRADIENT_SHADER = """

    uniform float2 iResolution;
    uniform float iTime;
    uniform float iTimeScale;       // make it slower(smaller value) or faster.
    uniform float2 iCenter;         // 0.5f by default
    layout(color) uniform half4 iColorTopLeft;
    layout(color) uniform half4 iColorTopRight;
    layout(color) uniform half4 iColorBottomLeft;
    layout(color) uniform half4 iColorBottomRight;

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / iResolution;
        
        uv -= iCenter;
        float a = iTime * iTimeScale;
        float c = cos(a);
        float s = sin(a);
        uv = mat2(c,-s,s,c) * uv;
        uv += iCenter;
        
        half4 colorTop = mix(iColorTopLeft, iColorTopRight, uv.x);
        half4 colorBottom = mix(iColorBottomLeft, iColorBottomRight, uv.x); 
        
        return mix(colorTop, colorBottom, uv.y);
    }
"""
    }
}

/**
 * A ShaderBrush that creates an animated spiral gradient.
 *
 * This brush uses a RuntimeShader to transform UV coordinates into a spiral,
 * then applies a linear gradient to those coordinates.
 *
 */
class FourColorGradientBrush(
    topLeft: Color = Color.Unspecified,
    topRight: Color  = Color.Unspecified,
    bottomLeft: Color  = Color.Unspecified,
    bottomRight: Color  = Color.Unspecified,
    timeScale: Float = .1f,
    center: Offset = Offset(.5f, .5f),
) : ShaderBrush() {

    private var internalSize =Size.Zero
    val shader = FourColorShader()

    init {
        shader.timeScale = timeScale
        shader.center = center
        shader.topLeftColor = topLeft
        shader.topRightColor = topRight
        shader.bottomLeftColor = bottomLeft
        shader.bottomRightColor = bottomRight
    }

    override fun createShader(size: Size): Shader {
        internalSize = size
        shader.resolution = size
        return shader.shader
    }

    fun setTime(time: Float) {
        shader.time = time
    }

    fun setCenter(center: Offset) {
        shader.center = center
    }

    fun setTimeScale(timeScale: Float) {
        shader.timeScale = timeScale
    }

    fun setTopLeftColor(c: Color) {
        shader.topLeftColor = c
    }
    
    fun setTopRightColor(c: Color) {
        shader.topRightColor= c
    }
    
    fun setBottomLeftColor(c: Color) {
        shader.bottomLeftColor = c
    }
    
    fun setBottomRightColor(c: Color) {
        shader.bottomRightColor
    }

}