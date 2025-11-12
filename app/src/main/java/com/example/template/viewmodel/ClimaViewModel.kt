package com.example.template.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.domain.ClimaIntent
import com.example.template.domain.ClimaState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * El "Cerebro" de nuestra pantalla de Clima.
 * Sigue el patrón MVI.
 */
class ClimaViewModel : ViewModel() {

    // STATE
    private val _state = MutableStateFlow(ClimaState())
    val state = _state.asStateFlow()


    // INTENT
    fun handleIntent(intent: ClimaIntent) {
        when (intent) {
            is ClimaIntent.CargarClimaInicial -> {
                cargarClima()
            }
        }
    }

    //LÓGICA
    private fun cargarClima() {
        viewModelScope.launch {

            //modo cargando y limpiar errores
            _state.update { it.copy(isLoading = true, error = null) }

            //simulamos la llamada a la API
            try {
                // demora de red de 2 segundos
                delay(2000)

                // datos "falsos"
                _state.update {
                    it.copy(
                        isLoading = false,
                        ciudad = "Buenos Aires",
                        temperatura = 25.4,
                        descripcion = "Mayormente Soleado"
                    )
                }

            } catch (e: Exception) {
                // futuro error en caso de que falle la api
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el clima. Intente más tarde."
                    )
                }
            }
        }
    }
}