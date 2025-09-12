package com.github.workoutdiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.workoutdiary.data.entities.WorkoutEntry
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