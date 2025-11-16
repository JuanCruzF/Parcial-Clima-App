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

// El "cerebro" de la pantalla clima
//sigue el patron MVI
class ClimaViewModel : ViewModel() {

    // STATE
    private val _state = MutableStateFlow(ClimaState())
    val state = _state.asStateFlow()

    // Datos falsos para simular una API o repositorio
    private val climasFalsos = mapOf(
        "Buenos Aires" to ClimaState(ciudad = "Buenos Aires", temperatura = 25.4, descripcion = "Mayormente Soleado"),
        "Córdoba" to ClimaState(ciudad = "Córdoba", temperatura = 28.1, descripcion = "Soleado"),
        "Rosario" to ClimaState(ciudad = "Rosario", temperatura = 26.5, descripcion = "Parcialmente Nublado"),
        "La Plata" to ClimaState(ciudad = "La Plata", temperatura = 24.9, descripcion = "Lluvias débiles"),
        "Mar del Plata" to ClimaState(ciudad = "Mar del Plata", temperatura = 22.0, descripcion = "Ventoso"),
        "San Miguel de Tucumán" to ClimaState(ciudad = "San Miguel de Tucumán", temperatura = 31.0, descripcion = "Muy caluroso"),
        "Salta" to ClimaState(ciudad = "Salta", temperatura = 29.5, descripcion = "Soleado"),
        "Santa Fe" to ClimaState(ciudad = "Santa Fe", temperatura = 27.8, descripcion = "Húmedo"),
        "Corrientes" to ClimaState(ciudad = "Corrientes", temperatura = 30.0, descripcion = "Tropical"),
        "Mendoza" to ClimaState(ciudad = "Mendoza", temperatura = 26.2, descripcion = "Seco y soleado")
    )


    // INTENT
    fun handleIntent(intent: ClimaIntent) {
        when (intent) {
            is ClimaIntent.CargarClimaInicial -> {
                // Mantenemos una ciudad por defecto si se llama a la carga inicial
                cargarClima("Buenos Aires")
            }
            // Agregamos el nuevo branch para manejar la carga de una ciudad específica
            is ClimaIntent.CargarClima -> {
                cargarClima(intent.ciudad)
            }
        }
    }

    //LÓGICA
    //TODO API
    private fun cargarClima() {
        viewModelScope.launch {

            //modo cargando y limpiar errores
            _state.update { it.copy(isLoading = true, error = null) }

            //simulamos la llamada a la API
            try {
                // demora de red de 2 segundos
                delay(2000)

                // Buscamos los datos en nuestro mapa de climas falsos
                val climaParaCiudad = climasFalsos[ciudad]

                if (climaParaCiudad != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            ciudad = climaParaCiudad.ciudad,
                            temperatura = climaParaCiudad.temperatura,
                            descripcion = climaParaCiudad.descripcion
                        )
                    }
                } else {
                    // Si no encontramos la ciudad, mostramos un error
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No se encontraron datos para $ciudad."
                        )
                    }
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
