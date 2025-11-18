package com.example.template.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class WeatherApiClient(
    private val apiKey: String
) {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    private val baseWeather = "https://api.openweathermap.org/data/2.5"
    private val baseGeo = "https://api.openweathermap.org/geo/1.0"

    // ---------- DTOs ----------

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



    suspend fun searchCitiesByName(query: String): List<GeoCityResponse> =
        client.get("$baseGeo/direct") {
            parameter("q", query)
            parameter("limit", 5)
            parameter("appid", apiKey)
        }.body()

    suspend fun getCurrentWeatherByCityName(cityName: String): CurrentWeatherResponse =
        client.get("$baseWeather/weather") {
            parameter("q", cityName)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("appid", apiKey)
        }.body()

    suspend fun getForecastByCityName(cityName: String): ForecastResponse =
        client.get("$baseWeather/forecast") {
            parameter("q", cityName)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("appid", apiKey)
        }.body()

    suspend fun searchCityByCoordinates(
        lat: Double,
        lon: Double
    ): List<GeoCityResponse> =
        client.get("$baseGeo/reverse") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("limit", 1)
            parameter("appid", apiKey)
        }.body()
}
