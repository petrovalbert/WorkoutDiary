package com.github.workoutdiary.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
// Импорты ваших классов
import com.github.workoutdiary.adapters.WorkoutAdapter
import com.github.workoutdiary.data.AppDatabase
import com.github.workoutdiary.repository.WorkoutRepository


import com.github.workoutdiary.databinding.FragmentHomeBinding

import com.github.workoutdiary.R
import com.github.workoutdiary.data.entities.WorkoutExercise
import kotlinx.coroutines.flow.combine


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var workoutAdapter: WorkoutAdapter
    // ДОБАВЛЯЕМ: переменную для View Binding
    private var _binding: FragmentHomeBinding? = null
    // Это свойство только для чтения, которое разворачивает _binding
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        setupViewModel()
        setupObservers()
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter { workoutId ->
            onWorkoutClicked(workoutId)
        }

        val recyclerView = binding.workoutRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = workoutAdapter

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createWorkoutFragment)
        }
    }

    private fun setupViewModel() {
        val applicationScope = viewLifecycleOwner.lifecycleScope
        val database = AppDatabase.getInstance(requireContext())
        val workoutDao = database.workoutDao()
        val exerciseDao = database.exerciseDao()
        val setDao = database.setDao()

        val repository = WorkoutRepository(workoutDao, exerciseDao, setDao)
        val viewModelFactory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                homeViewModel.workouts,
                homeViewModel.expandedWorkouts,
                homeViewModel.loadingStates
            ) { workouts, expandedIds, loadingIds ->
                Triple(workouts, expandedIds, loadingIds)
            }.collect { (workouts, expandedIds, loadingIds) ->
                workoutAdapter.updateData(workouts, expandedIds, loadingIds)
                expandedIds.forEach { workoutId ->
                    if (!isExercisesLoadedForWorkout(workoutId)) {
                        loadExercisesForWorkout(workoutId)
                    } else {
                        // Если упражнения уже в кэше, просто обновляем отображение
                        updateExercisesDisplay(workoutId)
                    }
                }

                workouts.forEach { workout ->
                    if (!expandedIds.contains(workout.id)) {
                        setExercisesForWorkout(workout.id, emptyList())
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.error.collect { error ->
                error?.let {
                    // Показ ошибки пользователю (можно реализовать Snackbar)
                    Log.e("HomeFragment", "Ошибка: $it")
                }
            }
        }
    }

    private fun isExercisesLoadedForWorkout(workoutId: Long): Boolean {
        return try {
            homeViewModel.getExercisesForWorkout(workoutId).isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    private fun updateExercisesDisplay(workoutId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val exercises = homeViewModel.getExercisesForWorkout(workoutId)
                setExercisesForWorkout(workoutId, exercises)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Ошибка обновления упражнений для тренировки $workoutId", e)
            }
        }
    }

    private fun loadExercisesForWorkout(workoutId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val exercises = homeViewModel.getExercisesForWorkout(workoutId)
                setExercisesForWorkout(workoutId, exercises)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Ошибка загрузки упражнений для тренировки $workoutId", e)
            }
        }
    }

    private fun setExercisesForWorkout(workoutId: Long, exercises: List<WorkoutExercise>) {
        // Находим ViewHolder по ID тренировки
        for (i in 0 until binding.workoutRecyclerView.childCount) {
            val child = binding.workoutRecyclerView.getChildAt(i)
            val holder = binding.workoutRecyclerView.getChildViewHolder(child)
            if (holder is WorkoutAdapter.WorkoutViewHolder && child.tag == workoutId) {
                holder.setExercises(exercises)
                break
            }
        }
    }

    private fun onWorkoutClicked(workoutId: Long) {
        homeViewModel.toggleWorkoutExpansion(workoutId)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}