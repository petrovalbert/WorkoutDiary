package com.github.workoutdiary.ui.exercise

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.workoutdiary.Constants
import com.github.workoutdiary.R
import com.github.workoutdiary.data.entities.WorkoutSet
import com.github.workoutdiary.databinding.FragmentAddExerciseBinding
import android.text.TextWatcher

class CreateExerciseFragment : Fragment(R.layout.fragment_add_exercise) {
    private var _binding: FragmentAddExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var setsAdapter: SetsAdapter
    private val setsList = mutableListOf<WorkoutSet>()

    private var exerciseName = ""
    private var muscleGroup = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddExerciseBinding.bind(view)
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }


        setupRecyclerView()
        setupTextListeners()

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
            layoutManager = LinearLayoutManager(this@CreateExerciseFragment)
            adapter = setsAdapter
        }

        // Добавляем первую пустую строку для ввода
        addEmptySet()
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

    private fun addEmptySet() {
        setsList.add(WorkoutSet(setsList.size + 1, 0.0, 0))
        setsAdapter.notifyItemInserted(setsList.size - 1)
    }

    private fun validateData(): Boolean {
        return exerciseName.isNotBlank()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}