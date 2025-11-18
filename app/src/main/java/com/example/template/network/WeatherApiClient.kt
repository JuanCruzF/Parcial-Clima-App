package com.example.template.network

import com.example.template.network.model.CurrentWeatherResponse
import com.example.template.network.model.ForecastResponse
import com.example.template.network.model.GeoCityResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
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

    suspend fun getCurrentWeatherByCoordinates(lat: Double, lon: Double): CurrentWeatherResponse =
        client.get("$baseWeather/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("appid", apiKey)
        }.body()

    suspend fun getForecastByCoordinates(lat: Double, lon: Double): ForecastResponse =
        client.get("$baseWeather/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
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
