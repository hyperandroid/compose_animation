package com.spellington.animationtest.gradient.brush

import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush


class FlowerShader() {

    val shader: RuntimeShader = RuntimeShader(FLOWER_GRADIENT_SHADER)

    private var _petals = 5f
    var petals: Float
        get() = _petals
        set(value) {
            if (_petals == value) return
            _petals = value
            shader.setFloatUniform("iFlowerPetals", _petals)
        }

    private var _rotationTimeScale = .1f
    var rotationTimeScale: Float
        get() = _rotationTimeScale
        set(value) {
            if (_rotationTimeScale == value) return
            _rotationTimeScale = value
            shader.setFloatUniform("iRotationTimeScale", _rotationTimeScale)
        }

    private var _inOutTimeScale = .1f
    var inOutTimeScale: Float
        get() = _inOutTimeScale
        set(value) {
            if (_inOutTimeScale == value) return
            _inOutTimeScale = value
            shader.setFloatUniform("iInOutTimeScale", _inOutTimeScale)
        }

    private var _resolution: Size = Size.Zero
    var resolution: Size
        get() = _resolution
        set(value) {
            if (_resolution == value) return
            _resolution = value
            shader.setFloatUniform("iResolution", _resolution.width, _resolution.height)
        }

    private var _direction = SpiralGradientDirection.Out
    var direction: SpiralGradientDirection
        get() = _direction
        set(value) {

            if (_direction == value) return
            _direction = value
            shader.setFloatUniform("iDirection", _direction.toFloat())
        }

    private var _rotationDirection = RotationDirection.Clockwise
    var rotationDirection: RotationDirection
        get() = _rotationDirection
        set(value) {

            if (_rotationDirection == value) return
            _rotationDirection = value
            shader.setFloatUniform("iRotationDirection", _rotationDirection.toFloat())
        }

    private var _wobblyFactor = 0f
    var wobblyFactor: Float
        get() = _wobblyFactor
        set(value) {
            if (_wobblyFactor == value) return
            _wobblyFactor = value
            shader.setFloatUniform("iWobblyFactor", _wobblyFactor)
        }

    private var _petalInfluence = 1f
    var petalInfluence: Float
        get() = _petalInfluence
        set(value) {
            if (_petalInfluence == value) return
            _petalInfluence = value
            shader.setFloatUniform("iPetalInfluence", _petalInfluence)
        }

    private var _center: Offset = Offset(.5f, .5f)
    var center: Offset
        get() = _center
        set(value) {
            if (_center == value) return
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


    private var _sampler: Shader? = null
    var sampler: Shader?
        get() = _sampler
        set(value) {
            if (value == null || _sampler == value) return
            _sampler = value
            shader.setInputShader("gradient", value)
        }

    init {
        shader.setFloatUniform("iTime", 0f)
        shader.setFloatUniform("iFlowerPetals", _petals)
        shader.setFloatUniform("iRotationTimeScale", _rotationTimeScale)
        shader.setFloatUniform("iInOutTimeScale", _inOutTimeScale)
        shader.setFloatUniform("iDirection", _direction.toFloat())
        shader.setFloatUniform("iRotationDirection", _rotationDirection.toFloat())
        shader.setFloatUniform("iWobblyFactor", _wobblyFactor)
        shader.setFloatUniform("iPetalInfluence", _petalInfluence)
        shader.setFloatUniform("iCenter", center.x, center.y)
    }

    companion object {
        private const val FLOWER_GRADIENT_SHADER = """
    
    uniform shader gradient;   
    uniform float2 iResolution; 
    uniform float iTime;
    uniform float iRotationTimeScale;   // make if slower(smaller value) or faster.
    uniform float iInOutTimeScale;   // make if slower(smaller value) or faster.
    uniform float iFlowerPetals;         // bigger more spiral-ish
    uniform float2 iCenter;             // 0.5f by default
    uniform float iPetalInfluence;
    uniform float iRotationDirection;   // 1 clockwise, -1 counterclockwise
    uniform float iDirection;           // in/out
    uniform float iWobblyFactor;
    
    float2 polar(float2 uv, float spiral, float rotationTime, float inOutTime) {
        uv = 2*(uv-iCenter);
        
        if (iResolution.x < iResolution.y) {
            float aspect = iResolution.y/iResolution.x;
            uv.y *= aspect;
        } else {
            float aspect = iResolution.x/iResolution.y;
            uv.x *= aspect;
        }
  
        float pi = 3.1415926;
        float radius = length(uv);
        float angle = atan(uv.y, uv.x) + rotationTime * iRotationDirection + iWobblyFactor*sin(pi*radius+rotationTime); 
        

        float t = radius + iPetalInfluence * sin((angle + pi)*spiral) + inOutTime;
        
        return float2(t, 0);
    }
    
    half4 main(float2 fragCoord) {	    
        float2 uv = fragCoord / iResolution;
        
        uv = polar(
            uv, 
            iFlowerPetals, 
            iTime*iRotationTimeScale*iRotationDirection,
            iTime*iInOutTimeScale*iDirection
        );
        
        half4 color = gradient.eval(uv);
       
        return color;
    }
"""

    }
}

enum class RotationDirection {
    Clockwise,
    CounterClockwise
    ;

    fun toFloat() = if (this == Clockwise) -1f else 1f
}

class PolarGradientBrush(
    sampler: Shader,
    flowerPetals: Float = 5f,
    rotationTimeScale: Float = .1f,
    inOutTimeScale: Float = .1f,
    center: Offset = Offset(.5f, .5f),
    petalInfluence: Float = 1f,
    wobblyFactor: Float = 0f,
    direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    rotationDirection: RotationDirection = RotationDirection.Clockwise,
) : ShaderBrush() {

    private var internalSize = Size.Zero
    val shader = FlowerShader()

    init {
        shader.apply {
            setSampler(sampler)
            setPetals(flowerPetals)
            setRotationTimeScale(rotationTimeScale)
            setInOutTimeScale(inOutTimeScale)
            setCenter(center)
            setPetalInfluence(petalInfluence)
            setWobblyFactor(wobblyFactor)
            setDirection(direction)
            setRotationDirection(rotationDirection)
        }
    }

    override fun createShader(size: Size): Shader {
        internalSize = size
        shader.resolution = size
        return shader.shader
    }

    fun setTime(time: Float) {
        shader.time = time
    }

    fun setSampler(shader: Shader) {
        this.shader.sampler = shader
    }

    fun setPetals(count: Float) {
        shader.petals = count
    }

    fun setRotationTimeScale(rotationTimeScale: Float) {
        shader.rotationTimeScale = rotationTimeScale
    }

    fun setInOutTimeScale(inOutTimeScale: Float) {
        shader.inOutTimeScale = inOutTimeScale
    }

    fun setCenter(center: Offset) {
        shader.center = center
    }

    fun setPetalInfluence(petalInfluence: Float) {
        shader.petalInfluence = petalInfluence
    }

    fun setDirection(direction: SpiralGradientDirection) {
        shader.direction = direction
    }

    fun setRotationDirection(rotationDirection: RotationDirection) {
        shader.rotationDirection = rotationDirection
    }

    fun setWobblyFactor(wobblyFactor: Float) {
        shader.wobblyFactor = wobblyFactor
    }
}