package com.example.medipets.model.data.repository

import com.example.medipets.model.data.dao.PacienteDao
import com.example.medipets.model.data.entities.PacienteEntity
import kotlinx.coroutines.flow.Flow

class PacienteRepository(private val pacienteDao: PacienteDao) {

    // Insertar un paciente(mascota)
    suspend fun guardarPaciente(
        nombre: String,
        especie: String,
        raza: String?,
        edad: Int
    ) {
        val entity = PacienteEntity(
            nombre = nombre,
            especie = especie,
            raza = raza,
            edad = edad
        )
        pacienteDao.insertarPaciente(entity)
    }

    // Obtener todos los pacientes(mascotas)
    fun obtenerPacientes(): Flow<List<PacienteEntity>> = pacienteDao.obtenerPacientes()

    // Obtener paciente por el ID
    suspend fun obtenerPacientePorId(idPaciente: Long): PacienteEntity? = pacienteDao.obtenerPacientePorId(idPaciente)

    // Actualizar paciente
    suspend fun actualizarPaciente(paciente: PacienteEntity) = pacienteDao.actualizarPaciente(paciente)

    // Eliminar (CRUD)
    suspend fun eliminarPaciente(paciente: PacienteEntity) = pacienteDao.eliminarPaciente(paciente)

    // Eliminar paciente por ID
    suspend fun eliminarPacientePorId(idPaciente: Long) = pacienteDao.eliminarPorId(idPaciente)
}