package com.example.recipexmlapp.ui.recipes.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.data.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    fun initialize() {
        loadFavoriteRecipes()
    }

    fun loadFavoriteRecipes() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val recipes = repository.getFavoriteRecipesFromCache()
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
}
