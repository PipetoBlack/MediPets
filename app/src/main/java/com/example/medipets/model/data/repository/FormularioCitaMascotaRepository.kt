package com.example.medipets.model.data.repository

import com.example.medipets.model.data.dao.FormularioCitaMascotaDao
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity

class FormularioCitaMascotaRepository(private val dao: FormularioCitaMascotaDao) {

    // Insertar nueva cita
    suspend fun agendarCita(cita: FormularioCitaMascotaEntity) {
        dao.insertar(cita)
    }

    // Obtener todas las citas de un usuario
    suspend fun obtenerCitasPorUsuario(usuarioId: Long): List<FormularioCitaMascotaEntity> {
        return dao.obtenerCitasPorUsuario(usuarioId)
    }

    // Obtener citas con detalle (mascota + veterinario)
    suspend fun obtenerCitasConDetalle(usuarioId: Long): List<FormularioCitaMascotaEntity> {
        return dao.obtenerCitasConDetalle(usuarioId)
    }
}
