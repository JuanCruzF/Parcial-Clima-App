package com.example.template.domain

import com.example.template.model.City

data class CitySelectorState(
    val query: String = "",
    val results: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CitySelectorIntent {
    object LoadInitial : CitySelectorIntent()
    data class QueryChanged(val query: String) : CitySelectorIntent()


    data class BuscarPorUbicacion(val lat: Double, val lon: Double) : CitySelectorIntent()
}
