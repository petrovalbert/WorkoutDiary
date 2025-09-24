package com.github.workoutdiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.workoutdiary.R
import com.github.workoutdiary.data.entities.WorkoutExercise

class ExerciseAdapter : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    private var exercises = emptyList<WorkoutExercise>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseAdapter.ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun setExercises(newExercises: List<WorkoutExercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName: TextView = itemView.findViewById(R.id.exercise_name)

        fun bind(exercise: WorkoutExercise) {
            exerciseName.text = exercise.exerciseName
        }
    }
}