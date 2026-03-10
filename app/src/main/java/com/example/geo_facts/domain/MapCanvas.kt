package com.example.geo_facts.domain



import android.graphics.Region
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import com.example.geo_facts.data.MapState

@Composable
fun MapCanvas(
    states: List<MapState>,
    selectedState: MapState?,
    onStateSelected: (MapState) -> Unit
) {
    // Create animated states for each state BEFORE the Canvas
    val animatedStates = states.associate { state ->
        val isSelected = state.name == selectedState?.name

        val animatedColor by animateColorAsState(
            targetValue = if (isSelected) state.selectedColor else state.defaultColor,
            label = "colorAnimation_${state.name}"
        )

        val scaleFactor by animateFloatAsState(
            targetValue = if (isSelected) 1.05f else 1f,
            label = "scaleAnimation_${state.name}"
        )

        state.name to Pair(animatedColor, scaleFactor)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    states.forEach { state ->
                        if (state.path.contains(offset)) {
                            onStateSelected(state)
                        }
                    }
                }
            }
    ) {
        states.forEach { state ->
            val (animatedColor, scaleFactor) = animatedStates[state.name] ?: return@forEach

            scale(scaleFactor, scaleFactor, pivot = state.path.getBounds().center) {
                drawPath(
                    path = state.path,
                    color = animatedColor
                )
            }
        }
    }
}

private fun Path.contains(point: Offset): Boolean {
    val androidPath = this.asAndroidPath()
    val bounds = android.graphics.RectF()
    androidPath.computeBounds(bounds, true)
    val region = Region().apply {
        setPath(
            androidPath,
            Region(
                bounds.left.toInt(),
                bounds.top.toInt(),
                bounds.right.toInt(),
                bounds.bottom.toInt()
            )
        )
    }
    return region.contains(point.x.toInt(), point.y.toInt())
}
