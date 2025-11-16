package com.example.template

import com.example.template.domain.ClimaIntent
import com.example.template.model.*
import com.example.template.repository.WeatherRepository
import com.example.template.viewmodel.ClimaViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class FakeRepoForTest : WeatherRepository {
    override suspend fun searchCities(query: String): List<City> = emptyList()

    override suspend fun getWeatherForCityName(cityName: String): WeatherForecast {
        val city = City(1, cityName, "AR", 0.0, 0.0)
        val today = TodayWeather(25.0, 50, "Soleado", "10d")
        val next = listOf(
            DailyForecast("mañana", 18.0, 26.0, "Parcialmente nublado")
        )
        return WeatherForecast(city, today, next)
    }

    override suspend fun searchCityByCoordinates(
        lat: Double,
        lon: Double
    ): City? {
        TODO("Not yet implemented")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ClimaViewModelTest {

    @Test
    fun cargarClima_actualizaEstadoConDatosDelRepo() = runTest {
        val vm = ClimaViewModel(repository = FakeRepoForTest())

        vm.handleIntent(ClimaIntent.CargarClima("Buenos Aires"))
        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals("Buenos Aires", state.ciudad)
        assertEquals(25.0, state.temperatura, 0.001)
        assertEquals("Soleado", state.descripcion)
        assertEquals(1, state.pronostico.size)
    }

    @Test
    fun compartir_generarTextoDeShare() = runTest {
        val vm = ClimaViewModel(repository = FakeRepoForTest())

        vm.handleIntent(ClimaIntent.CargarClima("Buenos Aires"))
        advanceUntilIdle()

        vm.handleIntent(ClimaIntent.Compartir)
        val state = vm.state.value

        assertNotNull(state.shareText)
        requireNotNull(state.shareText)
        assert(state.shareText!!.contains("Buenos Aires"))
        assert(state.shareText!!.contains("Soleado"))
    }
}
