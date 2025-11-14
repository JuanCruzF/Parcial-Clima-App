package com.example.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.template.ui.theme.ClimaAppTheme
import com.example.template.view.CiudadesView
import com.example.template.view.ClimaView
import com.example.template.viewmodel.TemaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // tema
            val temaViewModel: TemaViewModel = viewModel()
            val isDark by temaViewModel.isDarkMode.collectAsState()

            ClimaAppTheme(darkTheme = isDark) {

                // flujo condicional

                // simulacion pero va a venir de SharedPreferences o DataStore)
                val hayCiudadGuardada = false

                // loogica pantalla de arranque
                val startDestination = if (hayCiudadGuardada) {
                    "clima" // si hay ciudad, vamos al clima
                } else {
                    "ciudades" // Si no hay, vamos a la lista
                }


                // controlador para manear pantallas
                val navController = rememberNavController()

                // contenedor donde se van a mostrar las pantallas
                NavHost(navController = navController, startDestination = startDestination) {

                    // lista/buscador de ciudades
                    composable(route = "ciudades") {
                        CiudadesView(
                            onCiudadSeleccionada = {
                                navController.navigate("clima")
                            }
                        )
                    }

                    //detalles del clima
                    composable(route = "clima") {
                        ClimaView(temaViewModel = temaViewModel)
                    }
                }
            }
        }
    }
}