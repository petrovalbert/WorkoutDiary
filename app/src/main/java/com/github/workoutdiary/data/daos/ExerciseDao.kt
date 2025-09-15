package com.github.workoutdiary.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Query
import androidx.room.ForeignKey
import androidx.room.Index
import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.Constants
import com.github.workoutdiary.data.entities.WorkoutExercise

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: WorkoutExercise): Long

    @Update
    suspend fun update(exercise: WorkoutExercise): Int

    @Delete
    suspend fun delete(exercise: WorkoutExercise): Int

    // Get exercises by workout id
    @Query("SELECT * FROM ${Constants.TablenameExercise.NAME} " +
            "WHERE workout_id = :workoutId ORDER BY id")
    suspend fun getExercisesByWorkoutId(workoutId: Long): List<WorkoutExercise>

    // Get exercises by muscle_group
    @Query("SELECT * FROM ${Constants.TablenameExercise.NAME} " +
            "WHERE muscle_group = :muscleGroup ORDER BY id")
    suspend fun getExercisesByWorkoutId(muscleGroup: String): List<WorkoutExercise>

    // Check existing of exercise with the same name and workoutId
    @Query("SELECT COUNT(*) FROM ${Constants.TablenameExercise.NAME}" +
            " WHERE workout_id = :workoutId AND exercise_name = :exerciseName")
    suspend fun exerciseExists(workoutId: Long, exerciseName: String): Int

    // Get count of exercises by workoutId
    @Query("SELECT COUNT(*) FROM ${Constants.TablenameExercise.NAME}" +
            " WHERE workout_id = :workoutId")
    suspend fun getExerciseCountForWorkout(workoutId: Long): Int

    // Удаление всех упражнений тренировки (при удалении тренировки)
    // Deleting all exercises by workoutId
    @Query("DELETE FROM ${Constants.TablenameExercise.NAME} WHERE workout_id = :workoutId")
    suspend fun deleteExercisesForWorkout(workoutId: Long): Int
}