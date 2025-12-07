package com.example.medipets.model.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medipets.model.data.dao.FormularioServicioDao
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity
import com.example.medipets.model.data.entities.FormularioServicioEntity
import com.example.medipets.model.data.entities.VeterinarioEntity
import com.example.medipets.model.data.dao.VeterinarioDao
import com.example.medipets.model.data.dao.FormularioCitaMascotaDao
import com.example.medipets.model.data.dao.MascotaDao
import com.example.medipets.model.data.entities.MascotaEntity

@Database(
    entities = [FormularioServicioEntity::class,
                FormularioCitaMascotaEntity::class,
                VeterinarioEntity::class,
                MascotaEntity::class
               ],
    version = 4, // Si cambias el esquema, aumenta el número de versión
    exportSchema = false // Buena práctica para evitar warnings
)
abstract class AppDatabase : RoomDatabase() {

    // ¡ESTA ES LA LÍNEA QUE DEBES AÑADIR EN ESTE ARCHIVO!
    abstract fun formularioServicioDao(): FormularioServicioDao
    abstract fun formularioCitaMascotaDao(): FormularioCitaMascotaDao

    abstract fun veterinarioDao(): VeterinarioDao

    abstract fun mascotaDao(): MascotaDao

    // Y el companion object para crear la base de datos
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
