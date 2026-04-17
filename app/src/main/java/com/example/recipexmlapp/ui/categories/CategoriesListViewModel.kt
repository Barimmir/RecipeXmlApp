package com.example.recipexmlapp.ui.categories

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

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(CategoriesListState())
    val state: StateFlow<CategoriesListState> = _state.asStateFlow()
    
    private val recipesRepository = RecipesRepository

    fun initialize() {
        loadCategories()
    }

    fun loadCategories() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            val categories = recipesRepository.getCategories()
            if (categories != null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    categories = categories
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Ошибка получения данных"
                )
            }
        }
    }

    fun refreshCategories() {
        loadCategories()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    override fun onCleared() {
        super.onCleared()
    }
}

class CategoriesListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
