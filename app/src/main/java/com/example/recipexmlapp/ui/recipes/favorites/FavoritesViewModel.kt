package com.example.recipexmlapp.ui.recipes.favorites

import com.example.recipexmlapp.model.Recipe
import androidx.lifecycle.ViewModel

data class FavoritesState(
    val favoriteRecipes: List<Recipe> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false
)

class FavoritesViewModel : ViewModel() {
    
    var state = FavoritesState()
        private set
    
    fun updateFavoriteRecipes(recipes: List<Recipe>) {
        state = state.copy(
            favoriteRecipes = recipes,
            isEmpty = recipes.isEmpty()
        )
    }
    
    fun updateFavoriteIds(favoriteIds: Set<String>) {
        state = state.copy(favoriteIds = favoriteIds)
    }
    
    fun toggleFavorite(recipeId: Int) {
        val recipeIdString = recipeId.toString()
        val newFavoriteIds = if (state.favoriteIds.contains(recipeIdString)) {
            state.favoriteIds - recipeIdString
        } else {
            state.favoriteIds + recipeIdString
        }
        state = state.copy(favoriteIds = newFavoriteIds)
    }
    
    fun setLoading(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading)
    }
    
    fun setError(error: String?) {
        state = state.copy(error = error)
    }
}
