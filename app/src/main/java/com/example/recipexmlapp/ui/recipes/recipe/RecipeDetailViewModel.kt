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
    
    fun setRecipe(recipe: Recipe) {
        _state.value = _state.value?.copy(recipe = recipe)
    }
    
    fun updatePortions(portions: Int) {
        _state.value = _state.value?.copy(portions = portions)
    }
    
    fun toggleFavorite() {
        _state.value = _state.value?.copy(isFavorite = !(_state.value?.isFavorite ?: false))
    }
    
    fun setLoading(isLoading: Boolean) {
        _state.value = _state.value?.copy(isLoading = isLoading)
    }
    
    fun setError(error: String?) {
        _state.value = _state.value?.copy(error = error)
    }
}
