package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookingEntity::class], version = 1, exportSchema = false)
abstract class SafarGoDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: SafarGoDatabase? = null

        fun getInstance(context: Context): SafarGoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SafarGoDatabase::class.java,
                    "safargo_travel.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
