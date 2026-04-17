package com.example.recipexmlapp.ui.recipes.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.ViewModelProvider

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(RecipesListState())
    val state: StateFlow<RecipesListState> = _state.asStateFlow()
    
    private val recipesRepository = RecipesRepository

    fun initialize(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        _state.value = _state.value.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImageUrl = categoryImageUrl
        )
        loadRecipes()
    }

    fun loadRecipes() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        val categoryId = _state.value.categoryId
        if (categoryId != null) {
            viewModelScope.launch {
                val recipes = recipesRepository.getRecipesByCategory(categoryId)
                if (recipes != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        recipes = recipes
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Ошибка получения данных"
                    )
                }
            }
        } else {
            _state.value = _state.value.copy(
                isLoading = false,
                recipes = emptyList()
            )
        }
    }

    fun refreshRecipes() {
        loadRecipes()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    override fun onCleared() {
        super.onCleared()
    }
}

class RecipesListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipesListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
