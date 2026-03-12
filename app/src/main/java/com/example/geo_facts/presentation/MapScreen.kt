package com.example.geo_facts.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.geo_facts.domain.MapCanvas
import androidx.lifecycle.compose.collectAsStateWithLifecycle
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MainViewModel) {

    val states by viewModel.states.collectAsStateWithLifecycle()
    val selectedState by viewModel.selectedState.collectAsStateWithLifecycle()
    val fact by viewModel.currentFact.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        MapCanvas(
            states = states,
            onStateClick = { viewModel.selectState(it) }
        )

        selectedState?.let { state ->

            ModalBottomSheet(
                onDismissRequest = { viewModel.clearSelection() },
                sheetState = sheetState
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(
                        text = state.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = fact)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.generateRandomFact() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Generate Another Fact")
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}