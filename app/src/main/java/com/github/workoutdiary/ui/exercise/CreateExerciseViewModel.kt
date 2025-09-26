package com.github.workoutdiary.ui.exercise

import androidx.lifecycle.ViewModel
import com.github.workoutdiary.data.entities.WorkoutSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateExerciseViewModel : ViewModel() {
    private val _exerciseName = MutableStateFlow("")
    val exerciseName: StateFlow<String> = _exerciseName.asStateFlow()

    private val _muscleGroup = MutableStateFlow("")
    val muscleGroup: StateFlow<String> = _muscleGroup.asStateFlow()

    private val _sets = MutableStateFlow<List< WorkoutSet>>(emptyList())
    val sets: StateFlow<List<WorkoutSet>> = _sets.asStateFlow()





}