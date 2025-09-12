package com.github.workoutdiary.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.workoutdiary.Constants

@Entity(tableName = Constants.TablenameExercise.NAME)
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val exerciseName: String,
    val targetMuscle: String,
    val photoUri: String? = null
)