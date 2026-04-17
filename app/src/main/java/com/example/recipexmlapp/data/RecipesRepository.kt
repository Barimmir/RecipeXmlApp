package com.example.recipexmlapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RecipesRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiService = retrofit.create<RecipeApiService>()


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

    
    suspend fun getFavoriteRecipes(ids: Set<Int>): List<Recipe>? {
        return try {
            withContext(Dispatchers.IO) {
                val recipes = mutableListOf<Recipe>()
                for (id in ids) {
                    val recipe = apiService.getRecipeById(id)
                    recipes.add(recipe)
                }
                recipes
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
