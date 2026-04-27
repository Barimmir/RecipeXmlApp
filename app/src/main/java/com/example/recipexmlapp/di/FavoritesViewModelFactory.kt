package com.example.recipexmlapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipexmlapp.ui.recipes.favorites.FavoritesViewModel
import com.example.recipexmlapp.data.RecipesRepository
import android.app.Application

class FavoritesViewModelFactory(
    private val repository: RecipesRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
