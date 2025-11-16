package com.example.template.router

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

    val start = AppRouter.getStartDestination(ciudadInicial)

    NavHost(
        navController = navController,
        startDestination = start
    ) {
        composable("selector") {

            SelectorCiudadScreen(
                onCiudadSeleccionada = { ciudad ->
                    navController.navigate("clima/$ciudad")
                }
            )
        }

        composable("clima/{ciudad}") { backStackEntry ->
            val ciudad = backStackEntry.arguments?.getString("ciudad") ?: ""

            CiudadesView(
                temaViewModel = temaViewModel,
                ciudadInicial = ciudad,
                onNavigateBack = {
                    navController.navigate("selector") {
                        popUpTo("selector") { inclusive = false }
                    }
                }
            )
        }
    }
}
