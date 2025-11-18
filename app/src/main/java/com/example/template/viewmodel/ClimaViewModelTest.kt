package com.example.template.viewmodel

import com.example.template.domain.ClimaIntent
import com.example.template.model.City
import com.example.template.model.Weather
import com.example.template.model.WeatherForecast
import com.example.template.repository.WeatherRepository
import com.example.template.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClimaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // mock del repositorio
    private val repository: WeatherRepository = mockk()
    private lateinit var viewModel: ClimaViewModel

    // datos de prueba
    private val ciudadEjemplo = "Buenos Aires"
    private val forecastData = WeatherForecast(
        city = City(ciudadEjemplo, 0.0, 0.0, "AR"),
        today = Weather("2025-11-18", 25.0, 15.0, 30.0, "Soleado", 50, ""),
        nextDays = emptyList()
    )

    private val refreshedForecastData = WeatherForecast(
        city = City(ciudadEjemplo, 0.0, 0.0, "AR"),
        today = Weather("2025-11-18", 26.0, 16.0, 31.0, "Caluroso", 55, ""),
        nextDays = emptyList()
    )


    @Before
    fun setup() {
        // se inicializa el  ViewModel con el Mock del repositorio
        viewModel = ClimaViewModel(repository)
    }

    @Test
    fun `handleIntent CargarClima actualiza estado con datos cuando es exitoso`() = runTest {
        // given: mock para que devuelva datos fijos
        coEvery { repository.getWeatherForCityName(ciudadEjemplo) } returns forecastData

        // when
        viewModel.handleIntent(ClimaIntent.CargarClima(ciudadEjemplo))

        // then: verifica el estado
        val state = viewModel.state.value
        assertEquals(ciudadEjemplo, state.ciudad)
        assertEquals(25.0, state.temperatura, 0.1)
        assertEquals("Soleado", state.descripcion)
        assertNull(state.error)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `handleIntent CargarClima actualiza estado con error cuando falla repo`() = runTest {
        // given mock para que de una excepción
        val ciudadError = "Cualquiera"
        coEvery { repository.getWeatherForCityName(ciudadError) } throws Exception("Error de red simulado")

        // WHEN
        viewModel.handleIntent(ClimaIntent.CargarClima(ciudadError))

        // then: vemos que el error se haya capturado
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("Error al cargar el clima"))
    }

    @Test
    fun `handleIntent Compartir genera texto correctamente despues de cargar`() = runTest {
        // given: mockeamos la carga de datos
        val ciudad = "Madrid"
        val forecastMadrid = WeatherForecast(
            city = City(ciudad, 0.0, 0.0, "ES"),
            today = Weather("2025-11-18", 20.0, 10.0, 25.0, "Nublado", 60, ""),
            nextDays = emptyList()
        )
        coEvery { repository.getWeatherForCityName(ciudad) } returns forecastMadrid

        // carga el estado inicial, así tenemos datos para compartir
        viewModel.handleIntent(ClimaIntent.CargarClima(ciudad))

        // when: intentamos Compartir
        viewModel.handleIntent(ClimaIntent.Compartir)

        // then: verificamos el texto de compartir
        val state = viewModel.state.value
        assertNotNull(state.shareText)
        assertTrue(state.shareText!!.contains("Pronóstico del clima para Madrid:"))
        assertTrue(state.shareText!!.contains("Hoy: Nublado, 20.0°C"))
    }

    @Test
    fun `handleIntent Refrescar vuelve a cargar clima actual y actualiza estado`() = runTest {
        // given: mockeamos la primera carga
        coEvery { repository.getWeatherForCityName(ciudadEjemplo) } returns forecastData

        //carga datos iniciales
        viewModel.handleIntent(ClimaIntent.CargarClima(ciudadEjemplo))
        assertEquals(25.0, viewModel.state.value.temperatura, 0.1)

        // given: mockeamos la segunda carga (refresh) con datos diferentes
        coEvery { repository.getWeatherForCityName(ciudadEjemplo) } returns refreshedForecastData

        //when: llamamos a refrescar
        viewModel.handleIntent(ClimaIntent.Refrescar)

        //then: verificamos que los datos se hayan actualizado
        val state = viewModel.state.value
        assertEquals(ciudadEjemplo, state.ciudad)
        assertEquals(26.0, state.temperatura, 0.1) // El valor actualizado
        assertEquals("Caluroso", state.descripcion)
        assertEquals(false, state.isLoading)
    }
}