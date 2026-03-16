package com.example.geo_facts.domain



import android.graphics.RectF
import android.graphics.Region
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.roundToInt
import com.example.geo_facts.data.MapState
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp

@Composable
fun MapCanvas(
    modifier: Modifier,
    states: List<MapState>,
    onStateClick: (String) -> Unit
) {

    val defaultColor = Color(0xFF90CAF9)
    val selectedColor = Color(0xFFEF5350)

    val borderColor = Color.Black
    val borderWidth = 3f

    /**
     * Parse SVG paths once
     */
    val paths = remember(states) {
        states.associate { state ->
            state.name to PathParser()
                .parsePathString(state.svgPath)
                .toPath()
        }
    }

    /**
     * Calculate map bounds
     */
    val mapBounds = remember(paths) {

        var left = Float.MAX_VALUE
        var top = Float.MAX_VALUE
        var right = Float.MIN_VALUE
        var bottom = Float.MIN_VALUE

        paths.values.forEach { path ->

            val b = path.getBounds()

            left = minOf(left, b.left)
            top = minOf(top, b.top)
            right = maxOf(right, b.right)
            bottom = maxOf(bottom, b.bottom)
        }

        Rect(left, top, right, bottom)
    }

    /**
     * Animate colors outside Canvas
     */
    val animatedColors = states.associate { state ->

        val color by animateColorAsState(
            if (state.isSelected) selectedColor else defaultColor,
            label = "stateColor"
        )

        state.name to color
    }
    Box(modifier = modifier
        .fillMaxSize() ,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxHeight(.9f)
                .aspectRatio(mapBounds.width / mapBounds.height)
                .padding(horizontal = 12.dp)
                .pointerInput(states) {

                    detectTapGestures { tap ->

                        val canvasWidth = size.width
                        val scale = canvasWidth / mapBounds.width

                        val mapX = tap.x / scale + mapBounds.left
                        val mapY = tap.y / scale + mapBounds.top

                        paths.forEach { (name, path) ->

                            val androidPath = path.asAndroidPath()

                            val bounds = RectF()
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

                            if (region.contains(mapX.roundToInt(), mapY.roundToInt())) {
                                onStateClick(name)
                            }
                        }
                    }
                }
        ) {

            val canvasWidth = size.width
            val scale = canvasWidth / mapBounds.width

            Log.d("TAG", "MapCanvas: canvasWidth = $canvasWidth, scale = $scale")

            withTransform({

                scale(scale, scale)

                translate(
                    50f,
                    300f
                )

            }) {

                states.forEach { state ->

                    val path = paths[state.name]!!
                    val color = animatedColors[state.name]!!

                    drawPath(
                        path = path,
                        color = color
                    )

                    drawPath(
                        path = path,
                        color = borderColor,
                        style = Stroke(borderWidth)
                    )
                }
            }
        }
    }
}