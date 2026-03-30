package com.example.recipexmlapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R

class MethodAdapter(private var methodSteps: List<String>) : RecyclerView.Adapter<MethodAdapter.MethodViewHolder>() {

    fun updateMethod(newMethodSteps: List<String>) {
        val oldSize = methodSteps.size
        methodSteps = newMethodSteps
        val newSize = methodSteps.size
        
        when {
            oldSize == 0 -> notifyItemRangeInserted(0, newSize)
            newSize == 0 -> notifyItemRangeRemoved(0, oldSize)
            else -> {
                if (oldSize < newSize) {
                    notifyItemRangeChanged(0, oldSize)
                    notifyItemRangeInserted(oldSize, newSize - oldSize)
                } else {
                    notifyItemRangeChanged(0, newSize)
                    notifyItemRangeRemoved(newSize, oldSize - newSize)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_method_step, parent, false)
        return MethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        holder.bind(methodSteps[position], position + 1)
    }

    override fun getItemCount(): Int = methodSteps.size

    class MethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStepNumber: TextView = itemView.findViewById(R.id.tvStepNumber)
        private val tvStepText: TextView = itemView.findViewById(R.id.tvStepText)

        fun bind(stepText: String, stepNumber: Int) {
            tvStepNumber.text = itemView.context.getString(R.string.step_number_format, stepNumber)
            tvStepText.text = stepText
        }
    }
}
