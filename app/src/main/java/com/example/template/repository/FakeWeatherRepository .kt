package com.example.template.repository

import com.example.template.model.*

class FakeWeatherRepository : WeatherRepository {

    private val fakeCities = listOf(
        City(1, "Buenos Aires", "AR", -34.6037, -58.3816),
        City(2, "Córdoba", "AR", -31.4201, -64.1888),
        City(3, "Rosario", "AR", -32.9442, -60.6505)
    )

    override suspend fun searchCities(query: String): List<City> {
        if (query.isBlank()) return fakeCities
        return fakeCities.filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun getWeatherForCityName(cityName: String): WeatherForecast {
        val city = fakeCities.find { it.name.equals(cityName, ignoreCase = true) }
            ?: fakeCities.first()

        val today = TodayWeather(
            temperature = when (city.name) {
                "Buenos Aires" -> 24.0
                "Córdoba" -> 27.0
                "Rosario" -> 22.0
                else -> 20.0
            },
            humidity = 60,
            description = "Parcialmente nublado",
            icon = "10d"
        )

        val nextDays = listOf(
            DailyForecast("Mañana", 18.0, 25.0, "Soleado"),
            DailyForecast("Pasado", 17.0, 24.0, "Nublado"),
            DailyForecast("En 3 días", 19.0, 27.0, "Lluvias")
        )

        return WeatherForecast(
            city = city,
            today = today,
            nextDays = nextDays
        )
    }
}
