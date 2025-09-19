package com.github.workoutdiary.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.data.entities.WorkoutExercise
import com.github.workoutdiary.data.entities.WorkoutSet

class DatabasePopulator(
    private val database: AppDatabase,
    private val applicationScope: CoroutineScope
) {
    fun populateIfNeeded() {
        applicationScope.launch {
            val workoutCount = database.workoutDao().getWorkoutCount()
            if (workoutCount == 0) {
                populateDatabase()
            }
        }
    }

    private suspend fun populateDatabase() {
        val testWorkout = WorkoutEntry(
            id = 0L,
            date = 1758186050,
            name = "workout1",
            notes = ""
        )
        val insertedWorkoutId = database.workoutDao().insert(testWorkout)

        val exerciseSquats = WorkoutExercise(
            id = 0L,
            workoutId = insertedWorkoutId,
            exerciseName = "Squats",
            muscleGroup = "Quads"
        )
        val insertedSquats = database.exerciseDao().insert(exerciseSquats)

        val exercisesBenchPress = WorkoutExercise(
            id = 0L,
            workoutId = insertedWorkoutId,
            exerciseName = "Bench press",
            muscleGroup = "Chest"
        )
        val insertedBenchPress = database.exerciseDao().insert(exercisesBenchPress)

        // 3. Создаем и вставляем подходы для упражнения "Squats"
        val squatSets = listOf(
            WorkoutSet(
                id = 0L,
                exerciseId = insertedSquats, // Ссылаемся на ID упражнения "Squats"
                setNumber = 1,
                weight = 20.0,
                reps = 20
            ),
            WorkoutSet(
                id = 0L,
                exerciseId = insertedSquats,
                setNumber = 2,
                weight = 40.0,
                reps = 10
            ),
            WorkoutSet(
                id = 0L,
                exerciseId = insertedSquats,
                setNumber = 3,
                weight = 70.0,
                reps = 5
            )
        )
        database.setDao().insertAll(squatSets)

        // 4. Создаем и вставляем подходы для упражнения "Bench press"
        val benchSets = listOf(
            WorkoutSet(
                id = 0L,
                exerciseId = insertedBenchPress, // Ссылаемся на ID упражнения "Bench press"
                setNumber = 1,
                weight = 20.0,
                reps = 20
            ),
            WorkoutSet(
                id = 0L,
                exerciseId = insertedBenchPress,
                setNumber = 2,
                weight = 40.0,
                reps = 10
            ),
            WorkoutSet(
                id = 0L,
                exerciseId = insertedBenchPress,
                setNumber = 3,
                weight = 70.0,
                reps = 5
            )
        )
        database.setDao().insertAll(benchSets)
    }
}