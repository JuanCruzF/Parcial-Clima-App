package com.example.template.router

object AppRouter {
    fun getStartDestination(ciudadInicial: String?): String {
        return if (ciudadInicial.isNullOrBlank()) {
            "selector"
        } else {
            "clima/$ciudadInicial"
        }
    }
}
