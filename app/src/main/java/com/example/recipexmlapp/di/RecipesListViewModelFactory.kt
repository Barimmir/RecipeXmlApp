package com.example.recipexmlapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipexmlapp.ui.recipes.recipelist.RecipesListViewModel
import com.example.recipexmlapp.data.RecipesRepository
import android.app.Application

class RecipesListViewModelFactory(
    private val repository: RecipesRepository,
    private val application: Application
) : ViewModelProvider.Factory, Factory<RecipesListViewModel> {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipesListViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(repository, application)
    }
}
