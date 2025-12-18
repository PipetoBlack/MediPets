package com.example.medipets.model.data.repository

import com.example.medipets.model.data.dao.UsuarioDao
import com.example.medipets.model.data.entities.UsuarioEntity

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun registrarUsuario(nombre: String, email: String, password: String) {
        val usuario = UsuarioEntity(
            nombre = nombre,
            email = email,
            password = password
        )
        dao.insertar(usuario)
    }

    suspend fun existeEmail(email: String): Boolean {
        return dao.getUsuarioByEmail(email) != null
    }
}
