package com.example.medipets.model.data.repository

import android.util.Log
import com.example.medipets.model.data.dao.MascotaDao
import com.example.medipets.model.data.entities.MascotaEntity
import com.example.medipets.model.data.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

class MascotaRepository(
    private val dao: MascotaDao
) {
    // Instancia del servicio de Retrofit que configuramos antes
    private val apiService = RetrofitClient.instance

    /**
     * NUEVA: Sincroniza los datos del Backend con la base de datos local (Room).
     * Llama a esta función al iniciar la app o al hacer "pull to refresh".
     */
    suspend fun sincronizarConBackend() {
        try {
            val mascotasRemotas = apiService.getAllMascotas()
            // Actualizamos la base de datos local con lo que viene del servidor
            dao.insertarMascotas(mascotasRemotas) // Asegúrate de tener un insertAll en tu DAO
            Log.d("REPO", "Sincronización exitosa")
        } catch (e: Exception) {
            Log.e("REPO", "Error sincronizando: ${e.message}")
        }
    }

    suspend fun guardarMascota(
        nombre: String,
        tipo: String,
        raza: String,
        edadAnios: Int?,
        edadMeses: Int?,
        fotoUri: String?,
        clienteId: Long
    ) {
        val entity = MascotaEntity(
            nombre = nombre,
            tipo = tipo,
            raza = raza,
            edadAnios = edadAnios,
            edadMeses = edadMeses,
            fotoUri = fotoUri,
                    clienteId = 1L
        )

        // 1. Guardar en Local
        dao.insertarMascota(entity)

        // 2. Intentar guardar en Backend
        try {
            apiService.saveMascota(entity)
        } catch (e: Exception) {
            Log.e("REPO", "Error al guardar en la nube: ${e.message}")
        }
    }

    // Mantenemos obtenerMascotas apuntando al Flow de Room (Offline-first)
    fun obtenerMascotas(): Flow<List<MascotaEntity>> = dao.obtenerMascotas()

    suspend fun obtenerMascotaPorId(idMascota: Long): MascotaEntity? =
        dao.obtenerMascotaPorId(idMascota)

    suspend fun actualizarMascota(mascota: MascotaEntity) {
        // 1. Local
        dao.actualizarMascota(mascota)
        // 2. Remoto (asumiendo que tienes un método update en tu apiService)
        try {
            // apiService.updateMascota(mascota.id, mascota)
        } catch (e: Exception) {
            Log.e("REPO", "Error al actualizar en la nube")
        }
    }

    suspend fun eliminarMascota(mascota: MascotaEntity) {
        dao.eliminarMascota(mascota)
        try {
            // apiService.deleteMascota(mascota.id)
        } catch (e: Exception) {}
    }

    suspend fun eliminarMascotaPorId(idMascota: Long) {
        dao.eliminarMascotaPorId(idMascota)
        // Lógica similar para el backend si tienes el endpoint
    }
}