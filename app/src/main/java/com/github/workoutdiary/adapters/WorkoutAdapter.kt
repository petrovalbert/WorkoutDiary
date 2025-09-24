package com.github.workoutdiary.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.github.workoutdiary.R

import com.github.workoutdiary.data.entities.WorkoutEntry
import com.github.workoutdiary.data.entities.WorkoutExercise
import java.io.LineNumberReader

class WorkoutAdapter(private val onWorkoutClicked: (Long) -> Unit)
    : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private var workouts = emptyList<WorkoutEntry>()
    private var expandedWorkoutsIds = emptySet<Long>()
    private var loadingWorkoutIds = emptySet<Long>()

    fun updateData(
        newWorkouts: List<WorkoutEntry>,
        newExpandedIds: Set<Long>,
        newLoadingIds: Set<Long>
    ) {
        workouts = newWorkouts
        expandedWorkoutsIds = newExpandedIds
        loadingWorkoutIds = newLoadingIds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view, onWorkoutClicked)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        val isExpanded = expandedWorkoutsIds.contains(workout.id)
        val isLoading = loadingWorkoutIds.contains(workout.id)
        holder.bind(workout, isExpanded, isLoading)
    }
    override fun getItemCount(): Int {
        return workouts.size
    }

    class WorkoutViewHolder(
        itemView: View,
        private val onWorkoutClicked: (Long) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val workoutTitle: TextView = itemView.findViewById(R.id.workout_title)
        private val expandIcon: ImageView = itemView.findViewById(R.id.expand_icon)
        private val exercisesRecyclerView: RecyclerView = itemView.findViewById(R.id.exercisesRecyclerView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar) // Добавить если есть ProgressBar



        private val exerciseAdapter = ExerciseAdapter()

        init {
            exercisesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            exercisesRecyclerView.adapter = exerciseAdapter
        }

        fun bind(workout: WorkoutEntry, isExpanded: Boolean, isLoading: Boolean) {
            itemView.tag = workout.id

            workoutTitle.text = "Тренировка ${workout.date}"
            expandIcon.setImageResource(
                if (isExpanded) android.R.drawable.arrow_up_float
                else android.R.drawable.arrow_down_float
            )

            exercisesRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE

            progressBar.visibility = if (isLoading && isExpanded) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                onWorkoutClicked(workout.id)
            }

            if (!isExpanded) {
                exerciseAdapter.setExercises(emptyList())
            }

        }
        fun setExercises(exercises: List<WorkoutExercise>) {
            exerciseAdapter.setExercises(exercises)
            // Убедимся, что RecyclerView видим
            progressBar.visibility = View.GONE
        }
    }
}


