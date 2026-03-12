package com.example.geo_facts.presentation

import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.example.geo_facts.data.MapState
import com.example.geo_facts.domain.GermanyPaths
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val facts = listOf(
        "This state has beautiful landscapes.",
        "This region is famous for its cuisine.",
        "A major economic hub of Germany.",
        "Known for its historical architecture.",
        "Popular tourist destination.",
        "Home to many cultural festivals."
    )

    private val _states = MutableStateFlow(GermanyPaths.states)
    val states: StateFlow<List<MapState>> = _states

    private val _selectedState = MutableStateFlow<MapState?>(null)
    val selectedState: StateFlow<MapState?> = _selectedState

    private val _currentFact = MutableStateFlow("")
    val currentFact: StateFlow<String> = _currentFact

    fun selectState(name: String) {

        val updated = _states.value.map {
            it.copy(isSelected = it.name == name)
        }

        _states.value = updated
        _selectedState.value = updated.find { it.name == name }

        generateRandomFact()
    }

    fun clearSelection() {

        _states.value = _states.value.map {
            it.copy(isSelected = false)
        }

        _selectedState.value = null
    }

    fun generateRandomFact() {
        _currentFact.value = facts[Random.nextInt(facts.size)]
    }
}