package com.example.medipets.model.data.entities
import com.example.medipets.model.data.entities.FormularioServicioEntity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "formulario_cita_mascota",
    foreignKeys = [
        ForeignKey(
            entity = FormularioServicioEntity::class,
            parentColumns = ["id"],
            childColumns = ["formulario_servicio_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FormularioCitaMascotaEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val formulario_servicio_id: Long,
    val nombreMascota: String,
    val raza: String,
    val edad: Int,
    val fecha: String,
    val motivo: String


)