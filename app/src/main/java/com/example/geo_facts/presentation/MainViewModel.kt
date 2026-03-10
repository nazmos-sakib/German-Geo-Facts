package com.example.geo_facts.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.example.geo_facts.data.MapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val facts = listOf(
        "This state is known for its beautiful landscapes.",
        "A major historical event took place here.",
        "Famous for traditional cuisine.",
        "Home to several UNESCO heritage sites.",
        "This region has a strong industrial economy."
    )

    private val _selectedState = MutableStateFlow<MapState?>(null)
    val selectedState: StateFlow<MapState?> = _selectedState

    private val _currentFact = MutableStateFlow("")
    val currentFact: StateFlow<String> = _currentFact

    val states: List<MapState> = createMockStates()

    private fun createMockStates(): List<MapState> {
        return listOf(
            MapState("Bavaria", createRectanglePath(100f, 600f, 700f, 1000f),
                Color(0xFFBBDEFB), Color(0xFF1976D2)),
            MapState("Hesse", createRectanglePath(100f, 300f, 700f, 600f),
                Color(0xFFC8E6C9), Color(0xFF388E3C)),
            MapState("Saxony", createRectanglePath(700f, 300f, 1200f, 600f),
                Color(0xFFFFE0B2), Color(0xFFF57C00)),
            MapState("Lower Saxony", createRectanglePath(100f, 50f, 700f, 300f),
                Color(0xFFFFCDD2), Color(0xFFD32F2F)),
            MapState("Brandenburg", createRectanglePath(700f, 50f, 1200f, 300f),
                Color(0xFFE1BEE7), Color(0xFF7B1FA2))
        )
    }

    private fun createRectanglePath(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ): Path {
        return Path().apply {
            moveTo(left, top)
            lineTo(right, top)
            lineTo(right, bottom)
            lineTo(left, bottom)
            close()
        }
    }

    fun onStateSelected(state: MapState) {
        _selectedState.value = state
        generateRandomFact()
    }

    fun generateRandomFact() {
        _currentFact.value = facts[Random.nextInt(facts.size)]
    }
}