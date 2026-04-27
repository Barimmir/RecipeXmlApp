package com.example.recipexmlapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipexmlapp.data.AppDatabase
import com.example.recipexmlapp.data.CategoriesDao
import com.example.recipexmlapp.data.RecipesDao
import com.example.recipexmlapp.data.RecipesRepository
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

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
    
    val recipesRepository: RecipesRepository by lazy {
        RecipesRepository(
            appDatabase = appDatabase,
            categoriesDao = categoriesDao,
            recipesDao = recipesDao,
            ioDispatcher = ioDispatcher
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
