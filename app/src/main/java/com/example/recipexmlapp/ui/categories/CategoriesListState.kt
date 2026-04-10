package com.example.recipexmlapp.ui.categories

import com.example.recipexmlapp.data.Category

data class CategoriesListState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)
