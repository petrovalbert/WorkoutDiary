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
        setupButtons()

    }

    private fun setupRecyclerView() {
        setsAdapter = SetsAdapter(
            setsList,
            { position, field, value ->
                if (position < setsList.size) {
                    when (field) {
                        Constants.SetFields.WEIGHT -> {
                            val updatedSet = setsList[position].copy(
                                weight = value.toDoubleOrNull() ?: 0.0
                            )
                            setsList[position] = updatedSet
                        }

                        Constants.SetFields.REPS -> {
                            val updatedSet = setsList[position].copy(
                                reps = value.toIntOrNull() ?: 0
                            )
                            setsList[position] = updatedSet
                        }
                    }
                }
            },
            { position ->
                deleteSet(position)
            }
        )

        binding.setsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CreateExerciseFragment.context)
            adapter = setsAdapter
        }

    }

    private fun setupButtons() {
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addSetButton.setOnClickListener {
            addEmptySet()
        }
    }

    private fun deleteSet(position: Int) {
        setsList.removeAt(position)
        // Перенумеровываем оставшиеся подходы
        setsList.forEachIndexed { index, workoutSet ->
            setsList[index] = workoutSet.copy(setNumber = index + 1)
        }
        setsAdapter.updateSets(setsList)
    }

    private fun addEmptySet() {
        val nextSetNumber = setsList.size + 1
        setsList.add(
            WorkoutSet(
                id = 0L,
                exerciseId = 0L,
                setNumber = nextSetNumber,
                weight = 0.0,
                reps = 0
            )
        )
        setsAdapter.updateSets(setsList)

        // Прокрутка к новому элементу
        binding.setsRecyclerView.post {
            binding.setsRecyclerView.scrollToPosition(setsList.size - 1)
        }
    }


    private fun setupTextListeners() {
        binding.exerciseNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                exerciseName = s.toString()
            }
        })

        binding.muscleGroupEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                muscleGroup = s.toString()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}