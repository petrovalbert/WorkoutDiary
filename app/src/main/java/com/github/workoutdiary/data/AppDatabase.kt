package com.github.workoutdiary.data

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.github.workoutdiary.Constants
import com.github.workoutdiary.data.daos.ExerciseDao
import com.github.workoutdiary.data.daos.SetDao
import com.github.workoutdiary.data.daos.WorkoutDao
import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.data.entities.WorkoutExercise
import com.github.workoutdiary.data.entities.WorkoutSet
import kotlinx.coroutines.CoroutineScope


@Database(
    entities = [
        WorkoutEntry::class,
        WorkoutExercise::class,
        WorkoutSet::class,
    ],
    version = 1, exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun setDao(): SetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, applicationScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.Database.NAME
                )
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance

                val populator = DatabasePopulator(instance, applicationScope)
                populator.populateIfNeeded()

                instance
            }
        }
    }
}