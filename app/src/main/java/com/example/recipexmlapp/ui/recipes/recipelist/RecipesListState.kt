package com.example.recipexmlapp.ui.recipes.recipelist

import com.example.recipexmlapp.data.Recipe

data class RecipesListState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val categoryImageUrl: String? = null,
    val error: String? = null
)
