package com.example.recipexmlapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()
    
    private val recipesRepository = RecipesRepository
    
    fun initialize() {
        loadFavoriteRecipes()
    }
    
    fun loadFavoriteRecipes() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            val recipes = recipesRepository.getFavoriteRecipesFromCache()
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
    }
    
    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            val currentRecipes = _state.value.favoriteRecipes
            val isCurrentlyFavorite = currentRecipes.any { it.id == recipeId && it.isFavorite }
            
            if (isCurrentlyFavorite) {
                recipesRepository.removeFromFavorites(recipeId)
            } else {
                recipesRepository.addToFavorites(recipeId)
            }
            loadFavoriteRecipes()
        }
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
