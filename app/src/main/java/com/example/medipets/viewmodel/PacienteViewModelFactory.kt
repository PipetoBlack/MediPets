package com.example.medipets.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.data.repository.PacienteRepository

class PacienteViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PacienteViewModel::class.java)) {

            val db = AppDatabase.getDatabase(context)
            val dao = db.pacienteDao()
            val repo = PacienteRepository(dao)

            @Suppress("UNCHECKED_CAST")
            return PacienteViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}