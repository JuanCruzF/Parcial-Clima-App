package com.example.template.network

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
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

    private val baseGeo = "https://api.openweathermap.org/geo/1.0"
    private val baseData = "https://api.openweathermap.org/data/2.5"


    @Serializable
    data class GeoCityResponse(
        val name: String,
        val lat: Double,
        val lon: Double,
        val country: String
    )


    @Serializable
    data class MainInfo(
        val temp: Double,
        val humidity: Int
    )

    @Serializable
    data class WeatherInfo(
        val description: String,
        val icon: String
    )

    @Serializable
    data class CurrentWeatherResponse(
        val name: String,
        val main: MainInfo,
        val weather: List<WeatherInfo>
    )


    @Serializable
    data class ForecastItemMain(
        val temp_min: Double,
        val temp_max: Double
    )

    @Serializable
    data class ForecastItem(
        val dt_txt: String,
        val main: ForecastItemMain,
        val weather: List<WeatherInfo>
    )

    @Serializable
    data class ForecastResponse(
        val list: List<ForecastItem>
    )


    suspend fun searchCitiesByName(name: String): List<GeoCityResponse> {
        return client.get("$baseGeo/direct") {
            parameter("q", name)
            parameter("limit", 5)
            parameter("appid", apiKey)
        }.body()
    }


    suspend fun searchCityByCoordinates(lat: Double, lon: Double): List<GeoCityResponse> {
        return client.get("$baseGeo/reverse") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("limit", 1)
            parameter("appid", apiKey)
        }.body()
    }


    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return client.get("$baseData/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("appid", apiKey)
        }.body()
    }


    suspend fun getForecast5Days(lat: Double, lon: Double): ForecastResponse {
        return client.get("$baseData/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("appid", apiKey)
        }.body()
    }
}
