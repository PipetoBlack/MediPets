package com.example.medipets.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formulario_servicio")
data class FormularioServicioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombreCliente: String,
    val correoCliente: String,
    val region: String
)