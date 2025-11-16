package com.example.template.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.repository.SettingsRepository
import kotlinx.coroutines.launch


// conecta la UI con el SettingsRepository.
//AndroidViewModel antes que ViewMode porque necesitamos el application cotext para el repo

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    //se obtiene instancia del repo
    private val repository = SettingsRepository(application.applicationContext)

    // flow de la ciudad seleccionada
    val selectedCity = repository.selectedCity

    //función para guardar
    //cuando el usuario elija una ciudad.
    fun saveCity(cityName: String) {
        viewModelScope.launch {
            repository.saveCity(cityName)
        }
    }
}