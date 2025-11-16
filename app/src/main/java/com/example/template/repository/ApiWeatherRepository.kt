package com.example.template.repository

import com.example.template.model.City
import com.example.template.model.DailyForecast
import com.example.template.model.TodayWeather
import com.example.template.model.WeatherForecast
import com.example.template.network.WeatherApiClient

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

        val geo = apiClient.searchCitiesByName(cityName).firstOrNull()
            ?: throw IllegalArgumentException("Ciudad no encontrada")

        val current = apiClient.getCurrentWeather(geo.lat, geo.lon)
        val forecast = apiClient.getForecast5Days(geo.lat, geo.lon)

        val city = City(
            id = 0L,
            name = current.name,
            country = geo.country,
            lat = geo.lat,
            lon = geo.lon
        )

        val today = TodayWeather(
            temperature = current.main.temp,
            humidity = current.main.humidity,
            description = current.weather.firstOrNull()?.description ?: "",
            icon = current.weather.firstOrNull()?.icon ?: ""
        )

        val nextDays = forecast.list
            .groupBy { it.dt_txt.substring(0, 10) } // yyyy-MM-dd
            .entries
            .take(5)
            .map { (date, items) ->
                val min = items.minOf { it.main.temp_min }
                val max = items.maxOf { it.main.temp_max }
                val desc = items.first().weather.firstOrNull()?.description ?: ""
                DailyForecast(
                    date = date,
                    tempMin = min,
                    tempMax = max,
                    description = desc
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
        // usamos la API /geo/1.0/reverse de OpenWeather
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
