package com.github.workoutdiary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.github.workoutdiary.data.DatabaseInitializer
import com.github.workoutdiary.databinding.ActivityMainBinding

import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Только инициализация базы данных, UI полностью во фрагментах
        initializeDatabase()
    }

    private fun initializeDatabase() {
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "Инициализация базы данных...")
                DatabaseInitializer.initialize(applicationContext)
                Log.d("MainActivity", "База данных успешно инициализирована")
            } catch (e: Exception) {
                Log.e("MainActivity", "Ошибка инициализации базы данных", e)
            }
        }
    }
}