package com.example.medipets.model.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medipets.model.data.entities.VeterinarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VeterinarioDao {

    // Inserta un veterinario en la entidad
    @Insert
    suspend fun insertarVeterinario(vet: VeterinarioEntity)

    // Obtener todos los veterinarios
    @Query("SELECT * FROM veterinario ORDER BY id DESC")
    fun obtenerVeterinarios(): Flow<List<VeterinarioEntity>>

    // Obtener a un veterinario por su ID
    @Query("SELECT * FROM veterinario WHERE id = :idVeterinario")
    suspend fun obtenerVeterinarioPorId(idVeterinario: Long): VeterinarioEntity?

    @Update
    suspend fun actualizarVeterinario(vet: VeterinarioEntity)

    @Delete
    suspend fun eliminarVeterinario(vet: VeterinarioEntity)

    // Eliminar a un veterinario por su ID
    @Query("DELETE FROM veterinario WHERE id = :idVeterinario")
    suspend fun eliminarVeterinarioPorId(idVeterinario: Long)
}