package com.example.recipexmlapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipexmlapp.data.Recipe

data class RecipeDetailState(
    val recipe: Recipe? = null,
    val portions: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipeDetailViewModel : ViewModel() {
    
    var state = RecipeDetailState()
        private set
    
    fun setRecipe(recipe: Recipe) {
        state = state.copy(recipe = recipe)
    }
    
    fun updatePortions(portions: Int) {
        state = state.copy(portions = portions)
    }
    
    fun toggleFavorite() {
        state = state.copy(isFavorite = !state.isFavorite)
    }
    
    fun setLoading(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading)
    }
    
    fun setError(error: String?) {
        state = state.copy(error = error)
    }
}
