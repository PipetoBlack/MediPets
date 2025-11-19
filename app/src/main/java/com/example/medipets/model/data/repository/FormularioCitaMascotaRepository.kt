package com.example.medipets.model.data.repository

import com.example.medipets.model.data.dao.FormularioCitaMascotaDao
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity

/**
 * Repositorio para la entidad FormularioCitaMascota.
 * Actúa como intermediario entre el ViewModel y la fuente de datos (el DAO).
 * Su única responsabilidad es gestionar los datos de las citas.
 *
 * @param dao El Objeto de Acceso a Datos (DAO) para las citas, que Room inyectará.
 */
class FormularioCitaMascotaRepository(private val dao: FormularioCitaMascotaDao) {

    /**
     * Inserta una nueva cita en la base de datos.
     * Esta es una función suspendida porque interactúa con la base de datos,
     * lo cual es una operación que no debe bloquear el hilo principal.
     *
     * @param cita El objeto de la entidad [FormularioCitaMascotaEntity] que se va a guardar.
     */
    suspend fun insert(cita: FormularioCitaMascotaEntity) {
        dao.insert(cita)
    }

    // En el futuro, si necesitas obtener datos, puedes añadir funciones aquí. Por ejemplo:
    /*
    suspend fun getAllCitas(): List<FormularioCitaMascotaEntity> {
        return dao.getAll() // Suponiendo que has creado la función getAll() en tu DAO
    }
    */
}
