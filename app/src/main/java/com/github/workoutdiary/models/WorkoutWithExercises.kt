package com.github.workoutdiary.models

import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.data.entities.WorkoutExercise

data class WorkoutWithExercises(
    val workout: WorkoutEntry,
    val exercises: List<WorkoutExercise>
)