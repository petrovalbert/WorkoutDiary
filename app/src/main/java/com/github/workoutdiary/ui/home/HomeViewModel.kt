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


    init {
        viewModelScope.launch {
            repository.getAllWorkouts().collect { workoutList ->
                _workouts.value = workoutList
            }
        }
    }


    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            try {
                val workoutToDelete = repository.getWorkoutById(workoutId)
                workoutToDelete?.let {
                    repository.deleteWorkout(it)
                }
            } catch (e: Exception) {
                _error.value = "Error during deleting: ${e.message}"
            }
        }
    }
}