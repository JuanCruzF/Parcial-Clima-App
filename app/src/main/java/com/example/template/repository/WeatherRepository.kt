package com.example.template.repository

import com.example.template.model.City
import com.example.template.model.WeatherForecast

interface WeatherRepository {
    suspend fun searchCities(query: String): List<City>
    suspend fun getWeatherForCityName(cityName: String): WeatherForecast


    suspend fun searchCityByCoordinates(lat: Double, lon: Double): City?
}
