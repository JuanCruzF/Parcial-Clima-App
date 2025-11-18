package com.example.template.router

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.template.view.CiudadesView
import com.example.template.view.SelectorCiudadScreen
import com.example.template.viewmodel.TemaViewModel

@Composable
fun AppNavigation(ciudadInicial: String?) {
    val navController = rememberNavController()
    val temaViewModel: TemaViewModel = viewModel()

    // Si hay ciudad guardada, arrancamos en clima/ciudad-encodeada, si no en selector
    val startDestination =
        if (ciudadInicial.isNullOrBlank()) {
            "selector"
        } else {
            val encoded = Uri.encode(ciudadInicial)
            "clima/$encoded"
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("selector") {
            SelectorCiudadScreen(
                onCiudadSeleccionada = { city ->

                    val encodedName = Uri.encode(city.name)
                    navController.navigate("clima/$encodedName")
                }
            )
        }

        composable("clima/{ciudad}") { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("ciudad") ?: ""

            val ciudadNombre = Uri.decode(encoded)

            CiudadesView(
                temaViewModel = temaViewModel,
                ciudadInicial = ciudadNombre,
                onNavigateBack = {
                    navController.navigate("selector") {
                        popUpTo("selector") { inclusive = true }
                    }
                }
            )
        }
    }
}
