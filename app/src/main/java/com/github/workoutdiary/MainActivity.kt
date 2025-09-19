package com.github.workoutdiary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.github.workoutdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Устанавливаем Toolbar как ActionBar
        setSupportActionBar(binding.toolbar)

        // Находим NavController по правильному ID
        val navController = findNavController(R.id.nav_host_fragment)

        // Настраиваем AppBar
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Обработчик клика по FAB - но FAB сейчас в fragment_home.xml, а не здесь!
        // binding.fab.setOnClickListener { onFabClicked() } // Уберите это, если FAB в фрагменте
    }

    /**
     * Обрабатывает нажатие на FAB - переходим к созданию тренировки
     * Этот метод теперь должен вызываться из HomeFragment!
     */
    private fun onFabClicked() {
        val navController = findNavController(R.id.nav_host_fragment)
        if (navController.currentDestination?.id == R.id.homeFragment) {
            navController.navigate(R.id.action_homeFragment_to_createWorkoutFragment)
        }
    }

    // Для правильной работы кнопки "Назад" в ActionBar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}