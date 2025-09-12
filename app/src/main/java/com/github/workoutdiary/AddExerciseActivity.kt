package com.github.workoutdiary

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.databinding.ActivityAddExerciseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExerciseBinding
    private lateinit var setsAdapter: SetsAdapter
    private val setsList = mutableListOf<WorkoutSet>()
    private var exerciseName = ""
    private var muscleGroup = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupTextListeners()
        setupButtons()
    }

    private fun setupRecyclerView() {
        setsAdapter = SetsAdapter(setsList) { position, field, value ->
            when (field) {
                Constants.SetFields.WEIGHT -> setsList[position].weight = value.toDoubleOrNull() ?: 0.0
                Constants.SetFields.REPS -> setsList[position].reps = value.toIntOrNull() ?: 0
            }
            saveData()

            // Автоматическая прокрутка к последнему элементу
            binding.setsRecyclerView.post {
                binding.setsRecyclerView.scrollToPosition(setsList.size - 1)
            }
        }

        binding.setsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AddExerciseActivity)
            adapter = setsAdapter
        }

        // Добавляем первую пустую строку для ввода
        addEmptySet()
    }

    private fun setupTextListeners() {
        binding.exerciseNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                exerciseName = s.toString()
                saveData()
            }
        })

        binding.muscleGroupEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                muscleGroup = s.toString()
                saveData()
            }
        })
    }

    private fun setupButtons() {
        binding.saveButton.setOnClickListener {
            if (validateData()) {
                saveToDatabase()
                finish()
            } else {
                Toast.makeText(this, "Please fill exercise name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun addEmptySet() {
        setsList.add(WorkoutSet(setsList.size + 1, 0.0, 0))
        setsAdapter.notifyItemInserted(setsList.size - 1)
    }

    private fun validateData(): Boolean {
        return exerciseName.isNotBlank()
    }

    private fun saveData() {
        // Проверяем, нужно ли добавить новую строку
        if (setsList.isNotEmpty()) {
            val lastSet = setsList.last()
            if (lastSet.weight > 0 || lastSet.reps > 0) {
                addEmptySet()
            }
        }
    }

    private fun saveToDatabase() {
        if (!validateData()) return

        CoroutineScope(Dispatchers.IO).launch {
            val database = (application as WorkoutApplication).database
            val workoutDao = database.workoutDao()

            val currentTime = System.currentTimeMillis()

            // Сохраняем каждый подход как отдельную запись
            setsList.filter { it.weight > 0 && it.reps > 0 }.forEach { set ->
                val entry = WorkoutEntry(
                    date = currentTime,
                    exerciseName = exerciseName,
                    targetMuscle = muscleGroup,
                    setNumber = set.setNumber,
                    weight = set.weight,
                    reps = set.reps
                )
                workoutDao.insert(entry)
            }
        }
    }
}

data class WorkoutSet(
    val setNumber: Int,
    var weight: Double,
    var reps: Int
)