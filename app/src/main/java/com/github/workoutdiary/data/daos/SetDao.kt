package com.github.workoutdiary.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Query
import com.github.workoutdiary.Constants
import com.github.workoutdiary.data.entities.WorkoutSet

@Dao
interface SetDao {
    // Use *All for all type of CRUD request even if one set
    @Insert
    suspend fun insertAll(sets: List<WorkoutSet>): List<Long>

    @Update
    suspend fun updateAll(sets: List<WorkoutSet>): Int

    @Delete
    suspend fun deleteAll(sets: List<WorkoutSet>): Int

    // Get all sets by exercise id
    @Query("SELECT * FROM ${Constants.TablenameSets.NAME} " +
            "WHERE exercise_id = :exerciseId ORDER BY id")
    suspend fun getSetsByExerciseId(exerciseId: Long): List<WorkoutSet>

    // Get one set by id
    @Query("SELECT * FROM ${Constants.TablenameSets.NAME} WHERE id = :setId")
    suspend fun getSetById(setId: Long): WorkoutSet?

    // Get next set number by exercise id
    @Query("SELECT MAX(set_number) FROM ${Constants.TablenameSets.NAME} " +
            "WHERE exercise_id = :exerciseId")
    suspend fun getNextSetNumber(exerciseId: Long): Int?

    // Delete all sets (after exercise deleting)
    @Query("DELETE FROM ${Constants.TablenameSets.NAME} " +
            "WHERE exercise_id = :exerciseId")
    suspend fun deleteSetsForExercise(exerciseId: Long): Int

    // Get the maximum weight in exercise (for stats)
    @Query("SELECT MAX(weight) FROM ${Constants.TablenameSets.NAME} " +
            "WHERE exercise_id = :exerciseId")
    suspend fun getMaxWeightForExercise(exerciseId: Long): Double?

    // Check existing the set in exercise by number
    @Query("SELECT COUNT(*) FROM ${Constants.TablenameSets.NAME} " +
            "WHERE exercise_id = :exerciseId AND set_number = :setNumber")
    suspend fun setExists(exerciseId: Long, setNumber: Int): Int

}