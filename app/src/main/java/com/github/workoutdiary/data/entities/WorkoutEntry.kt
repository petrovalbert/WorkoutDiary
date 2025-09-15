package com.github.workoutdiary.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.github.workoutdiary.Constants

@Entity(
    tableName = Constants.TablenameWorkout.NAME,
    indices = [
        Index(value = ["date"], unique = false)
    ]
    )
data class WorkoutEntry (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Long,
    val name: String?
)