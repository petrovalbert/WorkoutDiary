package com.github.workoutdiary.ui.exercise

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.workoutdiary.Constants
import com.github.workoutdiary.R
import com.github.workoutdiary.data.entities.WorkoutSet
import android.text.TextWatcher
import android.widget.ImageButton
import android.widget.PopupMenu

class SetsAdapter(
    private var sets: List<WorkoutSet>,
    private val onSetChanged: (Int, String, String) -> Unit,
    private val onSetDeleted: (Int) -> Unit
) : RecyclerView.Adapter<SetsAdapter.SetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_set, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetsAdapter.SetViewHolder, position: Int) {
        holder.bind(sets[position], position)
    }

    override fun getItemCount(): Int = sets.size

    fun updateSets(newSets: MutableList<WorkoutSet>) {
        sets = newSets
        notifyDataSetChanged()
    }

    inner class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val setNumber: TextView = itemView.findViewById(R.id.set_number)
        private val weightEditText: EditText = itemView.findViewById(R.id.weight_edit_text)
        private val repsEditText: EditText = itemView.findViewById(R.id.reps_edit_text)

        private val menuButton: ImageButton = itemView.findViewById(R.id.delete_sets_btn_menu)

        private var currentPosition = -1
        private var weightWatcher: TextWatcher? = null
        private var repsWatcher: TextWatcher? = null

        fun bind(set: WorkoutSet, position: Int) {
            currentPosition = position
            setNumber.text = set.setNumber.toString()

            weightWatcher?.let { weightEditText.removeTextChangedListener(it) }
            repsWatcher?.let { repsEditText.removeTextChangedListener(it) }

            weightWatcher = null
            repsWatcher = null

            weightEditText.setText(if (set.weight > 0) set.weight.toString() else "")
            repsEditText.setText(if (set.reps > 0) set.reps.toString() else "")


            weightWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (currentPosition != -1 && currentPosition < sets.size) {
                        onSetChanged(currentPosition, Constants.SetFields.WEIGHT, s.toString())
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            repsWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (currentPosition != -1 && currentPosition < sets.size) {
                        onSetChanged(currentPosition, Constants.SetFields.REPS, s.toString())
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            weightWatcher?.let { weightEditText.addTextChangedListener(it) }
            repsWatcher?.let { repsEditText.addTextChangedListener(it) }

            // Обработка клика на кнопку удаления
            menuButton.setOnClickListener { view ->
                showPopupMenu(view, position)
            }
        }

        private fun showPopupMenu(view: View, position: Int) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.set_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        onSetDeleted(position)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

    }
}