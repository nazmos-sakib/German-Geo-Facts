package com.example.geo_facts.domain



import android.graphics.Region
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.roundToInt
import com.example.geo_facts.data.MapState


@Composable
fun MapCanvas(
    states: List<MapState>,
    onStateClick: (String) -> Unit
) {

    val defaultColor = Color(0xFF90CAF9)
    val selectedColor = Color(0xFFEF5350)

    val paths = remember { mutableStateMapOf<String, Path>() }

    val animatedStates = states.map { state ->

        val color by animateColorAsState(
            if (state.isSelected) selectedColor else defaultColor,
            label = "colorAnim"
        )

        val scale by animateFloatAsState(
            if (state.isSelected) 1.05f else 1f,
            label = "scaleAnim"
        )

        Triple(state, color, scale)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(states) {

                detectTapGestures { offset ->

                    paths.forEach { (name, path) ->

                        val androidPath = path.asAndroidPath()

                        val bounds = android.graphics.RectF()
                        androidPath.computeBounds(bounds, true)

                        val region = Region()

                        region.setPath(
                            androidPath,
                            Region(
                                bounds.left.roundToInt(),
                                bounds.top.roundToInt(),
                                bounds.right.roundToInt(),
                                bounds.bottom.roundToInt()
                            )
                        )

                        if (region.contains(offset.x.roundToInt(), offset.y.roundToInt())) {
                            onStateClick(name)
                        }
                    }
                }
            }
    ) {

        animatedStates.forEach { (state, color, scale) ->

            val path = paths.getOrPut(state.name) {
                PathParser().parsePathString(state.svgPath).toPath()
            }

            withTransform({

                val bounds = path.getBounds()
                val cx = bounds.center.x
                val cy = bounds.center.y

                translate(cx, cy)
                scale(scale, scale)
                translate(-cx, -cy)

            }) {

                drawPath(
                    path = path,
                    color = color
                )
            }
        }
    }
}