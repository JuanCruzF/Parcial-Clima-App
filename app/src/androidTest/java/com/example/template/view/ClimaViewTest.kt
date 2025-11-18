package com.example.template.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.template.domain.ClimaState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClimaViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraIndicadorDeCarga_cuandoIsLoadingEsTrue() {
        // given
        val state = ClimaState(isLoading = true)

        // when
        composeTestRule.setContent {
            ClimaScreen(state = state, onIntent = {})
        }

        // then
        // se verifica que el texto de error no exista
        composeTestRule.onNodeWithText("Error al cargar el clima").assertDoesNotExist()
    }

    @Test
    fun muestraMensajeDeError_cuandoHayError() {
        // given
        val mensajeError = "Error de conexión simulado"
        val state = ClimaState(isLoading = false, error = mensajeError)

        // when
        composeTestRule.setContent {
            ClimaScreen(state = state, onIntent = {})
        }

        // then
        composeTestRule.onNodeWithText(mensajeError).assertIsDisplayed()
    }

    @Test
    fun muestraDatosDelClima_cuandoHayDatos() {
        // given
        val ciudad = "Mendoza"
        val temperatura = "28.0°C"
        val descripcion = "Despejado"
        val state = ClimaState(
            isLoading = false,
            ciudad = ciudad,
            temperatura = 28.0,
            descripcion = descripcion
        )

        // when
        composeTestRule.setContent {
            ClimaScreen(state = state, onIntent = {})
        }

        // then
        composeTestRule.onNodeWithText(ciudad).assertIsDisplayed()
        composeTestRule.onNodeWithText(temperatura).assertIsDisplayed()
        composeTestRule.onNodeWithText(descripcion).assertIsDisplayed()
        composeTestRule.onNodeWithText("Acá iría el pronostico").assertIsDisplayed()
    }
}