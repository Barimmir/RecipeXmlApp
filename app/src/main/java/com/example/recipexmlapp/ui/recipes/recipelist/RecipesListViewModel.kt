package com.example.recipexmlapp.ui.recipes.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.app.Application

class RecipesListViewModel(
    private val repository: RecipesRepository,
    private val application: Application
) : ViewModel() {

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
        _state.value = _state.value.copy(isLoading = true, error = null)

        val categoryId = _state.value.categoryId
        if (categoryId != null) {
            viewModelScope.launch {
                val cachedRecipes = repository.getRecipesByCategoryFromCache(categoryId)
                if (cachedRecipes != null && cachedRecipes.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        recipes = cachedRecipes
                    )
                }
                val recipes = repository.getRecipesByCategory(categoryId)
                if (recipes != null) {
                    val recipesWithCategoryId = recipes.map { it.copy(categoryId = categoryId) }
                    repository.saveRecipesToCache(recipesWithCategoryId)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        recipes = recipesWithCategoryId
                    )
                } else if (cachedRecipes.isNullOrEmpty()) {
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
}

