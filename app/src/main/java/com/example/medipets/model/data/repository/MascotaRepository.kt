package com.example.medipets.model.data.repository

import com.example.medipets.model.data.dao.MascotaDao
import com.example.medipets.model.data.entities.MascotaEntity
import kotlinx.coroutines.flow.Flow

class MascotaRepository(
    private val dao: MascotaDao
) {
    suspend fun guardarMascota(
        nombre: String,
        tipo: String,
        raza: String,
        edadAnios: Int?,
        edadMeses: Int?,
        fotoUri: String?
    ) {
        val entity = MascotaEntity(
            nombre = nombre,
            tipo = tipo,
            raza = raza,
            edadAnios = edadAnios,
            edadMeses = edadMeses,
            fotoUri = fotoUri
        )
        dao.insertarMascota(entity)
    }

    fun obtenerMascotas(): Flow<List<MascotaEntity>> = dao.obtenerMascotas()

    suspend fun obtenerMascotaPorId(idMascota: Long): MascotaEntity? =
        dao.obtenerMascotaPorId(idMascota)

    suspend fun actualizarMascota(mascota: MascotaEntity) =
        dao.actualizarMascota(mascota)

    suspend fun eliminarMascota(mascota: MascotaEntity) =
        dao.eliminarMascota(mascota)

    suspend fun eliminarMascotaPorId(idMascota: Long) =
        dao.eliminarMascotaPorId(idMascota)
}