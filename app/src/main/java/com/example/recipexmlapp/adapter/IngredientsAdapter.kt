package com.example.recipexmlapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.data.Ingredient
import java.util.Locale

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    var dataSet: List<Ingredient> = emptyList()

    private var quantity = 1

    fun updateIngredients(newIngredients: List<Ingredient>) {
        val oldSize = dataSet.size
        dataSet = newIngredients
        val newSize = dataSet.size
        
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

    fun updatePortions(progress: Int) {
        quantity = progress
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(dataSet[position], quantity)
    }

    override fun getItemCount(): Int = dataSet.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIngredientName: TextView = itemView.findViewById(R.id.tvIngredientName)
        private val tvIngredientQuantity: TextView = itemView.findViewById(R.id.tvIngredientQuantity)

        fun bind(ingredient: Ingredient, quantity: Int) {
            val originalQuantity = ingredient.quantity.toDoubleOrNull() ?: 0.0
            val multipliedQuantity = originalQuantity * quantity
            val formattedQuantity = if (multipliedQuantity == multipliedQuantity.toInt().toDouble()) {
                multipliedQuantity.toInt().toString()
            } else {
                String.format(Locale.getDefault(), "%.1f", multipliedQuantity)
            }
            tvIngredientName.text = ingredient.description.replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase() else it.toString() 
            }
            tvIngredientQuantity.text = itemView.context.getString(R.string.ingredient_quantity_format, formattedQuantity, ingredient.unitOfMeasure).trim()
        }
    }
}
