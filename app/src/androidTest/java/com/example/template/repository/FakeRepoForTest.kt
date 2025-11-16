package com.example.template.repository

import com.example.template.model.City
import com.example.template.model.WeatherForecast

class FakeRepoForTest : WeatherRepository {
    override suspend fun searchCities(query: String): List<City> = emptyList()

    override suspend fun searchCityByCoordinates(lat: Double, lon: Double): City? =
        City(1, "Ciudad desde coordenadas", "AR", lat, lon)

    override suspend fun getWeatherForCityName(cityName: String): WeatherForecast {
        return TODO("Provide the return value")
    }
}
