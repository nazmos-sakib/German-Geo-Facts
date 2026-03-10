package com.example.geo_facts.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.geo_facts.domain.MapCanvas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MainViewModel) {

    val selectedState by viewModel.selectedState.collectAsState()
    val fact by viewModel.currentFact.collectAsState()

    val sheetState = rememberModalBottomSheetState()

    if (selectedState != null) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = selectedState!!.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = fact)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { viewModel.generateRandomFact() }) {
                    Text("Generate Another Fact")
                }
            }
        }
    }

    MapCanvas(
        states = viewModel.states,
        selectedState = selectedState,
        onStateSelected = { viewModel.onStateSelected(it) }
    )
}