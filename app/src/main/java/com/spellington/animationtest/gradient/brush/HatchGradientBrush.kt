package com.spellington.animationtest.gradient.brush

import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush
import com.spellington.animationtest.util.GradientSampler
import com.spellington.animationtest.util.GradientSamplerOrientation

class HatchGradientShader() {

    val shader = RuntimeShader(HATCH_GRADIENT_SHADER)

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


    private var _time: Float = 0f
    var time: Float
        get() = _time
        set(value) {
            if (_time == value) return
            _time = value
            shader.setFloatUniform("iTime", _time)
        }

    private var _amplitude: Float = 1f
    var amplitude: Float
        get() = _amplitude
        set(value) {
            if (_amplitude == value) return
            _amplitude = value
            shader.setFloatUniform("iAmplitude", _amplitude)
        }

    private var _peaks: Float = 1f
    var peaks: Float
        get() = _peaks
        set(value) {
            if (_peaks == value) return
            _peaks = value
            shader.setFloatUniform("iPeaks", _peaks)
        }

    private var _sampler: Shader? = null
    var sampler: Shader?
        get() = _sampler
        set(value) {
            if (value == null ||_sampler == value) return
            _sampler = value
            shader.setInputShader("gradient", value)
        }

    private var _direction = GradientSamplerOrientation.Horizontal
    var  direction: GradientSamplerOrientation
        get() = _direction
        set(value) {

            if (_direction == value) return
            _direction = value
            shader.setFloatUniform("iDirection", _direction.toFloat())
        }

    private var _angle: Float = 0f
    var angle: Float
        get() = _angle
        set(value) {
            if (_angle == value) return
            _angle = value
            shader.setFloatUniform("iAngle", _angle)
        }

    init {
        shader.setFloatUniform("iTime", 0f)
        shader.setFloatUniform("iTimeScale", timeScale)
        shader.setFloatUniform("iAmplitude", amplitude)
        shader.setFloatUniform("iDirection", direction.toFloat())
        shader.setFloatUniform("iPeaks", peaks)
        shader.setFloatUniform("iAngle", angle)
    }


    companion object {
        private const val HATCH_GRADIENT_SHADER = """
                    
            uniform shader gradient;
            uniform float2 iResolution;
            uniform float iTime;
            uniform float iDirection;
            uniform float iTimeScale;       // make it slower(smaller value) or faster.
            uniform float iAmplitude;
            uniform float iPeaks;
            uniform float iAngle;
        
            float2 hatchyH(float2 uv, float stripes, float factor) {
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
        
            float2 hatchyV(float2 uv, float stripes, float factor) {
                float displacement = stripes * uv.x + iTime;
                float repeat = fract(displacement);
                int index = int(displacement);
                if (mod(float(index),2)==0) {
                    uv.y += factor*repeat;
                } else {
                    uv.y += factor*(1.-repeat);
                }
                
                return uv;
            }
        
            float2 rotate(float2 uv, float angle, float aspect) {
                // rotate uv around the center of the composable
                float c = cos(angle);
                float s = sin(angle);
                mat2 mat = mat2(c,s,-s,c);

                float2 center =  float2(aspect, 1) * .5;
                float2 nuv = uv;
                
                nuv -= center;
                nuv = mat * nuv;
                nuv += center;
                return nuv;
            }
            
            half4 main(float2 fragCoord) {
                float2 uv = fragCoord.xy/iResolution.xy;

                float aspect = iResolution.x / iResolution.y;

                uv.x *= aspect; 
                uv = rotate(uv, iAngle, aspect);
                                                
                if (iDirection > 0)
                    uv = hatchyH(uv, iPeaks, iAmplitude);
                else 
                    uv = hatchyV(uv, iPeaks, iAmplitude);

                half4 color = gradient.eval(uv);
                return color;
            }
        """

    }
}

class HatchGradientBrush(
    sampler: GradientSampler,
    time: Float = 0f,
    timeScale: Float = .1f,
    amplitude: Float = 1f,
    peaks: Float = 4f,
    angle: Float = 0f,
) : ShaderBrush() {

    private val shader = HatchGradientShader()
    private var internalSize: Size = Size.Zero

    init {
        setSampler(sampler)
        setTimeScale(timeScale)
        setAmplitude(amplitude)
        setTime(time)
        setPeaks(peaks)
        setAngle(angle)
    }

    override fun createShader(size: Size): Shader {
        internalSize = size
        shader.resolution = size
        return shader.shader
    }

    fun setAmplitude(amplitude: Float) {
        shader.amplitude = amplitude
    }

    fun setTime(time: Float) {
        shader.time = time
    }

    fun setSampler(sampler: GradientSampler) {
        shader.sampler = sampler.sampler
        shader.direction = sampler.orientation
    }

    fun setTimeScale(timeScale: Float) {
        shader.timeScale = timeScale
    }

    fun setPeaks(peaks: Float) {
        shader.peaks = peaks * 2
    }

    fun setAngle(angle: Float) {
        shader.angle = angle
    }
}
