package com.example.app_kotlin.data

import android.content.Context
import androidx.room.Room

object DatabaseClient {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_kotlin_db"
            )
                .fallbackToDestructiveMigration() // elimina datos si cambias versión
                .build()

            INSTANCE = instance
            instance
        }
    }
}
