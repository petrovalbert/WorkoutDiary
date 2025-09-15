package com.github.workoutdiary.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.Constants

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(entry: WorkoutEntry): Long

    @Delete
    suspend fun delete(entry: WorkoutEntry): Int

    // Get all workouts
    @Query("SELECT * FROM ${Constants.TablenameWorkout.NAME} ORDER BY date DESC")
    suspend fun getAllEntries(): List<WorkoutEntry>

    // Get a workout by id number
    @Query("SELECT * FROM ${Constants.TablenameWorkout.NAME} WHERE id = :id")
    suspend fun getWorkoutByID(id: Long): WorkoutEntry?

    // Get count of workouts
    @Query("SELECT COUNT(*) FROM ${Constants.TablenameWorkout.NAME}")
    suspend fun getWorkoutCount(): Int

    // Get workouts by date
    @Query("SELECT * FROM ${Constants.TablenameWorkout.NAME} WHERE date = :date")
    suspend fun getWorkoutsByDate(date: Long): List<WorkoutEntry>
}