package com.spellington.animationtest

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainWidth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun Modifier.marquee(
    duration: Int = 3000,
): Modifier =
    this.then(MarqueeModifierElement(
            duration = duration
    ))

internal data class MarqueeModifierElement(val duration: Int):
    ModifierNodeElement<MarqueeModifierNode>()
{
    override fun create() = MarqueeModifierNode(
        duration = duration,
    )

    override fun update(node: MarqueeModifierNode) {
        node.update(
            duration = duration,
        )
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "marquee"
        properties["duration"] = duration
    }
}

internal class MarqueeModifierNode(
    private var duration: Int = 5000
):
    Modifier.Node(),
    LayoutModifierNode,
    DrawModifierNode
{
    private var containerWidth: Int = 0
    private var contentsWidth: Int = 0
    private val offset = Animatable(0f)
    private var animationJob: Job? = null

    override fun onDetach() {
        animationJob?.cancel()
        animationJob = null
    }

    fun update(
        duration: Int
    ) {
        if (this.duration != duration) {
            this.duration = duration
            restartMarquee()
        }
    }

    private fun restartMarquee() {
        val previousJob = animationJob
        previousJob?.cancel()

        animationJob = coroutineScope.launch {

            previousJob?.join()

            withContext(object : MotionDurationScale {
                override val scaleFactor: Float
                    get() = 1f
            }) {

                offset.snapTo(containerWidth.toFloat())
                offset.animateTo(
                    -contentsWidth.toFloat(),
                    infiniteRepeatable(
                        animation = tween(
                            durationMillis = this@MarqueeModifierNode.duration,
                            easing = LinearEasing
                        ),
                    )
                )

            }
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        // unbounded horizontally
        val childConstraints = constraints.copy(maxWidth = Constraints.Infinity)
        val placeable = measurable.measure(childConstraints)

        val newContainerWidth = constraints.constrainWidth(placeable.width)
        val newContentsWidth = placeable.width

        val sizeChanged = newContainerWidth != containerWidth
                || newContentsWidth != contentsWidth


        // The width used for layout, constrained by the parent
        containerWidth = newContainerWidth

        // The full width of the composable we want to scroll
        contentsWidth = newContentsWidth

        if (isAttached && sizeChanged) {
            restartMarquee()
        }

        return layout(containerWidth, placeable.height) {
            placeable.placeWithLayer(
                x = 0,
                y = 0
            )
        }
    }

    override fun ContentDrawScope.draw() {
        // clip defaults to the whole height of the Text container.
        clipRect(left = 0f, right = containerWidth.toFloat()) {
            // offset goes from containerWidth to -contentsWidth
            // which effectively makes the text slide right to left until
            // it completely disappears.
            translate(left = offset.value) {
                this@draw.drawContent()
            }
        }
    }
}
