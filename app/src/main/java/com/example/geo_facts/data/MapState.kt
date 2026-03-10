package com.example.geo_facts.data

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color

data class MapState(
    val name: String,
    val path: Path,
    val defaultColor: Color,
    val selectedColor: Color
)

