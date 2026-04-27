package com.example.recipexmlapp.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val repository: RecipesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesListState())
    val state: StateFlow<CategoriesListState> = _state.asStateFlow()

    fun initialize() {
        loadCategories()
    }

    fun loadCategories() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val cachedCategories = repository.getCategoriesFromCache()
            if (cachedCategories != null && cachedCategories.isNotEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    categories = cachedCategories
                )
            }
            val categories = repository.getCategories()
            if (categories != null) {
                repository.saveCategoriesToCache(categories)
                _state.value = _state.value.copy(
                    isLoading = false,
                    categories = categories
                )

            } else if (cachedCategories.isNullOrEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Ошибка получения данных"
                )
            }
        }
    }
}

