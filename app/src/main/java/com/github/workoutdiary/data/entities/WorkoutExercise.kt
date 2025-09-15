package com.github.workoutdiary.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ColumnInfo
import com.github.workoutdiary.Constants

@Entity(
    tableName = Constants.TablenameExercise.NAME,
    indices = [
        Index(value = ["workout_id"]),
        Index(value = ["name"]),
        Index(value = ["workout_id", "exercise_name"], unique = true),
        Index(value = ["muscle_group"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntry::class,
            parentColumns = ["id"],
            childColumns = ["workout_id"],
            onDelete = CASCADE
        )
    ]
)
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "workout_id")
    val workoutId: Long,
    @ColumnInfo(name = "exercise_name")
    val exerciseName: String,
    @ColumnInfo(name = "muscle_group")
    val muscleGroup: String?,
    @ColumnInfo(name = "photo_uri")
    val photoUri: String? = null
)