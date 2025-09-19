package com.github.workoutdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.workoutdiary.repository.WorkoutRepository


/**
 * Фабрика для создания HomeViewModel с зависимостью от WorkoutRepository.
 */
class HomeViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}