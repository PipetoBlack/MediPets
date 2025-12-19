package com.example.medipets.model.data.dao

import androidx.room.*
import com.example.medipets.model.data.entities.MascotaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {

    @Insert
    suspend fun insertarMascota(mascota: MascotaEntity)

    @Query("SELECT * FROM mascota ORDER BY id DESC")
    fun obtenerMascotas(): Flow<List<MascotaEntity>>

    @Query("SELECT * FROM mascota WHERE id = :idMascota")
    suspend fun obtenerMascotaPorId(idMascota: Long): MascotaEntity?

    @Update
    suspend fun actualizarMascota(mascota: MascotaEntity)

    @Delete
    suspend fun eliminarMascota(mascota: MascotaEntity)

    @Query("DELETE FROM mascota WHERE id = :idMascota")
    suspend fun eliminarMascotaPorId(idMascota: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMascotas(mascotas: List<MascotaEntity>)
}