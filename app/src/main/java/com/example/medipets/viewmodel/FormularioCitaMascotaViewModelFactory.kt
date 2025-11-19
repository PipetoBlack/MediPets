// En: FormularioCitaMascotaViewModelFactory.kt
package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository

class FormularioCitaMascotaViewModelFactory(
    private val repository: FormularioCitaMascotaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormularioCitaMascotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormularioCitaMascotaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
