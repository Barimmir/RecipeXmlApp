package com.example.recipexmlapp.data

import android.content.Context
import androidx.room.Room
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
    private var appDatabase: AppDatabase? = null
    private var categoriesDao: CategoriesDao? = null

    fun initialize(context: Context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "recipe_database"
            ).build()
            categoriesDao = appDatabase?.categoriesDao()
        }
    }

    suspend fun getCategoriesFromCache(): List<Category>? {
        return try {
            categoriesDao?.getAllCategories()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        try {
            categoriesDao?.addCategories(categories)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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
