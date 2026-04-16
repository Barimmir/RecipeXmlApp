package com.example.recipexmlapp.ui.recipes.recipelist

import androidx.lifecycle.ViewModel
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.ViewModelProvider

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(RecipesListState())
    val state: StateFlow<RecipesListState> = _state.asStateFlow()
    
    private val recipesRepository = RecipesRepository()

    fun initialize(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        println("RecipesListViewModel: Initializing with categoryId=$categoryId, categoryName=$categoryName")
        _state.value = _state.value.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImageUrl = categoryImageUrl
        )
        loadRecipes()
    }

    fun loadRecipes() {
        println("RecipesListViewModel: Starting to load recipes")
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        val categoryId = _state.value.categoryId
        println("RecipesListViewModel: categoryId = $categoryId")
        if (categoryId != null) {
            println("RecipesListViewModel: Calling repository for category $categoryId")
            recipesRepository.getRecipesByCategory(categoryId) { recipes ->
                println("RecipesListViewModel: Callback received with recipes: ${recipes?.size ?: 0}")
                if (recipes != null) {
                    println("RecipesListViewModel: Updating state with ${recipes.size} recipes")
                    recipes.forEach { recipe ->
                        println("RecipesListViewModel: Recipe - ${recipe.id}: ${recipe.title}")
                    }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        recipes = recipes
                    )
                    println("RecipesListViewModel: State updated successfully")
                } else {
                    println("RecipesListViewModel: API failed, setting error state")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Erreur obtaining data"
                    )
                    Toast.makeText(getApplication(), "Erreur obtaining data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            println("RecipesListViewModel: categoryId is null, setting empty recipes")
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
        recipesRepository.shutdown()
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
