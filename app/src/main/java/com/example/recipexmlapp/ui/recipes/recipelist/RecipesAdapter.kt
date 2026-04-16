package com.example.recipexmlapp.ui.recipes.recipelist

import com.example.recipexmlapp.R
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.databinding.ItemRecipeCardBinding
import com.example.recipexmlapp.data.Recipe

class RecipesAdapter(
    private var recipes: List<Recipe>,
    private val onRecipeClick: (Int) -> Unit
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class RecipeViewHolder(
        private val binding: ItemRecipeCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvRecipeTitle.text = recipe.title

            binding.tvRecipeDescription.text = recipe.description

            try {
                val inputStream = binding.root.context.assets.open(recipe.imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivRecipeImage.setImageDrawable(drawable)
            } catch (e: Exception) {
                binding.ivRecipeImage.setImageResource(R.drawable.ic_placeholder_image)
            }

            binding.root.setOnClickListener {
                onRecipeClick(recipe.id)
            }
        }
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
