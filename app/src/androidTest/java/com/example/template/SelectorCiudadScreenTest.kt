package com.example.template

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.template.view.SelectorCiudadScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SelectorCiudadScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun alPulsarCiudad_seLlamaACallbackConCiudadCorrecta() {
        val onCiudadSeleccionada: (String) -> Unit = mock()

        composeTestRule.setContent {
            SelectorCiudadScreen()
        }

        composeTestRule.onNodeWithText("Córdoba").performClick()

        verify(onCiudadSeleccionada).invoke("Córdoba")
    }

    private fun mock(): (String) -> Unit {}
}
