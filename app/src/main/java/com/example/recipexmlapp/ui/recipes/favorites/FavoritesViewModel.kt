package com.example.recipexmlapp.ui.recipes.favorites

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.model.STUB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()
    
    private lateinit var sharedPrefs: android.content.SharedPreferences
    
    fun initialize(context: Context) {
        sharedPrefs = context.getSharedPreferences("recipe_favorites", Context.MODE_PRIVATE)
        loadFavoriteRecipes()
    }
    
    private fun getFavorites(): Set<String> {
        val savedFavorites: Set<String>? = sharedPrefs.getStringSet("favorites_set", emptySet())
        return savedFavorites ?: emptySet()
    }
    
    private fun saveFavorites(favoriteIds: Set<String>) {
        sharedPrefs.edit()
            .putStringSet("favorites_set", favoriteIds)
            .apply()
    }
    
    fun loadFavoriteRecipes() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val favoriteIds = getFavorites()
                val favoriteIdsInt = favoriteIds.mapNotNull { it.toIntOrNull() }.toSet()
                val favoriteRecipes = STUB.getRecipesByIds(favoriteIdsInt)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    favoriteRecipes = favoriteRecipes,
                    isEmpty = favoriteRecipes.isEmpty()
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
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
}
