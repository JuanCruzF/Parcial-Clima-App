package com.example.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.template.data.CiudadStorage
import com.example.template.ui.theme.TemplateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemplateTheme {
                var ciudadGuardada: String? by remember { mutableStateOf(null) }
                LaunchedEffect(Unit) {
                    ciudadGuardada = CiudadStorage.obtenerCiudad(this@MainActivity)
                }
                // Usamos AppNavigation para que gestione las pantallas
                AppNavigation(ciudadInicial = ciudadGuardada)
            }
        }
    }
}
