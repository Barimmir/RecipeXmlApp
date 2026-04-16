package com.example.recipexmlapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()
    
    private val sharedPrefs = application.getSharedPreferences("recipe_favorites", Application.MODE_PRIVATE)
    private val recipesRepository = RecipesRepository
    
    fun initialize() {
        loadFavoriteRecipes()
    }
    
    private fun getFavorites(): Set<String> {
        val savedFavorites: Set<String>? = sharedPrefs.getStringSet("favorites_set", emptySet())
        return savedFavorites ?: emptySet()
    }
    
    private fun saveFavorites(favoriteIds: Set<String>) {
        sharedPrefs.edit {
            putStringSet("favorites_set", favoriteIds)
        }
    }
    
    fun loadFavoriteRecipes() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        val favoriteIds = getFavorites()
        val favoriteIdsInt = favoriteIds.mapNotNull { it.toIntOrNull() }.toSet()
        
        if (favoriteIdsInt.isNotEmpty()) {
            recipesRepository.getFavoriteRecipes(favoriteIdsInt) { recipes ->
                if (recipes != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        favoriteRecipes = recipes,
                        isEmpty = recipes.isEmpty()
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Ошибка получения данных",
                        isEmpty = true
                    )
                }
            }
        } else {
            _state.value = _state.value.copy(
                isLoading = false,
                favoriteRecipes = emptyList(),
                isEmpty = true
            )
        }
    }
    
    fun toggleFavorite(recipeId: Int) {
        val recipeIdString = recipeId.toString()
        val currentFavorites = getFavorites()
        val newFavoriteIds = if (currentFavorites.contains(recipeIdString)) {
            currentFavorites - recipeIdString
        } else {
            currentFavorites + recipeIdString
        }
        
        saveFavorites(newFavoriteIds)
        loadFavoriteRecipes()
    }
    
    fun refreshFavorites() {
        loadFavoriteRecipes()
    }
    
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    override fun onCleared() {
        super.onCleared()
    }
}
