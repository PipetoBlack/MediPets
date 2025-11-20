package com.example.medipets.model.data.repository

import com.example.medipets.model.data.dao.VeterinarioDao
import com.example.medipets.model.data.entities.VeterinarioEntity
import kotlinx.coroutines.flow.Flow

class VeterinarioRepository(
    private val dao: VeterinarioDao
) {
    // Insertar un veterinario nuevo
    suspend fun guardarVeterinario(
        nombre: String,
        especialidad: String,
        correo: String,
        telefono: String
    ) {
        val entity = VeterinarioEntity(
            nombre = nombre,
            especialidad = especialidad,
            correo = correo,
            telefono = telefono
        )
        dao.insertarVeterinario(entity)
    }

    // Obtener todos
    fun obtenerVeterinarios(): Flow<List<VeterinarioEntity>> =
        dao.obtenerVeterinarios()

    // Obtener por ID
    suspend fun obtenerVeterinarioPorId(idVeterinario: Long): VeterinarioEntity? =
        dao.obtenerVeterinarioPorId(idVeterinario)

    // Actualizar
    suspend fun actualizarVeterinario(vet: VeterinarioEntity) =
        dao.actualizarVeterinario(vet)

    // Eliminar por entidad
    suspend fun eliminarVeterinario(vet: VeterinarioEntity) =
        dao.eliminarVeterinario(vet)

    // Eliminar por ID
    suspend fun eliminarVeterinarioPorId(idVeterinario: Long) =
        dao.eliminarVeterinarioPorId(idVeterinario)
}