package com.example.recipexmlapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.Executors

class RecipesRepository {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    
    private val apiService = retrofit.create<RecipeApiService>()
    
    private val threadPool = Executors.newFixedThreadPool(10)
    
    fun getCategories(callback: (List<Category>?) -> Unit) {
        println("RecipesRepository: getCategories called")
        threadPool.submit {
            try {
                println("RecipesRepository: Starting API call...")
                val response = apiService.getCategories().execute()
                println("RecipesRepository: Response code: ${response.code()}")
                println("RecipesRepository: Response message: ${response.message()}")
                println("RecipesRepository: Response successful: ${response.isSuccessful()}")
                
                if (response.isSuccessful()) {
                    val categories = response.body()
                    println("RecipesRepository: Categories received: ${categories?.size ?: 0}")
                    categories?.forEach { category ->
                        println("RecipesRepository: Category: ${category.id} - ${category.title}")
                    }
                    callback(categories)
                } else {
                    callback(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }
    
    fun getRecipesByCategory(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        threadPool.submit {
            try {
                println("Getting recipes for category $categoryId...")
                val response = apiService.getRecipesByCategory(categoryId).execute()
                println("Recipes response code: ${response.code()}")
                println("Recipes response message: ${response.message()}")
                val recipes = response.body()
                println("Recipes received: ${recipes?.size ?: 0}")
                recipes?.forEach { recipe ->
                    println("Recipe: ${recipe.id} - ${recipe.title}")
                }
                callback(recipes)
            } catch (e: Exception) {
                println("Error getting recipes for category $categoryId: ${e.message}")
                e.printStackTrace()
                callback(null)
            }
        }
    }
    
    fun getRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        threadPool.submit {
            try {
                val response = apiService.getRecipeById(recipeId).execute()
                val recipe = response.body()
                callback(recipe)
            } catch (e: Exception) {
                println("Error getting recipe $recipeId: ${e.message}")
                callback(null)
            }
        }
    }
    
    fun getRecipesByIds(ids: Set<Int>, callback: (List<Recipe>?) -> Unit) {
        threadPool.submit {
            try {
                val idsString = ids.joinToString(",")
                val response = apiService.getRecipesByIds(idsString).execute()
                val recipes = response.body()
                callback(recipes)
            } catch (e: Exception) {
                println("Error getting recipes by ids: ${e.message}")
                callback(null)
            }
        }
    }
    
    fun searchRecipes(query: String, callback: (List<Recipe>?) -> Unit) {
        threadPool.submit {
            try {
                val response = apiService.searchRecipes(query).execute()
                val recipes = response.body()
                callback(recipes)
            } catch (e: Exception) {
                println("Error searching recipes: ${e.message}")
                callback(null)
            }
        }
    }
    
    fun getFavoriteRecipes(ids: Set<Int>, callback: (List<Recipe>?) -> Unit) {
        threadPool.submit {
            try {
                val idsString = ids.joinToString(",")
                val response = apiService.getFavoriteRecipes(idsString).execute()
                val recipes = response.body()
                callback(recipes)
            } catch (e: Exception) {
                println("Error getting favorite recipes: ${e.message}")
                callback(null)
            }
        }
    }
    
    fun shutdown() {
        threadPool.shutdown()
    }
}
