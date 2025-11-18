package com.example.template.repository

import com.example.template.model.City
import com.example.template.model.DailyForecast
import com.example.template.model.TodayWeather
import com.example.template.model.WeatherForecast
import com.example.template.network.WeatherApiClient
import com.example.template.network.model.CurrentWeatherResponse
import com.example.template.network.model.ForecastResponse

class ApiWeatherRepository(
    private val apiClient: WeatherApiClient
) : WeatherRepository {

    override suspend fun searchCities(query: String): List<City> {
        if (query.isBlank()) return emptyList()

        val result = apiClient.searchCitiesByName(query)

        return result.mapIndexed { index, item ->
            City(
                id = index.toLong(),
                name = item.name,
                country = item.country,
                lat = item.lat,
                lon = item.lon
            )
        }
    }

    override suspend fun getWeatherForCityName(cityName: String): WeatherForecast {
        val current = apiClient.getCurrentWeatherByCityName(cityName)
        val forecast = apiClient.getForecastByCityName(cityName)

        // The display name is just the city name when searching by name
        return createWeatherForecast(current, forecast, cityName)
    }

    override suspend fun getWeatherForCoordinates(lat: Double, lon: Double): WeatherForecast {
        val current = apiClient.getCurrentWeatherByCoordinates(lat, lon)
        val forecast = apiClient.getForecastByCoordinates(lat, lon)

        // Safely get the official city name from the geo API.
        // This is an enhancement, so if it fails, we don't want the whole function to fail.
        val officialCityName = try {
            apiClient.searchCityByCoordinates(lat, lon).firstOrNull()?.name
        } catch (e: Exception) {
            null
        }

        val localityName = current.name

        // Combine names only if the official city name is available and different from the locality name.
        val displayName = if (officialCityName != null && officialCityName.isNotBlank() && officialCityName != localityName) {
            "$localityName, $officialCityName"
        } else {
            localityName
        }

        return createWeatherForecast(current, forecast, displayName)
    }

    private fun createWeatherForecast(
        current: CurrentWeatherResponse,
        forecast: ForecastResponse,
        displayName: String
    ): WeatherForecast {
        val city = City(
            id = 0L,
            name = displayName, // Use the rich display name
            country = current.sys?.country ?: "",
            lat = current.coord.lat,
            lon = current.coord.lon
        )

        val today = TodayWeather(
            temperature = current.main.temp,
            humidity = current.main.humidity,
            description = current.weather.firstOrNull()?.description ?: "",
            icon = current.weather.firstOrNull()?.icon ?: ""
        )


        val nextDays: List<DailyForecast> =
            forecast.list
                .groupBy { it.dt_txt.substring(0, 10) }
                .entries
                .sortedBy { it.key }
                .take(5)
                .map { (date, items) ->
                    val tempsMin = items.map { it.main.temp_min ?: it.main.temp }
                    val tempsMax = items.map { it.main.temp_max ?: it.main.temp }
                    val description =
                        items.firstOrNull()?.weather?.firstOrNull()?.description ?: ""

                    DailyForecast(
                        date = date,
                        tempMin = tempsMin.minOrNull() ?: items.first().main.temp,
                        tempMax = tempsMax.maxOrNull() ?: items.first().main.temp,
                        description = description
                    )
                }

        return WeatherForecast(
            city = city,
            today = today,
            nextDays = nextDays
        )
    }

    override suspend fun searchCityByCoordinates(
        lat: Double,
        lon: Double
    ): City? {
        val result = apiClient.searchCityByCoordinates(lat, lon)
        val item = result.firstOrNull() ?: return null

        return City(
            id = 0L,
            name = item.name,
            country = item.country,
            lat = item.lat,
            lon = item.lon
        )
    }
}
