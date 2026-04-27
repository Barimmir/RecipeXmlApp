package com.example.recipexmlapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipexmlapp.ui.categories.CategoriesListViewModel
import com.example.recipexmlapp.data.RecipesRepository

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : ViewModelProvider.Factory, Factory<CategoriesListViewModel> {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesListViewModel(recipesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}
