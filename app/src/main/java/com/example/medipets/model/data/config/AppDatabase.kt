package com.example.medipets.model.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medipets.model.data.dao.*
import com.example.medipets.model.data.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FormularioCitaMascotaEntity::class,
        VeterinarioEntity::class,
        MascotaEntity::class,
        UsuarioEntity::class
    ],
    version = 10,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun formularioCitaMascotaDao(): FormularioCitaMascotaDao
    abstract fun veterinarioDao(): VeterinarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medipets.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getDatabase(context).usuarioDao()
                                dao.insertar(
                                    UsuarioEntity(
                                        nombre = "Felipe",
                                        email = "felipe@duoc.cl",
                                        password = "Kawazaki7991+"
                                    )
                                )
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
