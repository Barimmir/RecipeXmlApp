package com.example.recipexmlapp.ui.recipes.recipelist

import androidx.lifecycle.ViewModel
import com.example.recipexmlapp.model.Recipe

data class RecipeListState(
    val recipes: List<Recipe> = emptyList(),
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val categoryImageUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipeListViewModel : ViewModel() {
    
    var state = RecipeListState()
        private set
    
    fun setCategoryInfo(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        state = state.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImageUrl = categoryImageUrl
        )
    }
    
    fun updateRecipes(recipes: List<Recipe>) {
        state = state.copy(recipes = recipes)
    }
    
    fun setLoading(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading)
    }
    
    fun setError(error: String?) {
        state = state.copy(error = error)
    }
}
