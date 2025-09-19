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
            id = 1,
            date = 1758186050,
            name = "workout1",
            notes = ""
        )
        database.workoutDao().insert(testWorkout)

        val exerciseSquats = WorkoutExercise(
            id = 1,
            workoutId = 1,
            exerciseName = "Squats",
            muscleGroup = "Quads"
        )
        database.exerciseDao().insert(exerciseSquats)

        val exercisesBenchPress = WorkoutExercise(
            id = 2,
            workoutId = 1,
            exerciseName = "Bench press",
            muscleGroup = "Chest"
        )
        database.exerciseDao().insert(exercisesBenchPress)

        // 3. Создаем и вставляем подходы для упражнения "Squats"
        val squatSet1 = WorkoutSet(
            id = 1,
            exerciseId = 1, // Ссылаемся на ID упражнения "Squats"
            setNumber = 1,
            weight = 20.0,
            reps = 20
        )
        val squatSet2 = WorkoutSet(
            id = 2,
            exerciseId = 1,
            setNumber = 2,
            weight = 40.0,
            reps = 10
        )
        val squatSet3 = WorkoutSet(
            id = 3,
            exerciseId = 1,
            setNumber = 3,
            weight = 70.0,
            reps = 5
        )
        database.setDao().insertAll(listOf(squatSet1, squatSet2, squatSet3))

        // 4. Создаем и вставляем подходы для упражнения "Bench press"
        val benchSet1 = WorkoutSet(
            id = 4,
            exerciseId = 2, // Ссылаемся на ID упражнения "Bench press"
            setNumber = 1,
            weight = 20.0,
            reps = 20
        )
        val benchSet2 = WorkoutSet(
            id = 5,
            exerciseId = 2,
            setNumber = 2,
            weight = 40.0,
            reps = 10
        )
        val benchSet3 = WorkoutSet(
            id = 6,
            exerciseId = 2,
            setNumber = 3,
            weight = 70.0,
            reps = 5
        )
        database.setDao().insertAll(listOf(benchSet1, benchSet2, benchSet3))
    }
}