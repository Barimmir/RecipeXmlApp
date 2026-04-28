package com.example.recipexmlapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE categoryId = :categoryId")
    suspend fun getRecipesByCategory(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipes: List<Recipe>)

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM recipe WHERE id = :recipeId")
    suspend fun getRecipeFavoriteStatus(recipeId: Int): Boolean?

    @Query("SELECT COUNT(*) FROM recipe WHERE id = :recipeId")
    suspend fun recipeExists(recipeId: Int): Int

    @Query("UPDATE recipe SET title = :title, description = :description, ingredients = :ingredients, method = :method, imageUrl = :imageUrl, categoryId = :categoryId WHERE id = :recipeId")
    suspend fun updateRecipeFields(recipeId: Int, title: String, description: String, ingredients: List<Ingredient>, method: List<String>, imageUrl: String, categoryId: Int)
}
