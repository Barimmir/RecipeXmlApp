package com.example.recipexmlapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RecipesRepository(
    private val appDatabase: AppDatabase,
    private val categoriesDao: CategoriesDao,
    private val recipesDao: RecipesDao,
    private val ioDispatcher: CoroutineContext,
    private val apiService: RecipeApiService
) {

    suspend fun getCategoriesFromCache(): List<Category>? {
        return try {
            categoriesDao.getAllCategories()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        try {
            categoriesDao.addCategories(categories)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getRecipesByCategoryFromCache(categoryId: Int): List<Recipe>? {
        return try {
            recipesDao.getRecipesByCategory(categoryId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        try {
            recipesDao.addRecipes(recipes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getFavoriteRecipesFromCache(): List<Recipe>? {
        return try {
            recipesDao.getFavoriteRecipes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addToFavorites(recipeId: Int) {
        try {
            recipesDao.updateFavoriteStatus(recipeId, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeFromFavorites(recipeId: Int) {
        try {
            recipesDao.updateFavoriteStatus(recipeId, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCategories(): List<Category>? {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getCategories()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getRecipesByCategory(categoryId: Int): List<Recipe>? {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getRecipesByCategory(categoryId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getRecipeById(recipeId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
