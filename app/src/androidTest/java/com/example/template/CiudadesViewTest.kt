package com.example.template

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.template.model.Clima
import com.example.template.view.CiudadesView
import com.example.template.viewmodel.TemaViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CiudadesViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun alRecibirClima_muestraInformacionCorrecta() {
        val climaDePrueba = Clima("Buenos Aires", 25.0, "Soleado")
        val temaViewModel = TemaViewModel()

        composeTestRule.setContent {
        }

        // TODO: This test needs to be improved to use the fake repository
        // For now, we just check if the city name is displayed.
        composeTestRule.onNodeWithText("Clima en Buenos Aires").assertExists()
    }
}
