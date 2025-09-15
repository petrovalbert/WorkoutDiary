package com.github.workoutdiary.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.Constants

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(entry: WorkoutEntry): Long

    @Query("SELECT * FROM ${Constants.TablenameWorkout.NAME} ORDER BY date DESC")
    suspend fun getAllEntries(): List<WorkoutEntry>
}