package com.example.recipexmlapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipexmlapp.data.AppDatabase
import com.example.recipexmlapp.data.CategoriesDao
import com.example.recipexmlapp.data.RecipesDao
import com.example.recipexmlapp.data.RecipesRepository
import com.example.recipexmlapp.data.RecipeApiService
import com.example.recipexmlapp.data.ApiConstants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

class AppContainer(context: Context) {
    
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
    
    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "recipe_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }
    
    private val categoriesDao: CategoriesDao by lazy {
        appDatabase.categoriesDao()
    }
    
    private val recipesDao: RecipesDao by lazy {
        appDatabase.recipesDao()
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
    
    val recipesRepository: RecipesRepository by lazy {
        RecipesRepository(
            appDatabase = appDatabase,
            categoriesDao = categoriesDao,
            recipesDao = recipesDao,
            ioDispatcher = ioDispatcher,
            apiService = apiService
        )
    }
    val categoriesListViewModelFactory: CategoriesListViewModelFactory by lazy {
        CategoriesListViewModelFactory(recipesRepository)
    }
    
    val recipesListViewModelFactory: RecipesListViewModelFactory by lazy {
        RecipesListViewModelFactory(recipesRepository, context.applicationContext as android.app.Application)
    }
    
    val favoritesViewModelFactory: FavoritesViewModelFactory by lazy {
        FavoritesViewModelFactory(recipesRepository, context.applicationContext as android.app.Application)
    }
    
    val recipeViewModelFactory: RecipeViewModelFactory by lazy {
        RecipeViewModelFactory(recipesRepository, context.applicationContext as android.app.Application)
    }
}
