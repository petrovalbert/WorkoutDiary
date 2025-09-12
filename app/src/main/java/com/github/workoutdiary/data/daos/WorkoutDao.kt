package com.github.workoutdiary.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.workoutdiary.data.entities.WorkoutEntry

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(entry: WorkoutEntry): Long

    @Query("SELECT * FROM workout_entries ORDER BY date DESC")
    suspend fun getAllEntries(): List<WorkoutEntry>
}