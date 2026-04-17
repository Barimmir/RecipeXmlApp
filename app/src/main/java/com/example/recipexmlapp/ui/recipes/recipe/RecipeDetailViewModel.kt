package com.example.recipexmlapp.ui.recipes.recipe

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import android.content.Context
import androidx.core.content.edit
import android.app.Application
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

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    companion object {
        private const val PREFS_NAME = "recipe_favorites"
        private const val FAVORITES_KEY = "favorites_set"
    }
    
    private val _state = MutableLiveData<RecipeDetailState>()
    val state: LiveData<RecipeDetailState> = _state
    
    private val sharedPrefs = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val recipesRepository = RecipesRepository
    
    init {
        Log.d("RecipeDetailVM", "ViewModel initialized")
        _state.value = RecipeDetailState()
    }
    
    fun loadRecipe(id: Int) {
        _state.value = _state.value?.copy(isLoading = true)
        
        recipesRepository.getRecipeById(id) { recipe ->
            val favorites = getFavorites()
            val isFavorite = favorites.contains(id.toString())
            val currentPortionsCount = _state.value?.portionsCount ?: 1
            
            if (recipe != null) {
                val recipeImageUrl = "${ApiConstants.IMAGE_BASE_URL}${recipe.imageUrl}"
                
                _state.value = _state.value?.copy(
                    recipe = recipe,
                    isFavorite = isFavorite,
                    portionsCount = currentPortionsCount,
                    isLoading = false,
                    recipeImageUrl = recipeImageUrl
                )
            } else {
                _state.value = _state.value?.copy(
                    isLoading = false,
                    error = "Ошибка получения данных"
                )
            }
        }
    }
    
    private fun getFavorites(): MutableSet<String> {
        val savedFavorites: Set<String>? = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet())
        return HashSet(savedFavorites ?: emptySet())
    }
    
    private fun saveFavorites(favorites: Set<String>) {
        sharedPrefs.edit {
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
    
    override fun onCleared() {
        super.onCleared()
    }
}

class RecipeDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
