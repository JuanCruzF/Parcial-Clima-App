package com.example.template.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemaViewModel : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)

    val isDarkMode = _isDarkMode.asStateFlow()

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }
}