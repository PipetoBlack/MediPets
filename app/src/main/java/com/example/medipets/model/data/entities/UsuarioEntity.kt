package com.example.medipets.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "usuarios")
data class UsuarioEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val email: String,
    val password: String
)