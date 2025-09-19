package com.github.workoutdiary.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
// Импорты ваших классов
import com.github.workoutdiary.adapters.WorkoutAdapter
import com.github.workoutdiary.data.AppDatabase
import com.github.workoutdiary.repository.WorkoutRepository


import com.github.workoutdiary.databinding.FragmentHomeBinding

import com.github.workoutdiary.R


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
        workoutAdapter = WorkoutAdapter()

        val recyclerView = binding.workoutRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = workoutAdapter

        binding.fab.setOnClickListener {
            // Обработка клика по FAB
            onFabClicked()
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
            homeViewModel.workouts.collect { workouts ->
                workouts?.let {
                    workoutAdapter.setWorkouts(it)
                }
            }
        }
    }

    private fun onFabClicked() {
        // Навигация из фрагментафрагмента
        findNavController().navigate(R.id.action_homeFragment_to_createWorkoutFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
}