package com.example.recipexmlapp.ui.recipes.favorites

import com.example.recipexmlapp.data.Recipe

data class FavoritesState(
    val isLoading: Boolean = false,
    val favoriteRecipes: List<Recipe> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String? = null
)
