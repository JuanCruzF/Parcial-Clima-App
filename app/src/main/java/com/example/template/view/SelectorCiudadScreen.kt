package com.example.template.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectorCiudadScreen(onCiudadSeleccionada: (String) -> Unit) {
    val ciudades = listOf(
        "Buenos Aires",
        "Córdoba",
        "Rosario",
        "La Plata",
        "Mar del Plata",
        "San Miguel de Tucumán",
        "Salta",
        "Santa Fe",
        "Corrientes",
        "Mendoza"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Selecciona tu ciudad:")
        Spacer(modifier = Modifier.height(24.dp))
        ciudades.forEach { ciudad ->
            Button(
                onClick = { onCiudadSeleccionada(ciudad) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(ciudad)
            }
        }
    }
}
