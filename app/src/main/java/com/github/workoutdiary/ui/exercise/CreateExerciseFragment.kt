package com.github.workoutdiary.ui.exercise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.workoutdiary.R
import com.github.workoutdiary.databinding.FragmentAddExerciseBinding

class CreateExerciseFragment : Fragment(R.layout.fragment_add_exercise) {
    private var _binding: FragmentAddExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddExerciseBinding.bind(view)
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}