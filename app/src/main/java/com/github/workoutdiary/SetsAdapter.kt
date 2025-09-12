package com.github.workoutdiary

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SetsAdapter(
    private val sets: List<WorkoutSet>,
    private val onSetChanged: (Int, String, String) -> Unit
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

    inner class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val setNumber: TextView = itemView.findViewById(R.id.set_number)
        private val weightEditText: EditText = itemView.findViewById(R.id.weight_edit_text)
        private val repsEditText: EditText = itemView.findViewById(R.id.reps_edit_text)

        fun bind(set: WorkoutSet, position: Int) {
            setNumber.text = set.setNumber.toString()

            weightEditText.setText(if (set.weight > 0) set.weight.toString() else "")
            repsEditText.setText(if (set.reps > 0) set.reps.toString() else "")

            weightEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onSetChanged(position, Constants.SetFields.WEIGHT, s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            repsEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onSetChanged(position, Constants.SetFields.REPS, s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

        }

    }
}