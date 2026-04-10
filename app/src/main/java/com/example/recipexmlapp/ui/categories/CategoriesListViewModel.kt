package com.example.recipexmlapp.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipexmlapp.data.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesListViewModel : ViewModel() {

    private val _state = MutableStateFlow(CategoriesListState())
    val state: StateFlow<CategoriesListState> = _state.asStateFlow()

    fun initialize() {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val categories = CategoryRepository.getCategories()
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    categories = categories
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
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
}
