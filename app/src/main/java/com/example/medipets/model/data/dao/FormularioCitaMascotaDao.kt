package com.example.medipets.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity

//En resumen, el DAO es una interfaz que define una lista de funciones permitidas para interactuar
//con una tabla de la base de datos. No contiene la lógica de cómo se hacen las cosas, solo dice
//qué se puede hacer.

@Dao
interface FormularioCitaMascotaDao {
    @Insert

    suspend fun insertar(cita: FormularioCitaMascotaEntity)

    @Query("SELECT * FROM cita_mascota WHERE usuario_id = :userId")
    suspend fun obtenerCitasPorUsuario(userId: Long): List<FormularioCitaMascotaEntity>

    // Si quieres listar con join para mostrar nombre mascota y veterinario
    @Query("""
        SELECT cm.id, cm.usuario_id, cm.mascota_id, cm.veterinario_id, cm.fecha, cm.motivo,
               m.nombre AS nombreMascota, v.nombre AS nombreVeterinario
        FROM cita_mascota cm
        INNER JOIN mascota m ON cm.mascota_id = m.id
        LEFT JOIN veterinario v ON cm.veterinario_id = v.id
        WHERE cm.usuario_id = :userId
    """)
    suspend fun obtenerCitasConDetalle(userId: Long): List<FormularioCitaMascotaEntity>
}