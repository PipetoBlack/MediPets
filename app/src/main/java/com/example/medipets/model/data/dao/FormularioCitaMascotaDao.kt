package com.example.medipets.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity

//En resumen, el DAO es una interfaz que define una lista de funciones permitidas para interactuar
//con una tabla de la base de datos. No contiene la lógica de cómo se hacen las cosas, solo dice
//qué se puede hacer.

@Dao
interface FormularioCitaMascotaDao {
    @Insert fun insert(cita: FormularioCitaMascotaEntity)
}