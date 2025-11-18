package com.example.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.template.data.CiudadStorage
import com.example.template.router.AppNavigation
import com.example.template.ui.theme.ClimaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClimaAppTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {
    var ciudadGuardada: String? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        ciudadGuardada = CiudadStorage.obtenerCiudad(context)
    }

    AppNavigation(ciudadInicial = ciudadGuardada)
}
