package com.github.workoutdiary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.workoutdiary.databinding.FragmentHomeBinding
import com.github.workoutdiary.ui.WorkoutViewModel
import com.github.workoutdiary.ui.home.WorkoutAdapter

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var vewModel: WorkoutViewModel
    private lateinit var adapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = WorkoutAdapter { workout ->
            // Обработка клика по тренировке
            showWorkoutDetails(workout)
        }
        binding.workoutRecyclerView.adapter = adapter
        binding.workoutRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            // Открыть экран создания тренировки
            navigateToFragment(CreateWorkoutFragment())
        }
    }

    private fun observeViewModel() {
        viewModel.workoutsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WorkoutListState.Loading -> showLoading()
                is WorkoutListState.Success -> showWorkouts(state.workouts)
                is WorkoutListState.Error -> showError(state.messageResId, state.formatArgs)
            }
        }
    }

    private fun showWorkouts(workouts: List<WorkoutWithExercises>) {
        adapter.submitList(workouts)
        // Скрыть loading indicator
    }

}