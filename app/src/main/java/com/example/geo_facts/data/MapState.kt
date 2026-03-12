package com.example.geo_facts.data

import androidx.compose.ui.graphics.Path

data class MapState(
    val name: String,
    val svgPath: String,
    val isSelected: Boolean = false
)

