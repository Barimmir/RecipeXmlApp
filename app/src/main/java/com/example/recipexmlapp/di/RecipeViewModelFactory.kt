package com.example.recipexmlapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipexmlapp.ui.recipes.recipe.RecipeDetailViewModel
import com.example.recipexmlapp.data.RecipesRepository
import android.app.Application

class RecipeViewModelFactory(
    private val repository: RecipesRepository,
    private val application: Application
) : ViewModelProvider.Factory, Factory<RecipeDetailViewModel> {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    
    override fun create(): RecipeDetailViewModel {
        return RecipeDetailViewModel(repository, application)
    }
}
