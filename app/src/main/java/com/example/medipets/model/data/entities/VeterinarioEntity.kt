package com.example.medipets.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "veterinario")
data class VeterinarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val especialidad: String,
    val correo: String,
    val telefono: String
)