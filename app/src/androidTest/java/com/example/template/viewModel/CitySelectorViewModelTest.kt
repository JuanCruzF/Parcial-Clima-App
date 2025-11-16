package com.example.template

import com.example.template.domain.CitySelectorIntent
import com.example.template.model.City
import com.example.template.model.DailyForecast
import com.example.template.model.TodayWeather
import com.example.template.model.WeatherForecast
import com.example.template.repository.WeatherRepository
import com.example.template.viewmodel.CitySelectorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

// Fake repo usado en los tests de búsqueda por texto
class FakeWeatherRepositoryForSelector : WeatherRepository {

    override suspend fun searchCities(query: String): List<City> {
        return listOf(
            City(1, "Buenos Aires", "AR", -34.6, -58.4),
            City(2, "Córdoba", "AR", -31.4, -64.2)
        ).filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun getWeatherForCityName(cityName: String): WeatherForecast {
        // Dummy para cumplir la interfaz, no se usa en estos tests
        val city = City(1, cityName, "AR", 0.0, 0.0)
        val today = TodayWeather(25.0, 50, "Soleado", "10d")
        val next = listOf(
            DailyForecast("mañana", 18.0, 26.0, "Parcialmente nublado")
        )
        return WeatherForecast(city, today, next)
    }

    // ➕ NUEVO: hay que implementarlo porque ahora la interfaz lo exige
    override suspend fun searchCityByCoordinates(lat: Double, lon: Double): City? {
        // No lo usamos en estos tests, devolvemos una ciudad dummy
        return City(99, "Ciudad Geo", "AR", lat, lon)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CitySelectorViewModelTest {

    @Test
    fun queryChanged_actualizaResultadosDesdeRepositorio() = runTest {
        val vm = CitySelectorViewModel(repository = FakeWeatherRepositoryForSelector())

        vm.handleIntent(CitySelectorIntent.QueryChanged("buenos"))
        advanceUntilIdle()   // dejamos que termine la corrutina con delay

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.results.size)
        assertEquals("Buenos Aires", state.results[0].name)
    }

    @Test
    fun queryVacio_limpiaResultados() = runTest {
        val vm = CitySelectorViewModel(repository = FakeWeatherRepositoryForSelector())

        vm.handleIntent(CitySelectorIntent.QueryChanged("buenos"))
        advanceUntilIdle()

        vm.handleIntent(CitySelectorIntent.QueryChanged(""))
        advanceUntilIdle()

        val state = vm.state.value
        assertEquals(0, state.results.size)
    }


}
