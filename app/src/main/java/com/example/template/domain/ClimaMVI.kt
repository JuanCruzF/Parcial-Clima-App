package com.example.template.domain

import com.example.template.model.DailyForecast

data class ClimaState(
    val ciudad: String = "",
    val temperatura: Double = 0.0,
    val descripcion: String = "",
    val humedad: Int = 0,
    val pronostico: List<DailyForecast> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val shareText: String? = null      // para la función compartir
)

sealed class ClimaIntent {
    data class CargarClima(val ciudad: String) : ClimaIntent()
    object Refrescar : ClimaIntent()
    object Compartir : ClimaIntent()
}
