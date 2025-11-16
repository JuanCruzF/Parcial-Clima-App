package com.example.template.repository

import com.example.template.model.Clima

class ClimaRepository {

    private val climasDePrueba = mapOf(
        "Buenos Aires" to Clima("Buenos Aires", 25.0, "Soleado"),
        "Córdoba"      to Clima("Córdoba", 28.0, "Nublado"),
        "Rosario"      to Clima("Rosario", 26.0, "Lluvioso")
    )

    fun obtenerClima(ciudad: String): Clima {
        return climasDePrueba[ciudad]
            ?: Clima(ciudad, 20.0, "No disponible")
    }
}
