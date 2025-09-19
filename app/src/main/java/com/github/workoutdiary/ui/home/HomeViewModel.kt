package com.github.workoutdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.repository.WorkoutRepository

class HomeViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _workouts = MutableStateFlow<List<WorkoutEntry>>(emptyList())

    val workouts: StateFlow<List<WorkoutEntry>> = _workouts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadWorkouts() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val workoutsList = repository.getAllWorkouts()
                _workouts.value = workoutsList
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshWorkouts() {
        loadWorkouts()
    }

    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            try {
                val workoutToDelete = repository.getWorkoutById(workoutId)
                workoutToDelete?.let {
                    repository.deleteWorkout(it)
                    loadWorkouts()
                }
            } catch (e: Exception) {
                _error.value = "Error during deleting: ${e.message}"
            }
        }
    }
}