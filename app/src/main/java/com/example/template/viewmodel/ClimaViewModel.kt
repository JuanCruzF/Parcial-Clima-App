package com.example.template.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.BuildConfig
import com.example.template.domain.ClimaIntent
import com.example.template.domain.ClimaState
import com.example.template.network.WeatherApiClient
import com.example.template.repository.ApiWeatherRepository
import com.example.template.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClimaViewModel(
    private val repository: WeatherRepository = ApiWeatherRepository(
        WeatherApiClient(BuildConfig.OPEN_WEATHER_API_KEY)
    )
) : ViewModel() {

    private val _state = MutableStateFlow(ClimaState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: ClimaIntent) {
        when (intent) {
            is ClimaIntent.CargarClima -> cargarClima(intent.ciudad)
            ClimaIntent.Refrescar -> refrescar()
            ClimaIntent.Compartir -> compartir()
        }
    }

    private fun cargarClima(ciudad: String) {
        if (ciudad.isBlank()) return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    ciudad = ciudad,
                    isLoading = true,
                    error = null,
                    shareText = null
                )
            }

            try {
                val forecast = repository.getWeatherForCityName(ciudad)

                _state.update {
                    it.copy(
                        ciudad = forecast.city.name,
                        temperatura = forecast.today.temperature,
                        descripcion = forecast.today.description,
                        humedad = forecast.today.humidity,
                        pronostico = forecast.nextDays,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el clima. Verificá conexión o nombre de ciudad."
                    )
                }
            }
        }
    }

    private fun refrescar() {
        val ciudadActual = _state.value.ciudad
        if (ciudadActual.isNotBlank()) {
            cargarClima(ciudadActual)
        }
    }

    private fun compartir() {
        val s = _state.value
        if (s.ciudad.isBlank()) return

        val builder = StringBuilder()
        builder.appendLine("Pronóstico del clima para ${s.ciudad}:")
        builder.appendLine("Hoy: ${s.descripcion}, ${s.temperatura}°C, humedad ${s.humedad}%")
        if (s.pronostico.isNotEmpty()) {
            builder.appendLine("Próximos días:")
            s.pronostico.forEach {
                builder.appendLine("- ${it.date}: ${it.tempMin}°C / ${it.tempMax}°C, ${it.description}")
            }
        }

        _state.update { it.copy(shareText = builder.toString()) }
    }
}
