package com.example.recipexmlapp.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    
    @GET("category")
    suspend fun getCategories(): List<Category>
    
    @GET("category/{categoryId}/recipes")
    suspend fun getRecipesByCategory(@Path("categoryId") categoryId: Int): List<Recipe>
    
    @GET("recipe/{recipeId}")
    suspend fun getRecipeById(@Path("recipeId") recipeId: Int): Recipe
    
        
    @GET("recipes/favorites")
    suspend fun getFavoriteRecipes(@Query("ids") ids: String): List<Recipe>
}
