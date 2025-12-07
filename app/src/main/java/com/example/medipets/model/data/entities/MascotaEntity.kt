package com.example.medipets.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascota")
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,      // Perro, Gato, etc.
    val raza: String,
    val edad: Int?,
    val fotoUri: String?   // URI de la foto en el celular
)