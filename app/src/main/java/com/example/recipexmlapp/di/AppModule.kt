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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineContext = Dispatchers.IO

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "recipe_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideCategoriesDao(appDatabase: AppDatabase): CategoriesDao {
        return appDatabase.categoriesDao()
    }

    @Provides
    fun provideRecipesDao(appDatabase: AppDatabase): RecipesDao {
        return appDatabase.recipesDao()
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(
        appDatabase: AppDatabase,
        categoriesDao: CategoriesDao,
        recipesDao: RecipesDao,
        ioDispatcher: CoroutineContext,
        apiService: RecipeApiService
    ): RecipesRepository {
        return RecipesRepository(
            appDatabase = appDatabase,
            categoriesDao = categoriesDao,
            recipesDao = recipesDao,
            ioDispatcher = ioDispatcher,
            apiService = apiService
        )
    }
}
