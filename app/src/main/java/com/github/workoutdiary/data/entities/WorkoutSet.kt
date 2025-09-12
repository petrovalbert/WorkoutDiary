package com.github.workoutdiary.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.workoutdiary.Constants

@Entity(tableName = Constants.TablenameSets.NAME)
data class WorkoutSet (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val exerciseId: Long,
    val weight: Double,
    val reps: Int,
    val setNumber: Int
)
