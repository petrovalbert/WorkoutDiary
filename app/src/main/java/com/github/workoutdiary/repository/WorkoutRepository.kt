package com.github.workoutdiary.repository

//DAO imports
import com.github.workoutdiary.data.daos.WorkoutDao
import com.github.workoutdiary.data.daos.ExerciseDao
import com.github.workoutdiary.data.daos.SetDao
import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.data.entities.WorkoutExercise
import com.github.workoutdiary.data.entities.WorkoutSet

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val setDao: SetDao
) {
    // Workout operations

    suspend fun insertWorkout(workout: WorkoutEntry): Long {
        return workoutDao.insert(workout)
    }

    suspend fun deleteWorkout(workout: WorkoutEntry): Int {
        return workoutDao.delete(workout)
    }

    suspend fun getAllWorkouts(): List<WorkoutEntry> {
        return workoutDao.getAllEntries()
    }

    suspend fun getWorkoutById(id: Long): WorkoutEntry? {
        return workoutDao.getWorkoutByID(id)
    }

    suspend fun getWorkoutByDate(date: Long): List<WorkoutEntry> {
        return workoutDao.getWorkoutsByDate(date)
    }

    suspend fun getWorkoutCount(): Int {
        return workoutDao.getWorkoutCount()
    }

    // Exercises operations

    suspend fun insertExercise(exercise: WorkoutExercise): Long {
        return exerciseDao.insert(exercise)
    }

    suspend fun updateExercise(exercise: WorkoutExercise): Int {
        return exerciseDao.update(exercise)
    }

    suspend fun deleteExercise(exercise: WorkoutExercise): Int {
        return exerciseDao.delete(exercise)
    }

    suspend fun getExercisesByWorkoutId(workoutId: Long): List<WorkoutExercise> {
        return exerciseDao.getExercisesByWorkoutId(workoutId)
    }

    suspend fun getExercisesByMuscleGroup(muscleGroup: String): List<WorkoutExercise> {
        return exerciseDao.getExercisesByWorkoutId(muscleGroup)
    }

    suspend fun exerciseExists(workoutId: Long, exerciseName: String): Boolean {
        return exerciseDao.exerciseExists(workoutId, exerciseName) > 0
    }

    suspend fun getExerciseCountForWorkout(workoutId: Long): Int {
        return exerciseDao.getExerciseCountForWorkout(workoutId)
    }

    suspend fun deleteExercisesForWorkout(workoutId: Long): Int {
        return exerciseDao.deleteExercisesForWorkout(workoutId)
    }

    // Sets operations

    suspend fun insertSets(sets: List<WorkoutSet>): Long {
        return setDao.insertAll(sets)
    }

    suspend fun updateSets(sets: List<WorkoutSet>): Int {
        return setDao.updateAll(sets)
    }

    suspend fun deleteSets(sets: List<WorkoutSet>): Int {
        return setDao.deleteAll(sets)
    }

    suspend fun getSetsByExerciseId(exerciseId: Long): List<WorkoutSet> {
        return setDao.getExercisesByWorkoutId(exerciseId)
    }

    suspend fun getSetById(setId: Long): WorkoutSet? {
        return setDao.getSetById(setId)
    }

    suspend fun getNextSetNumber(exerciseId: Long): Int {
        return setDao.getNextSetNumber(exerciseId) ?: 0
    }

    suspend fun deleteSetsForExercise(exerciseId: Long): Int {
        return setDao.deleteSetsForExercise(exerciseId)
    }

    suspend fun getMaxWeightForExercise(exerciseId: Long): Double {
        return setDao.getMaxWeightForExercise(exerciseId) ?: 0.0
    }

    suspend fun setExists(exerciseId: Long, setNumber: Int): Boolean {
        return setDao.setExists(exerciseId, setNumber) > 0
    }
}