package com.example.template.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)

@Serializable
data class Sys(
    val country: String? = null
)

@Serializable
data class Main(
    val temp: Double,
    val humidity: Int,
    val temp_min: Double? = null,
    val temp_max: Double? = null
)

@Serializable
data class WeatherDesc(
    val description: String,
    val icon: String
)

@Serializable
data class CurrentWeatherResponse(
    val name: String,
    val coord: Coord,
    val sys: Sys? = null,
    val main: Main,
    val weather: List<WeatherDesc>
)

@Serializable
data class ForecastItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<WeatherDesc>
)

@Serializable
data class ForecastResponse(
    val list: List<ForecastItem>
)

@Serializable
data class GeoCityResponse(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)
