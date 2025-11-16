package com.example.template.domain


//patron mvi (state e intent para clima)


// --- STATE --

//todo el estado de pantalla de clima

data class ClimaState(
    val ciudad: String = "Cargando...",
    val temperatura: Double = 0.0,
    val descripcion: String = "",
    val isLoading: Boolean = true,
    //manejar error

    val error: String? = null
)


// --- INTENT ---
//acciones de usuario o sistema - interfaz sellada para limitar acciones
sealed interface ClimaIntent {
    // Cargar el clima para una ciudad específica
    data class CargarClima(val ciudad: String) : ClimaIntent

    // carga por primera vez
    object CargarClimaInicial : ClimaIntent

    // al actualizar (más adelante) TODO
    // object RefrescarClima : ClimaIntent
}