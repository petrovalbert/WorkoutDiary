package com.github.workoutdiary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels
import com.github.workoutdiary.adapters.WorkoutAdapter
import com.github.workoutdiary.data.AppDatabase
import com.github.workoutdiary.data.DatabaseInitializer
import com.github.workoutdiary.databinding.ActivityMainBinding
import com.github.workoutdiary.repository.WorkoutRepository
import com.github.workoutdiary.ui.home.HomeViewModel
import com.github.workoutdiary.ui.home.HomeViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var workoutAdapter: WorkoutAdapter

    // Правильное объявление ViewModel с делегатом
    private val viewModel: HomeViewModel by viewModels {
        // Получаем экземпляр базы данных
        val database = AppDatabase.getInstance(applicationContext)

        // Создаем фабрику с ВСЕМИ необходимыми параметрами
        HomeViewModelFactory(
            workoutRepository = WorkoutRepository(database.workoutDao()),
            exerciseDao = database.exerciseDao(),  // Добавляем exerciseDao
            setDao = database.setDao()             // Добавляем setDao
            // Добавьте другие параметры, если они требуются вашей фабрике
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        initializeDatabaseAndObserve()
        setupFab()

        // Наблюдение за LiveData из ViewModel
        viewModel.allWorkouts.observe(this) { workouts ->
            workoutAdapter.updateWorkouts(workouts)
            // Дополнительная логика обновления UI
        }
    }

    private fun initializeDatabaseAndObserve() {
        lifecycleScope.launch {
            try {
                DatabaseInitializer.initialize(applicationContext)
                // После инициализации загружаем данные
                viewModel.loadAllWorkouts()
            } catch (e: Exception) {
                Log.e("MainActivity", "Ошибка инициализации", e)
            }
        }
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter(emptyList())
        binding.recyclerViewWorkouts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = workoutAdapter
        }
    }

    private fun setupFab() {
        binding.fabAddWorkout.setOnClickListener {
            Log.d("MainActivity", "FAB clicked")
        }
    }
}