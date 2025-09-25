package com.github.workoutdiary.ui.workout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.workoutdiary.R
import com.github.workoutdiary.databinding.FragmentAddWorkoutBinding

/**
 * Заглушка для фрагмента создания тренировки.
 * Будет реализована позже.
 */
class CreateWorkoutFragment : Fragment(R.layout.fragment_add_workout) {

    private var _binding: FragmentAddWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddWorkoutBinding.bind(view)
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.addExercise.setOnClickListener {
            findNavController().navigate(R.id.action_createWorkoutFragment_to_createExerciseFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}