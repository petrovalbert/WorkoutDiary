package com.github.workoutdiary.data

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.github.workoutdiary.data.DatabasePopulator

class DatabaseInitializer {
    companion object {

        @WorkerThread
        suspend fun initialize(context: Context): AppDatabase {
            return withContext(Dispatchers.IO) {
                val database = AppDatabase.getInstance(context)
                val workoutDao = database.workoutDao()

                val workoutCount = workoutDao.getWorkoutCount()
                if (workoutCount == 0) {
                    DatabasePopulator.populateDatabase(database)
                }
                database
            }
        }
    }
}