package com.example.medipets.model.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medipets.model.data.entities.PacienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao {

    @Insert
    suspend fun insertarPaciente(paciente: PacienteEntity)

    // Obtener a todos los pacientes(mascotas)
    @Query("SELECT * FROM paciente ORDER BY id DESC")
    fun obtenerPacientes(): Flow<List<PacienteEntity>>

    // Obtener a los pacientes por el ID
    @Query("SELECT * FROM paciente WHERE id = :idPaciente")
    suspend fun obtenerPacientePorId(idPaciente: Long): PacienteEntity?

    @Update
    suspend fun actualizarPaciente(paciente: PacienteEntity)

    @Delete
    suspend fun eliminarPaciente(paciente: PacienteEntity)

    // Eliminar por el ID
    @Query("DELETE FROM paciente WHERE id = :idPaciente")
    suspend fun eliminarPorId(idPaciente: Long)

}