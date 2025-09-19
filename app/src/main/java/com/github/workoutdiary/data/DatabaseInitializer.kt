package com.github.workoutdiary.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.data.entities.WorkoutExercise
import com.github.workoutdiary.data.entities.WorkoutSet

class DatabaseInitializer {
    companion object {
        suspend fun initialize(context: Context): AppDatabase {
            return withContext(Dispatchers.IO) {
                val database = AppDatabase.getInstance(context)
                val workoutDao = database.workoutDao()

                val workoutCount = workoutDao.getWorkoutCount()
                if (workoutCount == 0) {
                    populateDatabase(database)
                }
                database
            }
        }
        suspend fun populateDatabase(database: AppDatabase) {
            withContext(Dispatchers.IO) {
                try {
                    val workoutDao = database.workoutDao()
                    val exerciseDao = database.exerciseDao()
                    val setDao = database.setDao()

                    val testWorkout = WorkoutEntry(
                        id = 0L,
                        date = 1758186050,
                        name = "workout1",
                        notes = ""
                    )
                    val workoutId = workoutDao.insert(testWorkout)

                    val exercises = listOf(
                        WorkoutExercise(
                            id = 0L,
                            workoutId = workoutId,
                            exerciseName = "Squats",
                            muscleGroup = "Quads"
                        ),
                        WorkoutExercise(
                            id = 0L,
                            workoutId = workoutId,
                            exerciseName = "Bench press",
                            muscleGroup = "Chest"
                        )
                    )
                    val exerciseIds = mutableListOf<Long>()
                    exercises.forEach { exercise ->
                        val exerciseId = exerciseDao.insert(exercise)
                        exerciseIds.add(exerciseId)
                    }

                    val squatSets = listOf(
                        WorkoutSet(
                            id = 0L,
                            exerciseId = exerciseIds[0], // Ссылаемся на ID упражнения "Squats"
                            setNumber = 1,
                            weight = 20.0,
                            reps = 20
                        ),
                        WorkoutSet(
                            id = 0L,
                            exerciseId = exerciseIds[0],
                            setNumber = 2,
                            weight = 40.0,
                            reps = 10
                        ),
                        WorkoutSet(
                            id = 0L,
                            exerciseId = exerciseIds[0],
                            setNumber = 3,
                            weight = 70.0,
                            reps = 5
                        )

                    )
                    setDao.insertAll(squatSets)

                    val benchSets = listOf(
                        WorkoutSet(
                            id = 0L,
                            exerciseId = exerciseIds[1], // Ссылаемся на ID упражнения "Bench press"
                            setNumber = 1,
                            weight = 20.0,
                            reps = 20
                        ),
                        WorkoutSet(
                            id = 0L,
                            exerciseId = exerciseIds[1],
                            setNumber = 2,
                            weight = 40.0,
                            reps = 10
                        ),
                        WorkoutSet(
                            id = 0L,
                            exerciseId = exerciseIds[1],
                            setNumber = 3,
                            weight = 70.0,
                            reps = 5
                        )
                    )
                    setDao.insertAll(benchSets)
                } catch (e: Exception) {
                    throw e
                }
            }
        }
    }
}