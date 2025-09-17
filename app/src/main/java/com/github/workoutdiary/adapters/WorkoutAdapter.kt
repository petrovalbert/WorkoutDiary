package com.github.workoutdiary.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.workoutdiary.databinding.ItemWorkoutBinding
import com.github.workoutdiary.models.WorkoutWithExercises

class WorkoutAdapter(
    private val onItemClick: (WorkoutWithExercises) -> Unit
) : ListAdapter<WorkoutWithExercises, WorkoutAdapter.ViewHolder>(WorkoutDiffCallback()) {

    inner class ViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutWithExercises) {
            binding.workoutTitle.text = workout.workout.name
            // TODO: настроить RecyclerView для упражнений внутри
            binding.root.setOnClickListener { onItemClick(workout) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WorkoutDiffCallback : DiffUtil.ItemCallback<WorkoutWithExercises>() {
    override fun areItemsTheSame(oldItem: WorkoutWithExercises, newItem: WorkoutWithExercises): Boolean {
        return oldItem.workout.id == newItem.workout.id
    }

    override fun areContentsTheSame(oldItem: WorkoutWithExercises, newItem: WorkoutWithExercises): Boolean {
        return oldItem == newItem
    }
}
