package com.example.medipets.model.data.network.service

import com.example.medipets.model.data.entities.MascotaEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MascotaApiService {
    @GET("mascotas") // Ruta exacta de tu @GetMapping en Spring
    suspend fun getAllMascotas(): List<MascotaEntity>

    @POST("/api/mascotas/crear") // Ruta exacta de tu @PostMapping en Spring
    suspend fun saveMascota(@Body mascota: MascotaEntity): MascotaEntity

    @GET("api/razas")
    suspend fun obtenerRazas(@Query("tipo") tipo: String): List<String>
}