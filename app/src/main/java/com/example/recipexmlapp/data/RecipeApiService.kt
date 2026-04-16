package com.example.recipexmlapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    
    @GET("category")
    fun getCategories(): Call<List<Category>>
    
    @GET("category/{categoryId}/recipes")
    fun getRecipesByCategory(@Path("categoryId") categoryId: Int): Call<List<Recipe>>
    
    @GET("recipe/{recipeId}")
    fun getRecipeById(@Path("recipeId") recipeId: Int): Call<Recipe>
    
    @GET("recipes")
    fun getRecipesByIds(@Query("ids") ids: String): Call<List<Recipe>>
    
    @GET("recipes/search")
    fun searchRecipes(@Query("q") query: String): Call<List<Recipe>>
    
    @GET("recipes/favorites")
    fun getFavoriteRecipes(@Query("ids") ids: String): Call<List<Recipe>>
}
