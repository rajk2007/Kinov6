
package com.rajk2007.kino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadContent() {
        viewModelScope.launch {
            try {
                // Simulate API call
                kotlinx.coroutines.delay(1000) // Simulate network delay
                val success = (0..1).random() == 1 // Simulate success/failure
                if (!success) {
                    throw Exception("Simulated network error")
                }
                _errorMessage.value = null // Clear error on success
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load content. Check internet connection."
            }
        }
    }

    fun errorMessageShown() {
        _errorMessage.value = null
    }
}
