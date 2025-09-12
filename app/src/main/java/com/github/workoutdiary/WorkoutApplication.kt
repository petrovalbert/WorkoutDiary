package com.github.workoutdiary

import android.app.Application
import com.github.workoutdiary.data.AppDatabase

class WorkoutApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}