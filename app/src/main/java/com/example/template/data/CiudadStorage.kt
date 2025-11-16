package com.example.template.data

import android.content.Context

object CiudadStorage {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_CIUDAD = "ciudad_guardada"

    fun guardarCiudad(context: Context, ciudad: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_CIUDAD, ciudad).apply()
    }

    fun obtenerCiudad(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CIUDAD, null)
    }

    fun borrarCiudad(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().remove(KEY_CIUDAD).apply()
    }
}