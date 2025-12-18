package com.example.medipets.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.data.repository.UsuarioRepository


class RegistroViewModelFactory (
    private val repo: UsuarioRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Change RegisterViewModel to RegistroViewModel here
        if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // And also here
            return RegistroViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}