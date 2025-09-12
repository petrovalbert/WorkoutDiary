package com.github.workoutdiary

import android.app.Application

class WorkoutApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}