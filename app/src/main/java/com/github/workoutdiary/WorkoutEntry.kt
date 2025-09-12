package com.github.workoutdiary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Constants.Tablename.NAME)
data class WorkoutEntry (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Long,
    val exerciseName: String,
    val targetMuscle: String,
    val setNumber: Int,
    val weight: Double,
    val reps: Int
)