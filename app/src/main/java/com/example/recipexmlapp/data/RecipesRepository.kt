package com.example.recipexmlapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.Executors

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

    private val threadPool = Executors.newFixedThreadPool(10)

    fun getCategories(callback: (List<Category>?) -> Unit) {
        threadPool.submit {
            try {
                val response = apiService.getCategories().execute()

                if (response.isSuccessful()) {
                    val categories = response.body()
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
                val response = apiService.getRecipesByCategory(categoryId).execute()
                val recipes = response.body()
                callback(recipes)
            } catch (e: Exception) {
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
                e.printStackTrace()
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
                e.printStackTrace()
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
                e.printStackTrace()
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
                e.printStackTrace()
                callback(null)
            }
        }
    }

    fun shutdown() {
        threadPool.shutdown()
    }
}
