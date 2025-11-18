package com.example.template.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.BuildConfig
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
        WeatherApiClient(BuildConfig.OPEN_WEATHER_API_KEY)
    )
) : ViewModel() {

    private val _state = MutableStateFlow(CitySelectorState())
    val state = _state.asStateFlow()

    private var currentSearchJob: Job? = null

    fun handleIntent(intent: CitySelectorIntent) {
        when (intent) {
            CitySelectorIntent.LoadInitial -> { /* opcional */ }

            is CitySelectorIntent.QueryChanged ->
                searchByQuery(intent.query)

            is CitySelectorIntent.BuscarPorUbicacion ->
                searchByLocation(intent.lat, intent.lon)
        }
    }

    private fun searchByQuery(query: String) {
        _state.update { it.copy(query = query) }

        if (query.isBlank()) {
            currentSearchJob?.cancel()
            _state.update {
                it.copy(
                    results = emptyList(),
                    isLoading = false,
                    error = null
                )
            }
            return
        }

        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                delay(500)
                val cities = repository.searchCities(query)

                _state.update {
                    if (cities.isEmpty()) {
                        it.copy(
                            results = emptyList(),
                            isLoading = false,
                            error = "No se encontraron ciudades"
                        )
                    } else {
                        it.copy(
                            results = cities,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        results = emptyList(),
                        isLoading = false,
                        error = "Error al buscar ciudades: ${e.message ?: e::class.simpleName}"
                    )
                }
            }
        }
    }

    private fun searchByLocation(lat: Double, lon: Double) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val weatherForecast = repository.getWeatherForCoordinates(lat, lon)

                _state.update {
                    it.copy(
                        query = weatherForecast.city.name,
                        results = listOf(weatherForecast.city),
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al buscar por ubicación: ${e.message ?: e::class.simpleName}"
                    )
                }
            }
        }
    }
}
