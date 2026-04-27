package com.example.recipexmlapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import android.app.Application
import kotlinx.coroutines.launch
import com.example.recipexmlapp.data.Recipe
import com.example.recipexmlapp.data.RecipesRepository
import com.example.recipexmlapp.data.ApiConstants

data class RecipeDetailState(
    val recipe: Recipe? = null,
    val portionsCount: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val recipeImageUrl: String? = null
)

class RecipeDetailViewModel(
    private val repository: RecipesRepository,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeDetailState())
    val state: StateFlow<RecipeDetailState> = _state.asStateFlow()

    init {
        _state.value = RecipeDetailState()
    }

    fun loadRecipe(id: Int) {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            val recipe = repository.getRecipeById(id)
            val currentPortionsCount = _state.value.portionsCount

            if (recipe != null) {
                val recipeImageUrl = "${ApiConstants.IMAGE_BASE_URL}${recipe.imageUrl}"
                val favoriteRecipes = repository.getFavoriteRecipesFromCache()
                val isFavorite = favoriteRecipes?.any { it.id == id } ?: false

                _state.value = _state.value.copy(
                    recipe = recipe,
                    isFavorite = isFavorite,
                    portionsCount = currentPortionsCount,
                    isLoading = false,
                    recipeImageUrl = recipeImageUrl
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Ошибка получения данных"
                )
            }
        }
    }

    fun onFavoritesClicked() {
        val currentState = _state.value
        val recipe = currentState.recipe ?: return

        viewModelScope.launch {
            val newIsFavorite = !currentState.isFavorite

            if (newIsFavorite) {
                repository.addToFavorites(recipe.id)
            } else {
                repository.removeFromFavorites(recipe.id)
            }

            _state.value = currentState.copy(isFavorite = newIsFavorite)
        }
    }

    fun updatePortions(portions: Int) {
        _state.value = _state.value.copy(portionsCount = portions)
    }
}

