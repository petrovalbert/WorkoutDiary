package com.github.workoutdiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as WorkoutApplication).database
    private val workoutDao = database.workoutDao()

    val workouts = MutableLiveData<List<Workout>>()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            val entries = workoutDao.getAllEntries()
            val groupedWorkouts = groupEntriesByDate(entries)
            workouts.postValue(groupedWorkouts)
        }
    }

    private fun groupEntriesByDate(entries: List<WorkoutEntry>): List<Workout> {
        return entries.groupBy { it.date }
            .map { (date, exercises) ->
                Workout(
                    date = date,
                    exercises = exercises,
                    isExpanded = false
                )
            }
            .sortedByDescending { it.date }
    }

    fun addTestWorkout() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            // Создаем несколько упражнений для одной тренировки
            val testExercises = listOf(
                WorkoutEntry(
                    date = currentTime,
                    exerciseName = "Приседания",
                    targetMuscle = "Ноги",
                    setNumber = 1,
                    weight = 70.0,
                    reps = 10
                ),
                WorkoutEntry(
                    date = currentTime,
                    exerciseName = "Жим лежа",
                    targetMuscle = "Грудь",
                    setNumber = 1,
                    weight = 60.0,
                    reps = 8
                ),
                WorkoutEntry(
                    date = currentTime,
                    exerciseName = "Подтягивания",
                    targetMuscle = "Спина",
                    setNumber = 1,
                    weight = 0.0,
                    reps = 12
                )
            )

            testExercises.forEach { workoutDao.insert(it) }
            loadWorkouts() // Перезагружаем список
        }
    }

    fun toggleWorkoutExpanded(date: Long) {
        val currentWorkouts = workouts.value ?: return
        val updatedWorkouts = currentWorkouts.map { workout ->
            if (workout.date == date) {
                workout.copy(isExpanded = !workout.isExpanded)
            } else {
                workout
            }
        }
        workouts.postValue(updatedWorkouts)
    }
}

// Новая data class для представления тренировки
data class Workout(
    val date: Long,
    val exercises: List<WorkoutEntry>,
    val isExpanded: Boolean = false
)