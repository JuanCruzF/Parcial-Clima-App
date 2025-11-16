package com.example.template.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.data.CiudadStorage
import com.example.template.domain.CitySelectorIntent
import com.example.template.viewmodel.CitySelectorViewModel

@Composable
fun SelectorCiudadScreen(
    onCiudadSeleccionada: (String) -> Unit,
    citySelectorViewModel: CitySelectorViewModel = viewModel()
) {
    val state by citySelectorViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        citySelectorViewModel.handleIntent(CitySelectorIntent.LoadInitial)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Seleccioná tu ciudad",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.query,
            onValueChange = {
                citySelectorViewModel.handleIntent(CitySelectorIntent.QueryChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar ciudad (ej: Buenos Aires)") }
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val (lat, lon) = obtenerUbicacionOSimilar(context)
                citySelectorViewModel.handleIntent(
                    CitySelectorIntent.BuscarPorUbicacion(lat, lon)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Usar mi ubicación")
        }

        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (state.error != null) {
            Text(
                text = state.error ?: "",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.results) { city ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // ✅ GUARDAR CIUDAD
                            CiudadStorage.guardarCiudad(context, city.name)
                            onCiudadSeleccionada(city.name)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "${city.name}, ${city.country}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "(${city.lat}, ${city.lon})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private fun obtenerUbicacionOSimilar(context: Context): Pair<Double, Double> {
    return -34.6037 to -58.3816
}
