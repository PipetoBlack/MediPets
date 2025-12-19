package com.example.medipets.model.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "mascota")
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: String,      // Perro, Gato, etc.
    val raza: String,
    val edadAnios: Int?,
    @ColumnInfo(name = "edad_meses")
    @SerializedName("edadMeses")
    val edadMeses: Int?,
    val fotoUri: String?,   // URI de la foto en el celular
    val clienteId: Long
)