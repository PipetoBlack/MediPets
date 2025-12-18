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
    version = 15,
    exportSchema = false
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
                    // ⚠️ QUITA fallbackToDestructiveMigration DURANTE DESARROLLO
                    .fallbackToDestructiveMigration()

                    .addCallback(object : RoomDatabase.Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Se ejecuta SOLO la primera vez que se crea la BD
                            CoroutineScope(Dispatchers.IO).launch {

                                // ⚠️ USAMOS LA INSTANCIA, NO INSTANCE (que aún es null)
                                getDatabase(context).apply {

                                    // Mascota de prueba
                                    mascotaDao().insertarMascota(
                                        MascotaEntity(
                                            nombre = "Firulais",
                                            tipo = "Perro",
                                            raza = "Mestizo",
                                            edadAnios = 3,
                                            edadMeses = 0,
                                            fotoUri = null
                                        )
                                    )

                                    // Veterinario de prueba
                                    veterinarioDao().insertarVeterinario(
                                        VeterinarioEntity(
                                            nombre = "Dr. Carlos",
                                            especialidad = "General",
                                            correo = "carlos@vet.com",
                                            telefono = "123456789"
                                        )
                                    )

                                    // Usuario de prueba (MUY IMPORTANTE)
                                    usuarioDao().insertar(
                                        UsuarioEntity(
                                            nombre = "Usuario Test",
                                            email = "test@test.com",
                                            password = "1234"
                                        )
                                    )
                                }
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
