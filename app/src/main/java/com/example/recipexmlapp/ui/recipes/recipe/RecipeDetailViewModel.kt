package com.example.recipexmlapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import android.content.Context
import androidx.core.content.edit
import com.example.recipexmlapp.data.Recipe
import com.example.recipexmlapp.model.STUB

data class RecipeDetailState(
    val recipe: Recipe? = null,
    val portionsCount: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipeDetailViewModel : ViewModel() {
    
    private val _state = MutableLiveData<RecipeDetailState>()
    val state: LiveData<RecipeDetailState> = _state
    
    private val PREFS_NAME = "recipe_favorites"
    private val FAVORITES_KEY = "favorites_set"
    
    private var sharedPrefs: android.content.SharedPreferences? = null
    
    fun setContext(context: Context) {
        sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    init {
        Log.d("RecipeDetailVM", "ViewModel initialized")
        _state.value = RecipeDetailState()
    }
    
    fun loadRecipe(id: Int) {
        // TODO: load from network
        val recipe = STUB.getRecipeById(id)
        val favorites = getFavorites()
        val isFavorite = favorites.contains(id.toString())
        val currentPortionsCount = _state.value?.portionsCount ?: 1
        
        _state.value = _state.value?.copy(
            recipe = recipe,
            isFavorite = isFavorite,
            portionsCount = currentPortionsCount
        )
    }
    
    private fun getFavorites(): MutableSet<String> {
        val savedFavorites: Set<String>? = sharedPrefs?.getStringSet(FAVORITES_KEY, emptySet())
        return HashSet(savedFavorites ?: emptySet())
    }
    
    private fun saveFavorites(favorites: Set<String>) {
        sharedPrefs?.edit {
            putStringSet(FAVORITES_KEY, favorites)
        }
    }
    
    fun onFavoritesClicked() {
        val currentState = _state.value ?: return
        val recipe = currentState.recipe ?: return
        
        val favorites = getFavorites()
        val recipeIdString = recipe.id.toString()
        val newIsFavorite = !currentState.isFavorite
        
        if (newIsFavorite) {
            favorites.add(recipeIdString)
        } else {
            favorites.remove(recipeIdString)
        }
        
        saveFavorites(favorites)
        
        _state.value = currentState.copy(isFavorite = newIsFavorite)
    }
    
    fun updatePortions(portions: Int) {
        _state.value = _state.value?.copy(portionsCount = portions)
    }
    
    fun setLoading(isLoading: Boolean) {
        _state.value = _state.value?.copy(isLoading = isLoading)
    }
    
    fun setError(error: String?) {
        _state.value = _state.value?.copy(error = error)
    }
}
