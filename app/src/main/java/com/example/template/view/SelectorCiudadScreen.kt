package com.example.template.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.domain.CitySelectorIntent
import com.example.template.domain.CitySelectorState
import com.example.template.location.LocationProvider
import com.example.template.model.City
import com.example.template.viewmodel.CitySelectorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorCiudadScreen(
    citySelectorViewModel: CitySelectorViewModel = viewModel(),
    onCiudadSeleccionada: (City) -> Unit
) {

    val state by citySelectorViewModel.state.collectAsState(
        initial = CitySelectorState()
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineGranted || coarseGranted) {
            obtenerUbicacionReal(
                context = context,
                onLocation = { lat, lon ->
                    citySelectorViewModel.handleIntent(
                        CitySelectorIntent.BuscarPorUbicacion(lat, lon)
                    )
                },
                onError = {
                    Toast.makeText(
                        context,
                        "No se pudo obtener la ubicación",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                scope = scope
            )
        } else {
            Toast.makeText(
                context,
                "Se necesitan permisos de ubicación para buscar por tu ubicación actual",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccioná tu ciudad") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = state.query,
                onValueChange = { query ->
                    citySelectorViewModel.handleIntent(
                        CitySelectorIntent.QueryChanged(query)
                    )
                },
                label = { Text("Buscar ciudad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val fineGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    val coarseGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (fineGranted || coarseGranted) {
                        obtenerUbicacionReal(
                            context = context,
                            onLocation = { lat, lon ->
                                citySelectorViewModel.handleIntent(
                                    CitySelectorIntent.BuscarPorUbicacion(lat, lon)
                                )
                            },
                            onError = {
                                Toast.makeText(
                                    context,
                                    "No se pudo obtener la ubicación",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            scope = scope
                        )
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📍 Buscar por mi ubicación")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.results) { city ->
                        CityRow(
                            city = city,
                            onClick = { onCiudadSeleccionada(city) }
                        )
                    }
                }
            }
        }
    }
}

private fun obtenerUbicacionReal(
    context: Context,
    onLocation: (Double, Double) -> Unit,
    onError: () -> Unit,
    scope: CoroutineScope
) {
    val locationProvider = LocationProvider(context)

    scope.launch {
        try {
            val location = locationProvider.getLastKnownLocation()
            if (location != null) {
                onLocation(location.latitude, location.longitude)
            } else {
                onError()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError()
        }
    }
}

@Composable
private fun CityRow(
    city: City,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = city.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${city.country} · lat: ${"%.2f".format(city.lat)}, lon: ${"%.2f".format(city.lon)}",
            style = MaterialTheme.typography.bodySmall
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
