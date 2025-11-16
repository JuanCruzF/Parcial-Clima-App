package com.example.template.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.template.domain.CitySelectorIntent
import com.example.template.domain.CitySelectorState
import com.example.template.network.WeatherApiClient
import com.example.template.repository.ApiWeatherRepository
import com.example.template.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CitySelectorViewModel(
    private val repository: WeatherRepository = ApiWeatherRepository(
        WeatherApiClient("3ca8ed3a66da6ecf389692f9521fab6c")
    )
) : ViewModel() {

    private val _state = MutableStateFlow(CitySelectorState())
    val state = _state.asStateFlow()

    private var currentSearchJob: Job? = null

    fun handleIntent(intent: CitySelectorIntent) {
        when (intent) {
            CitySelectorIntent.LoadInitial -> {
                // Podés mostrar ciudades populares, por ahora dejamos vacío
                _state.update { it.copy(results = emptyList(), error = null) }
            }
            is CitySelectorIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.query) }
                buscarCiudades(intent.query)
            }
        }
    }

    private fun buscarCiudades(query: String) {
        currentSearchJob?.cancel()

        if (query.isBlank()) {
            _state.update { it.copy(results = emptyList(), isLoading = false, error = null) }
            return
        }

        currentSearchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // pequeño debounce para no pegarle a la API por cada tecla
                delay(300)
                val cities = repository.searchCities(query)

                _state.update {
                    it.copy(
                        results = cities,
                        isLoading = false,
                        error = if (cities.isEmpty()) "No se encontraron ciudades" else null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al buscar ciudades"
                    )
                }
            }
        }
    }
}
