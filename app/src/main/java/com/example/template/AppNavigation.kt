package com.example.template

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.template.view.CiudadesView
import com.example.template.view.SelectorCiudadScreen

@Composable
fun AppNavigation(ciudadInicial: String?) {
    val navController = rememberNavController()
    val temaViewModel: TemaViewModel = viewModel()

    NavHost(navController = navController, startDestination = if (ciudadInicial.isNullOrBlank()) "selector" else "ciudades/{ciudad}") {
        composable("selector") {
            SelectorCiudadScreen {
                navController.navigate("ciudades/$it")
            }
        }
        composable("ciudades/{ciudad}") {
            val ciudad = it.arguments?.getString("ciudad") ?: ciudadInicial
            CiudadesView(temaViewModel = temaViewModel, ciudadInicial = ciudad!!, onNavigateBack = { navController.popBackStack() })
        }
    }
}
