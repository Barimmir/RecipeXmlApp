package com.example.recipexmlapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.example.recipexmlapp.data.Recipe

data class RecipeDetailState(
    val recipe: Recipe? = null,
    val portions: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipeDetailViewModel : ViewModel() {
    
    private val _state = MutableLiveData<RecipeDetailState>()
    val state: LiveData<RecipeDetailState> = _state
    
    init {
        Log.d("RecipeDetailVM", "ViewModel initialized")
        _state.value = RecipeDetailState(isFavorite = true)
    }
    
    var currentState = RecipeDetailState()
        private set
    
    fun setRecipe(recipe: Recipe) {
        currentState = currentState.copy(recipe = recipe)
        _state.value = currentState
    }
    
    fun updatePortions(portions: Int) {
        currentState = currentState.copy(portions = portions)
        _state.value = currentState
    }
    
    fun toggleFavorite() {
        currentState = currentState.copy(isFavorite = !currentState.isFavorite)
        _state.value = currentState
    }
    
    fun setLoading(isLoading: Boolean) {
        currentState = currentState.copy(isLoading = isLoading)
        _state.value = currentState
    }
    
    fun setError(error: String?) {
        currentState = currentState.copy(error = error)
        _state.value = currentState
    }
}
