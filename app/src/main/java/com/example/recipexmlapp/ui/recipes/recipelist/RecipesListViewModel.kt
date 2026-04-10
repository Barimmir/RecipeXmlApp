package com.example.recipexmlapp.ui.recipes.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.model.STUB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipesListViewModel : ViewModel() {

    private val _state = MutableStateFlow(RecipesListState())
    val state: StateFlow<RecipesListState> = _state.asStateFlow()

    fun initialize(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        _state.value = _state.value.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImageUrl = categoryImageUrl
        )
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val categoryId = _state.value.categoryId
                val recipes = categoryId?.let { 
                    STUB.getRecipesByCategoryId(it) 
                } ?: emptyList()
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    recipes = recipes
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun refreshRecipes() {
        loadRecipes()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
