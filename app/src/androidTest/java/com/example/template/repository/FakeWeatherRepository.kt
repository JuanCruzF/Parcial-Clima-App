package com.example.template.repository

import com.example.template.model.City
import com.example.template.model.WeatherForecast

class FakeWeatherRepository : WeatherRepository {

    private val fakeCities = listOf(
        City(1, "Buenos Aires", "AR", -34.6037, -58.3816),
        City(2, "Córdoba", "AR", -31.4201, -64.1888),
        City(3, "Rosario", "AR", -32.9442, -60.6505)
    )

    override suspend fun searchCities(query: String): List<City> = TODO("Provide the return value")

    override suspend fun searchCityByCoordinates(lat: Double, lon: Double): City? {
        // Elegimos la "más cercana" para las pruebas
        return fakeCities.minByOrNull { city ->
            val dLat = city.lat - lat
            val dLon = city.lon - lon
            dLat * dLat + dLon * dLon
        }
    }

    override suspend fun getWeatherForCityName(cityName: String): WeatherForecast = TODO("Provide the return value")
}
