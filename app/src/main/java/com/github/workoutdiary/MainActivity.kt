package com.github.workoutdiary

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.workoutdiary.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFab()
        observeWorkouts()
    }

    private fun setupRecyclerView() {
        adapter = WorkoutAdapter(
            onWorkoutClick = { workout ->
                viewModel.toggleWorkoutExpanded(workout.date)
            },
            onExerciseClick = { exercise ->
                val message =getString(R.string.exercise_hint, exercise.exerciseName)
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        )

        binding.workoutRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            // Заменяем вызов addTestWorkout на открытие нового Activity
            val intent = Intent(this, AddExerciseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeWorkouts() {
        viewModel.workouts.observe(this) { workouts ->
            adapter.submitList(workouts)
        }
    }
}