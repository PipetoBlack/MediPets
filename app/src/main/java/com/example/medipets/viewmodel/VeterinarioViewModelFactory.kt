package com.example.medipets.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.data.repository.VeterinarioRepository

class VeterinarioViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T): T {
        if (modelClass.isAssignableFrom(VeterinarioViewModel::class.java)) {

            val db = AppDatabase.getDatabase(context)
            val dao = db.veterinarioDao()
            val repo = VeterinarioRepository(dao)

            @Suppress("UNCHECKED_CAST")
            return VeterinarioViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}