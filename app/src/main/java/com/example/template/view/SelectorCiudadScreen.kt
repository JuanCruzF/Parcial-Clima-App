package com.example.template.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.domain.CitySelectorIntent
import com.example.template.viewmodel.CitySelectorViewModel

@Composable
fun SelectorCiudadScreen(
    onCiudadSeleccionada: (String) -> Unit,
    citySelectorViewModel: CitySelectorViewModel = viewModel()
) {
    val state by citySelectorViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        citySelectorViewModel.handleIntent(CitySelectorIntent.LoadInitial)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Selecciona tu ciudad",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.query,
            onValueChange = {
                citySelectorViewModel.handleIntent(CitySelectorIntent.QueryChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar ciudad (ej: Buenos Aires)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.results) { city ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCiudadSeleccionada(city.name) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "${city.name}, ${city.country}")
                    Text(
                        text = "(${city.lat}, ${city.lon})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
