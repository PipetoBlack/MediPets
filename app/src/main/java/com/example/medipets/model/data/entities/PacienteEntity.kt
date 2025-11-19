package com.example.medipets.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paciente")
data class PacienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String, // Obligatorio
    val especie: String, // Obligatorio
    val raza: String?, // No es obligatoria
    val edad: Int // Obligatorio
)