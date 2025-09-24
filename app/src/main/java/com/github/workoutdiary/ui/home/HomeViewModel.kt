package com.github.workoutdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.data.entities.WorkoutExercise
import com.github.workoutdiary.repository.WorkoutRepository

class HomeViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _workouts = MutableStateFlow<List<WorkoutEntry>>(emptyList())

    val workouts: StateFlow<List<WorkoutEntry>> = _workouts.asStateFlow()

    private val _expandedWorkouts = MutableStateFlow<Set<Long>>(emptySet())
    val expandedWorkouts: StateFlow<Set<Long>> = _expandedWorkouts.asStateFlow()

    private val exercisesCache = mutableMapOf<Long, List<WorkoutExercise>>()
    private val _loadingStates = MutableStateFlow<Set<Long>>(emptySet())
    val loadingStates: StateFlow<Set<Long>> = _loadingStates.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    init {
        viewModelScope.launch {
            repository.getAllWorkouts().collect { workoutsList ->
                _workouts.value = workoutsList
            }
        }
    }

    fun toggleWorkoutExpansion(workoutId: Long) {
        viewModelScope.launch {
            val currentlyExpanded = _expandedWorkouts.value.contains(workoutId)

            if (currentlyExpanded) {
                _expandedWorkouts.value = _expandedWorkouts.value - workoutId
                _loadingStates.value = _loadingStates.value - workoutId
            } else {
                _expandedWorkouts.value = _expandedWorkouts.value + workoutId

                if (!exercisesCache.containsKey(workoutId)) {
                    _loadingStates.value = _loadingStates.value + workoutId
                    try {
                        val exercises = repository.getExercisesForWorkout(workoutId)
                        exercisesCache[workoutId] = exercises
                    } catch (e: Exception) {
                        _error.value = "Ошибка загрузки упражнений: ${e.message}"
                    } finally {
                        _loadingStates.value = _loadingStates.value - workoutId
                    }
                }
            }
        }
    }

    fun getExercisesForWorkout(workoutId: Long): List<WorkoutExercise> {
        return exercisesCache[workoutId] ?: emptyList()
    }
}