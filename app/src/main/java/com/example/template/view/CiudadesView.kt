package com.example.template.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.viewmodel.SettingsViewModel

//pantalla para buscar o listar ciudades

@Composable
fun CiudadesView(onCiudadSeleccionada: () -> Unit) {

    // se obtiene la instancia del SettingsViewModel
    val settingsViewModel: SettingsViewModel = viewModel()

    //TODO con api reemplazar el coumn textfield → llamado API → resultado lazycolumn
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Acá va la lista o buscador de ciudades")

        Button(onClick = {

            //logica de guardado

            // cuando se tenga el buscador esta variable viene de ahí
            //simulado
            val ciudadSimulada = "Buenos Aires"

            // se guarda al ciudad mediante el vm. el guardado quedaría así (definitivo)
            settingsViewModel.saveCity(ciudadSimulada)

            // navegacion
            onCiudadSeleccionada()
        }) {
            Text(text = "Buenos Aires (simulado)")
        }
    }
}