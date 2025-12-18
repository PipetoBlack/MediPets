package com.example.medipets.model.data.entities
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cita_mascota",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MascotaEntity::class,
            parentColumns = ["id"],
            childColumns = ["mascota_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VeterinarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["veterinario_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class FormularioCitaMascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuario_id: Long,
    val mascota_id: Long,
    val veterinario_id: Long?,
    val fecha: String,      // o Date si quieres manejar fechas
    val motivo: String
)
