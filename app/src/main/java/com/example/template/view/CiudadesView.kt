package com.example.template.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//pantalla para buscar o listar ciudades
//hay que cambiar todo el cuerpo del composabe para integrar a api

@Composable
fun CiudadesView(onCiudadSeleccionada: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Acá va la lista o buscadr de ciudades")

        //simular que elegimos una ciudad y navegar
        Button(onClick = onCiudadSeleccionada) {
            Text(text = "Simula la selección de ciudad e ir al Clima")
        }
    }
}
