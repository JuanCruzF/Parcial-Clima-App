package com.example.template.model


data class TodayWeather(
    val temperature: Double,
    val humidity: Int,
    val description: String,
    val icon: String
)

data class DailyForecast(
    val date: String,      // "2025-11-15"
    val tempMin: Double,
    val tempMax: Double,
    val description: String
)

data class WeatherForecast(
    val city: City,
    val today: TodayWeather,
    val nextDays: List<DailyForecast>
)
