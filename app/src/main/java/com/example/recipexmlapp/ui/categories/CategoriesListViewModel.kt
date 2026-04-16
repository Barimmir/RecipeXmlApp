package com.example.recipexmlapp.ui.categories

import androidx.lifecycle.ViewModel
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.ViewModelProvider

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(CategoriesListState())
    val state: StateFlow<CategoriesListState> = _state.asStateFlow()
    
    private val recipesRepository = RecipesRepository()

    fun initialize() {
        println("CategoriesListViewModel: initialize called")
        loadCategories()
    }

    fun loadCategories() {
        try {
            println("CategoriesListViewModel: Starting to load categories")
            println("CategoriesListViewModel: Current thread: ${Thread.currentThread().name}")
            
            if (Thread.currentThread().name == "main") {
                println("CategoriesListViewModel: WARNING - Called on main thread!")
            }
            
            _state.value = _state.value.copy(isLoading = true, error = null)
            println("CategoriesListViewModel: State set to loading, calling repository")
            
            recipesRepository.getCategories { categories ->
                try {
                    println("CategoriesListViewModel: *** CALLBACK EXECUTED ***")
                    println("CategoriesListViewModel: Callback received with categories: ${categories?.size ?: 0}")
                    println("CategoriesListViewModel: Current thread: ${Thread.currentThread().name}")
                    
                    if (categories != null) {
                        println("CategoriesListViewModel: Updating state with ${categories.size} categories")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            categories = categories
                        )
                        println("CategoriesListViewModel: State updated successfully")
                    } else {
                        println("CategoriesListViewModel: API failed, setting error state")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Ошибка получения данных"
                        )
                    }
                } catch (e: Exception) {
                    println("CategoriesListViewModel: Exception in callback: ${e.message}")
                    e.printStackTrace()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Внутренняя ошибка: ${e.message}"
                    )
                }
            }
            println("CategoriesListViewModel: Repository call submitted, waiting for callback...")
        } catch (e: Exception) {
            println("CategoriesListViewModel: Exception in loadCategories: ${e.message}")
            e.printStackTrace()
            _state.value = _state.value.copy(
                isLoading = false,
                error = "Ошибка загрузки: ${e.message}"
            )
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
        recipesRepository.shutdown()
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
