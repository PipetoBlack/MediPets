    // En: FormularioCitaMascotaViewModelFactory.kt
    package com.example.medipets.viewmodel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository
    import com.example.medipets.model.data.repository.MascotaRepository
    import com.example.medipets.model.data.repository.VeterinarioRepository

    class FormularioCitaMascotaViewModelFactory(
        private val citaRepo: FormularioCitaMascotaRepository,
        private val mascotaRepo: MascotaRepository,
        private val veterinarioRepo: VeterinarioRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FormularioCitaMascotaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FormularioCitaMascotaViewModel(
                    citaRepo,
                    mascotaRepo,
                    veterinarioRepo
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }