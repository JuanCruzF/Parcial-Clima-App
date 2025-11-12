package com.example.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.data.CiudadStorage
import com.example.template.ui.theme.TemplateTheme
import com.example.template.view.CiudadesView
import com.example.template.view.SelectorCiudadScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemplateTheme {
                val temaViewModel: TemaViewModel = viewModel()
                var ciudadGuardada: String? by remember { mutableStateOf(null) }
                LaunchedEffect(Unit) {
                    ciudadGuardada = CiudadStorage.obtenerCiudad(this@MainActivity)
                }
                if (ciudadGuardada == null) {
                    SelectorCiudadScreen { ciudad ->
                        CiudadStorage.guardarCiudad(this@MainActivity, ciudad)
                        ciudadGuardada = ciudad
                    }
                } else {
                    CiudadesView(temaViewModel = temaViewModel, ciudadInicial = ciudadGuardada!!)
                }
            }
        }
    }
}
