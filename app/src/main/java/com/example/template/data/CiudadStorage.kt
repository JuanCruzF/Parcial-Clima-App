package com.example.template.data

import android.content.Context
import androidx.core.content.edit

object CiudadStorage {
    private const val PREFS = "ciudad_prefs"
    private const val KEY_CIUDAD = "ciudad_guardada"

    fun guardarCiudad(context: Context, ciudad: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_CIUDAD, ciudad) }
    }

    fun obtenerCiudad(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CIUDAD, null)
    }

    fun borrarCiudad(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit { remove(KEY_CIUDAD) }
    }
}
