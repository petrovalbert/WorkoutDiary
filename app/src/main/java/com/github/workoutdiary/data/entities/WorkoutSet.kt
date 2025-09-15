package com.github.workoutdiary.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.github.workoutdiary.Constants

@Entity(
    tableName = Constants.TablenameSets.NAME,
    indices = [
        Index(value = ["exercise_id"]),
        Index(value = ["exercise_id", "set_number"], unique = true),
    ],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = CASCADE
        )
    ]
)
data class WorkoutSet (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Long,
    @ColumnInfo(name = "set_number")
    val setNumber: Int,
    val weight: Double,
    val reps: Int
)
