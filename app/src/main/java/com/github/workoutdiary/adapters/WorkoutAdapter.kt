package com.github.workoutdiary.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.github.workoutdiary.data.entities.WorkoutEntry

class WorkoutAdapter : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private var workouts = emptyList<WorkoutEntry>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val currentWorkout = workouts[position]
        holder.bind(currentWorkout)
    }

    override fun getItemCount(): Int {
        return workouts.size
    }


    fun setWorkouts(newWorkouts: List<WorkoutEntry>) {
        this.workouts = newWorkouts

        notifyDataSetChanged()
    }


    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val workoutTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(workout: WorkoutEntry) {
            workoutTextView.text = workout.name
        }
    }
}


