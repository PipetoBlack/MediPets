package com.example.medipets.model.data.repository

import com.example.medipets.model.data.dao.FormularioCitaMascotaDao
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity

class FormularioCitaMascotaRepository(private val dao: FormularioCitaMascotaDao) {

    suspend fun insert(cita: FormularioCitaMascotaEntity) {
        dao.insert(cita)
    }

    // se puede añadir nuevas funciones aquí. como getAll() en tu DAO
}
