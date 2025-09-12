package com.github.workoutdiary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.workoutdiary.data.entities.WorkoutEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutAdapter(
    private val onWorkoutClick: (Workout) -> Unit,
    private val onExerciseClick: (WorkoutEntry) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_WORKOUT = 0
        private const val TYPE_EXERCISE = 1
    }

    private var items: List<Any> = emptyList()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Workout -> TYPE_WORKOUT
            is WorkoutEntry -> TYPE_EXERCISE
            else -> throw IllegalArgumentException("Unknown type")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_WORKOUT -> WorkoutViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_workout, parent, false)
            )
            TYPE_EXERCISE -> ExerciseViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_exercise, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WorkoutViewHolder -> holder.bind(items[position] as Workout)
            is ExerciseViewHolder -> holder.bind(items[position] as WorkoutEntry)
        }
    }

    fun submitList(workouts: List<Workout>) {
        val newItems = mutableListOf<Any>()
        workouts.forEach { workout ->
            newItems.add(workout)
            if (workout.isExpanded) {
                newItems.addAll(workout.exercises)
            }
        }
        items = newItems
        notifyDataSetChanged()
    }

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.workout_title)
        private val date: TextView = itemView.findViewById(R.id.workout_date)
        private val expandIcon: ImageView = itemView.findViewById(R.id.expand_icon)

        fun bind(workout: Workout) {
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            title.text = "Тренировка (${workout.exercises.size} упражнений)"
            date.text = "${dateFormat.format(Date(workout.date))} в ${timeFormat.format(Date(workout.date))}"

            expandIcon.setImageResource(
                if (workout.isExpanded) android.R.drawable.arrow_up_float
                else android.R.drawable.arrow_down_float
            )

            itemView.setOnClickListener {
                onWorkoutClick(workout)
            }
        }
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.exercise_name)
        private val details: TextView = itemView.findViewById(R.id.exercise_details)

        fun bind(exercise: WorkoutEntry) {
            name.text = exercise.exerciseName
            details.text = "${exercise.targetMuscle} • ${exercise.weight}кг × ${exercise.reps}повт."

            itemView.setOnClickListener {
                onExerciseClick(exercise)
            }
        }
    }
}