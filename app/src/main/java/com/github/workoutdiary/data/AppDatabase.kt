package com.github.workoutdiary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.workoutdiary.Constants
import com.github.workoutdiary.data.daos.WorkoutDao
import com.github.workoutdiary.data.entities.WorkoutEntry

@Database(entities = [WorkoutEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.Database.NAME
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}