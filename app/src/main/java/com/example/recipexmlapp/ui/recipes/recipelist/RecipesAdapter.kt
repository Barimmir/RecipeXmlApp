package com.example.recipexmlapp.ui.recipes.recipelist

import com.example.recipexmlapp.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recipexmlapp.databinding.ItemRecipeCardBinding
import com.example.recipexmlapp.data.Recipe
import com.example.recipexmlapp.data.ApiConstants

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

            val imageUrl = "${ApiConstants.IMAGE_BASE_URL}${recipe.imageUrl}"

            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivRecipeImage)

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
