package com.github.workoutdiary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.workoutdiary.models.WorkoutWithExercises
import com.github.workoutdiary.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.github.workoutdiary.R

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {
    private val _workoutsState = MutableStateFlow<WorkoutListState>(WorkoutListState.Loading)
    val workoutsState: StateFlow<WorkoutListState> = _workoutsState.asStateFlow()

    init {
        loadWorkoutsWithExercises()
    }

    fun loadWorkoutsWithExercises() {
        viewModelScope.launch {
            _workoutsState.value = WorkoutListState.Loading
            try {
                val workouts = repository.getAllWorkouts()

                val workoutsWithExercises = workouts.map { workout ->
                    WorkoutWithExercises(
                        workout = workout,
                        exercises = repository.getExercisesByWorkoutId(workout.id)
                    )
                }

                _workoutsState.value = WorkoutListState.Success(workoutsWithExercises)
            } catch (e: Exception) {
                _workoutsState.value = WorkoutListState.Error(
                    R.string.loading_error_message,
                    arrayOf(e.message ?: "")
                )
            }
        }
    }

}

sealed class WorkoutListState {
    object Loading : WorkoutListState()
    data class Success(val workouts: List<WorkoutWithExercises>) : WorkoutListState()
    data class Error(val messageResId: Int, val formatArgs: Array<Any> = emptyArray()) : WorkoutListState()
}