package com.example.medipets.model.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medipets.model.data.dao.FormularioServicioDao
import com.example.medipets.model.data.dao.PacienteDao
import com.example.medipets.model.data.entities.FormularioServicioEntity
import com.example.medipets.model.data.entities.PacienteEntity

@Database(
    entities = [FormularioServicioEntity::class, PacienteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun formularioServicio(): FormularioServicioDao
    abstract fun pacienteDao(): PacienteDao

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