package com.example.template.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore para toda la app.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

//repository para manejar las configuraciones del usuario.
// va a usar DataStore para guardar y leer datos.

class SettingsRepository(private val context: Context) {
    //se definan las claves para los datos.
    companion object {
        val CITY_KEY = stringPreferencesKey("selected_city")
    }

    // flujo de lectura -  GET
    // se lee la clave. si no existe (??), devuelve un string vacío "".
    val selectedCity: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[CITY_KEY] ?: ""
        }

    // funcion escritura - SAVE - asincrono
    suspend fun saveCity(cityName: String) {
        context.dataStore.edit { settings ->
            settings[CITY_KEY] = cityName
        }
    }
}